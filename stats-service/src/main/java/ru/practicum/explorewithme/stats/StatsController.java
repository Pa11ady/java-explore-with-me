package ru.practicum.explorewithme.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.stats.dto.EndpointHit;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final StatsService statsService;

    @PostMapping("/hit")
    public EndpointHit create(@RequestBody EndpointHit endpointHit, HttpServletRequest request) {
        log.info("{}: {}; добавление статистики {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                endpointHit.toString());
        return statsService.create(endpointHit);
    }

    @GetMapping("/stats")
    public List<ViewStats> findStats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam List<String> uris,
            @RequestParam(defaultValue = "false")
            boolean unique,
            HttpServletRequest request) {
        log.info("{}: {}; получение статистики",
                request.getRemoteAddr(),
                request.getRequestURI());
        return statsService.findStats(LocalDateTime.parse(start, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)),
                LocalDateTime.parse(end, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)), uris, unique);
    }
}