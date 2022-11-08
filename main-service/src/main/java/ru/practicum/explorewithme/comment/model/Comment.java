package ru.practicum.explorewithme.comment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import ru.practicum.explorewithme.comment.Status;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@FilterDef(name = "usersFilter", parameters = @ParamDef(name = "userIds", type = "java.lang.Long"))
@Filter(name = "usersFilter", condition = "author_id in (:userIds)")

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private  Long id;

    @Column(nullable = false)
    private  String text;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    private Status status;
}
