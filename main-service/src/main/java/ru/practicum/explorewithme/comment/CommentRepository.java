package ru.practicum.explorewithme.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Long userId, Pageable page);

    List<Comment> findByEventIdAndStatus(Long eventId, Status status, Pageable page);

    @Query("SELECT c FROM Comment AS c " +
            "WHERE (c.status IN :statuses) AND " +
            "(c.created BETWEEN :rangeStart AND :rangeEnd)")
    List<Comment> findByStatuses(Collection<Status> statuses, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
