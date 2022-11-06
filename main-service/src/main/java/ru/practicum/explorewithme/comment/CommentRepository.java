package ru.practicum.explorewithme.comment;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.comment.model.Comment;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByAuthorId(Long userId, Pageable page);

    List<Comment> findByEventIdAndStatus(Long eventId, Status status, Pageable page);

    @Query("SELECT c FROM Comment AS c " +
            "WHERE (c.status IN :statuses)")
    List<Comment> findByStatuses(Collection<Status> statuses, Pageable pageable);
}
