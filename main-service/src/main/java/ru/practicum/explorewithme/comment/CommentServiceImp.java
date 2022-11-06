package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentRequest;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.common.OffsetPage;
import ru.practicum.explorewithme.common.exception.ForbiddenException;
import ru.practicum.explorewithme.common.exception.NotFoundException;
import ru.practicum.explorewithme.common.exception.NotValidException;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.model.User;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final EntityManager entityManager;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь ID=" + userId + " не найден!"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие ID=" + eventId + " не найдено!"));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Комментарий ID=" + commentId + " не найден!"));
    }

    private void statusApprovedIsError(Comment comment) {
        if (Status.APPROVED.equals(comment.getStatus())) {
            throw new ForbiddenException("Статус комментария не должен быть одобрен!");
        }
    }

    private void statusNotApprovedIsError(Comment comment) {
        if (!Status.APPROVED.equals(comment.getStatus())) {
            throw new ForbiddenException("Комментарий не одобрен!");
        }
    }

    private void checkAuthor(Comment comment, User user) {
        Long userId = user.getId();
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ForbiddenException("Отредактировать комментарий может только автор!");
        }
    }

    private void setFilters(Session session,  Set<Long> users, LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (users != null) {
            session.enableFilter("usersFilter").setParameterList("userIds", users);
        }

        rangeStart = (rangeStart != null) ? rangeStart : LocalDateTime.now();
        rangeEnd = (rangeEnd != null) ? rangeEnd : LocalDateTime.now().plusYears(100);
        if (rangeStart.isAfter(rangeEnd)) {
            throw new NotValidException("Дата и время окончаний события не может быть раньше даты начала событий!");
        }
        Filter dateFilter = session.enableFilter("dateFilter");
        dateFilter.setParameter("rangeStart", rangeStart);
        dateFilter.setParameter("rangeEnd", rangeEnd);
    }

    @Transactional
    @Override
    public CommentDto create(Long userId, NewCommentDto newCommentDto) {
        User user = getUser(userId);
        Event event = getEvent(newCommentDto.getEventId());

        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new NotValidException("Нельзя оставлять комментарии на событие, которое не началось");
        }
        Comment comment = new Comment(null, newCommentDto.getText(), user, LocalDateTime.now(), event, Status.PENDING);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto update(Long userId, UpdateCommentRequest updateCommentRequest) {
        Comment comment = getComment(updateCommentRequest.getId());
        User user = getUser(userId);
        checkAuthor(comment, user);
        statusApprovedIsError(comment);
        comment.setText(updateCommentRequest.getText());
        comment.setStatus(Status.PENDING);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public void delete(Long userId, Long commentId) {
        Comment comment = getComment(commentId);
        User user = getUser(userId);
        checkAuthor(comment, user);
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findCommentsByUser(Long userId, Integer from, Integer size) {
        getUser(userId);
        Pageable pageable = new OffsetPage(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return CommentMapper.mapToCommentDto(commentRepository.findByAuthorId(userId, pageable));
    }

    @Override
    public List<CommentDto> findCommentsByEvent(Long eventId, Integer from, Integer size) {
        getEvent(eventId);
        Pageable pageable = new OffsetPage(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return CommentMapper.mapToCommentDto(commentRepository.findByEventId(eventId, pageable));
    }

    @Override
    public CommentDto findCommentById(Long commentId) {
        Comment comment = getComment(commentId);
        statusNotApprovedIsError(comment);
        return CommentMapper.mapToCommentDto(comment);
    }

    @Transactional
    @Override
    public CommentDto adminApprove(Long commentId) {
        Comment comment = getComment(commentId);
        statusApprovedIsError(comment);
        comment.setStatus(Status.APPROVED);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Transactional
    @Override
    public CommentDto adminReject(Long commentId) {
        Comment comment = getComment(commentId);
        statusApprovedIsError(comment);
        comment.setStatus(Status.REJECTED);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> adminFindComments(Set<Long> users, Set<Status> statuses, LocalDateTime rangeStart,
                                              LocalDateTime rangeEnd, Integer from, Integer size) {
        if (statuses == null) {
            statuses = Set.of(Status.PENDING, Status.APPROVED, Status.REJECTED);
        }
        Session session = entityManager.unwrap(Session.class);
        setFilters(session, users, rangeStart, rangeEnd);
        Pageable pageable = new OffsetPage(from, size, Sort.by(Sort.Direction.ASC, "id"));

        List<Comment> result = commentRepository.findByStatuses(statuses, pageable);

        session.disableFilter("dateFilter");
        session.disableFilter("usersFilter");

        return CommentMapper.mapToCommentDto(result);
    }

    @Transactional
    @Override
    public void adminDelete(Long commentId) {
        getComment(commentId);
        commentRepository.deleteById(commentId);
    }

    @Transactional
    @Override
    public CommentDto adminUpdate(UpdateCommentRequest updateCommentRequest) {
        Comment comment = getComment(updateCommentRequest.getId());
        comment.setText(updateCommentRequest.getText());
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }
}
