package ru.practicum.explorewithme.stats;


public interface StatsService {
    Long getViews(String uri);

    void setHits(String uri, String ip);
}
