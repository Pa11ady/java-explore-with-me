package ru.practicum.explorewithme.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.comment.model.Comment;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Long userId, Pageable page);

    List<Comment> findByEventId(Long eventId, Pageable page);
}
