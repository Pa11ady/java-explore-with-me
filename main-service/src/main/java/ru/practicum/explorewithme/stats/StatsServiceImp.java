package ru.practicum.explorewithme.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.stats.client.StatsClient;
import ru.practicum.explorewithme.stats.client.dto.EndpointHit;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImp implements StatsService {
    private final StatsClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Long getViews(String uri) {
        String start = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC).format(formatter);
        String end = LocalDateTime.now().plusYears(100).format(formatter);
        List<ViewStats> viewStats = statsClient.findStats(start, end, uri, false);
        if (viewStats.isEmpty()) {
            return 0L;
        }
        return viewStats.get(0).getHits();
    }

    @Override
    public void setHits(String uri, String ip) {
        EndpointHit endpointHit = new EndpointHit(null, "ewm-service", uri, ip, LocalDateTime.now());
        statsClient.create(endpointHit);
    }
}
