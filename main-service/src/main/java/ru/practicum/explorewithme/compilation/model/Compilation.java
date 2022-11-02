package ru.practicum.explorewithme.compilation.model;

import lombok.*;
import ru.practicum.explorewithme.event.model.Event;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;

    private String title;
    private boolean pinned;

    @ManyToMany
    @JoinTable(
            name = "compilation_event",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    Set<Event> events = new HashSet<>();
}
