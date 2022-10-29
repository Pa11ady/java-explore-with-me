package ru.practicum.explorewithme.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.stats.model.Hit;
import ru.practicum.explorewithme.stats.dto.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor

public class StatsServiceImp implements StatsService {
    private final StatsRepository statsRepository;

    @Transactional
    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        Hit hit = statsRepository.save(HitMapper.mapToHit(endpointHit));
        return HitMapper.mapToEndpointHit(hit);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ViewStats> findStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<ViewStats> res;
        if (unique) {
            res = statsRepository.calculateUniqueStats(uris, start, end);
        } else {
            res = statsRepository.calculateStats(uris, start, end);
        }
        return res;
    }
}
