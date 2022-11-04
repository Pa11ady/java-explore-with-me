package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentRequest;
import ru.practicum.explorewithme.event.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImp implements CommentService {
    @Transactional
    @Override
    public CommentDto create(Long userId, NewCommentDto newCommentDto) {
        return null;
    }

    @Transactional
    @Override
    public CommentDto update(Long userId, UpdateCommentRequest updateCommentRequest) {
        return null;
    }

    @Transactional
    @Override
    public void delete(Long userId, Long commentId) {

    }

    @Override
    public List<CommentDto> findCommentsByUser(Long userId, Integer from, Integer size) {
        return null;
    }

    @Override
    public List<CommentDto> findCommentsByEvent(Long eventId, Integer from, Integer size) {
        return null;
    }

    @Override
    public CommentDto findCommentById(Long commentId) {
        return null;
    }

    @Transactional
    @Override
    public CommentDto adminApprove(Long commentId) {
        return null;
    }

    @Transactional
    @Override
    public CommentDto adminReject(Long commentId) {
        return null;
    }

    @Override
    public List<CommentDto> adminFindEvents(Set<Long> users, Set<State> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        return null;
    }

    @Transactional
    @Override
    public void adminDelete(Long commentId) {

    }

    @Transactional
    @Override
    public CommentDto adminUpdate(UpdateCommentRequest updateCommentRequest) {
        return null;
    }
}
