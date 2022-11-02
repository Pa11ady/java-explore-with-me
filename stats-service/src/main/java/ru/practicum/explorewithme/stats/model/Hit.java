package ru.practicum.explorewithme.stats.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hits", schema = "public")
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id")
    private Long id;

    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
