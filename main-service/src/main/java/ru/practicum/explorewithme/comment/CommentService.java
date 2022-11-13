package ru.practicum.explorewithme.comment;

import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface CommentService {
    CommentDto create(Long userId, NewCommentDto newCommentDto);

    CommentDto update(Long userId, UpdateCommentRequest updateCommentRequest);

    void delete(Long userId, Long commentId);

    List<CommentDto> findCommentsByUser(Long userId, Integer from, Integer size);

    List<CommentDto> findCommentsByEvent(Long eventId, Integer from, Integer size);

    CommentDto findCommentById(Long commentId);

    CommentDto adminApprove(Long commentId);

    CommentDto adminReject(Long commentId);

    List<CommentDto> adminFindComments(Set<Long> users, Set<Status> statuses, LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd, Integer from, Integer size);

    void adminDelete(Long commentId);

    CommentDto adminUpdate(UpdateCommentRequest updateCommentRequest);
}
