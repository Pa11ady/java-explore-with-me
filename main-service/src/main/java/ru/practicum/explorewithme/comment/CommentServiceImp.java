package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.explorewithme.event.State;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.model.User;

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

    private void statusNotApprovedIsError(Comment comment, String errorMsg) {
        if (!State.PUBLISHED.equals(comment.getStatus())) {
            throw new ForbiddenException(errorMsg);
        }
    }

    private void checkAuthor(Comment comment, User user) {
        Long userId = user.getId();
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ForbiddenException("Отредактировать комментарий может только автор!");
        }
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
        statusNotApprovedIsError(comment, "Событие не опубликовано!");
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
    public List<CommentDto> adminFindEvents(Set<Long> users, Set<State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        Pageable pageable = new OffsetPage(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Comment> result = commentRepository.findAll(pageable).getContent();
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
