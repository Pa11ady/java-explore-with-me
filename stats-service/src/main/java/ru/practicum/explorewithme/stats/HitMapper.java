package ru.practicum.explorewithme.stats;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.stats.model.Hit;
import ru.practicum.explorewithme.stats.dto.EndpointHit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HitMapper {
    public static EndpointHit mapToEndpointHit(Hit hit) {
        return new EndpointHit(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }

    public static Hit mapToHit(EndpointHit endpointHit) {
        return new Hit(
                null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp()
        );
    }
}
