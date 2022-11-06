package ru.practicum.explorewithme.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.comment.dto.CommentDto;
import ru.practicum.explorewithme.comment.dto.NewCommentDto;
import ru.practicum.explorewithme.comment.dto.UpdateCommentRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CommentController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private  final CommentService commentService;

    @PostMapping("/users/{userId}/comments")
    public CommentDto create(@RequestBody @Valid NewCommentDto newCommentDto, @PathVariable Long userId,
                             HttpServletRequest request) {
        log.info("{}: {}; создание комментария {}",
                request.getRemoteAddr(), request.getRequestURI(), newCommentDto.toString());
        return  commentService.create(userId, newCommentDto);
    }

    @PutMapping("/users/{userId}/comments")
    public CommentDto update(@RequestBody @Valid UpdateCommentRequest updateCommentRequest, @PathVariable Long userId,
                             HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} редактирование комментария{}",
                request.getRemoteAddr(), request.getRequestURI(), userId, updateCommentRequest);
        return  commentService.update(userId, updateCommentRequest);
    }


    @DeleteMapping("/users/{userId}/comments/{commentId}")
    public void delete(@PathVariable Long userId, @PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}: {}; удаление комментария ID={} пользователем ID={}", request.getRemoteAddr(),
                request.getRequestURI(), commentId, userId);
        commentService.delete(userId, commentId);
    }

    @GetMapping("/users/{userId}/comments")
    public List<CommentDto> findCommentsByUser(@PathVariable Long userId,
                                               @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Valid @Positive @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        log.info("{}: {}; получение списка комментариев пользователя ID={}",
                request.getRemoteAddr(), request.getRequestURI(), userId);
        return commentService.findCommentsByUser(userId, from, size);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> findCommentsByEvent(@PathVariable Long eventId,
                                               @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Valid @Positive @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        log.info("{}: {}; анонимно получение списка комментариев по событию ID={}",
                request.getRemoteAddr(), request.getRequestURI(), eventId);
        return commentService.findCommentsByEvent(eventId, from, size);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto findCommentById(@PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}: {}; анонимно получение комментария с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                commentId);
        return commentService.findCommentById(commentId);
    }

    @PatchMapping("/admin/comments/{commentId}/approve")
    public CommentDto approve(@PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}: {}; админ одобрение  комментария ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                commentId);
        return commentService.adminApprove(commentId);
    }

    @PatchMapping("/admin/comments/{commentId}/reject")
    public CommentDto reject(@PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}: {}; админ отклонение комментария ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                commentId);
        return commentService.adminReject(commentId);
    }

    @GetMapping("/admin/comments")
    public List<CommentDto> adminFindComments(@RequestParam(required = false) Set<Long> users,
                                            @RequestParam(required = false) Set<Status> statuses,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("{}: {}; админ получение списка комментариев",
                request.getRemoteAddr(), request.getRequestURI());
        return commentService.adminFindComments(users, statuses, LocalDateTime.parse(rangeStart,
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)), LocalDateTime.parse(rangeEnd,
                DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)), from, size);
    }

    @DeleteMapping("/admin/comments/{commentId}")
    public void adminDelete(@PathVariable Long commentId, HttpServletRequest request) {
        log.info("{}: {}; админ удаление комментария ID={}", request.getRemoteAddr(),
                request.getRequestURI(), commentId);
        commentService.adminDelete(commentId);
    }

    @PutMapping("/admin/comments/")
    public CommentDto adminUpdate(@RequestBody @Valid UpdateCommentRequest updateCommentRequest,
                                  HttpServletRequest request) {
        log.info("{}: {}; редактирование события ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                updateCommentRequest.getId());
        return commentService.adminUpdate(updateCommentRequest);
    }
}
