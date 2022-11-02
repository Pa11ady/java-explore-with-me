package ru.practicum.explorewithme.stats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.stats.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT new ru.practicum.explorewithme.stats.ViewStats(app, uri, COUNT(DISTINCT ip)) " +
            "FROM Hit " +
            "WHERE uri IN :uris AND (timestamp >= :start AND timestamp < :end) GROUP BY app, uri"
    )
    List<ViewStats> calculateUniqueStats(List<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.stats.ViewStats(app, uri, COUNT(ip)) " +
            "FROM Hit " +
            "WHERE uri IN :uris AND (timestamp >= :start AND timestamp <= :end) GROUP BY app, uri"
    )
    List<ViewStats> calculateStats(List<String> uris, LocalDateTime start, LocalDateTime end);

}
