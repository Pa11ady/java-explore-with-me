package ru.practicum.explorewithme.stats;

import ru.practicum.explorewithme.stats.dto.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    EndpointHit create(EndpointHit endpointHit);

    List<ViewStats> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
