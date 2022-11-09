package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.stats.StatsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class EventController {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final EventService eventService;
    private final StatsService statsService;

    @GetMapping("/events")
    public List<EventShortDto> findEvents(@RequestParam(defaultValue = "") String text,
                                                @RequestParam(required = false) Set<Long> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false) String rangeStart,
                                                @RequestParam(required = false) String rangeEnd,
                                                @RequestParam(required = false) Boolean onlyAvailable,
                                                @RequestParam(required = false) EventSort sort,
                                                @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                @Positive @RequestParam(defaultValue = "10") Integer size,
                                                HttpServletRequest request) {
        log.info("{}: {}; получение списка событий",
                request.getRemoteAddr(), request.getRequestURI());
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        }
        List<EventShortDto> result = eventService.findEvents(text, categories, paid, start, end, onlyAvailable,
                sort, from, size);
        statsService.setHits(request.getRequestURI(), request.getRemoteAddr());
        return result;
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto findEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; получение события с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId);
        EventFullDto eventFullDto = eventService.findEventById(eventId);
        statsService.setHits(request.getRequestURI(), request.getRemoteAddr());
        return eventFullDto;
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> findEventsByUser(@PathVariable Long userId,
                                         @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Valid @Positive @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} " +
                        "на получение {} добавленных событий, начиная с {}",
                request.getRemoteAddr(), request.getRequestURI(), userId, size, from);
        return eventService.findEventsByUser(userId, from, size);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEvent(@Valid @RequestBody UpdateEventRequest updateEventRequest,
                                    @PathVariable Long userId,
                                    HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} обновление события {}",
                request.getRemoteAddr(), request.getRequestURI(), userId, updateEventRequest);
        return  eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto create(@Valid @RequestBody NewEventDto newEventDto,
                               @PathVariable Long userId,
                               HttpServletRequest request) {
        log.info("{}: {}; добавление события {}",
                request.getRemoteAddr(), request.getRequestURI(), newEventDto.toString());
        return eventService.createEvent(newEventDto, userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto findUserEventById(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} получение события ID={}",
                request.getRemoteAddr(), request.getRequestURI(), userId, eventId);
        return eventService.findUserEventById(eventId, userId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancel(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               HttpServletRequest request) {
        log.info("{}: {}; отмена события ID={}",
                request.getRemoteAddr(), request.getRequestURI(), eventId);
        return eventService.cancel(eventId, userId);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findRequests(@PathVariable Long userId,
                                                      @PathVariable Long eventId,
                                                      HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} получение заявок на участие в событии ID={} ",
                request.getRemoteAddr(), request.getRequestURI(), userId, eventId);
        return eventService.findRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirm(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @PathVariable Long reqId,
                                           HttpServletRequest request) {
        log.info("{}: {}; подтверждение заявки ID={} " +
                        "на участие в событии ID={} ",
                request.getRemoteAddr(), request.getRequestURI(), reqId, eventId);
        return eventService.confirm(userId, eventId, reqId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto reject(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @PathVariable Long reqId,
                                          HttpServletRequest request) {
        log.info("{}: {}; отклонение заявки ID={} на участие в событии ID={} ",
                request.getRemoteAddr(), request.getRequestURI(), reqId, eventId);
        return eventService.reject(userId, eventId, reqId);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> findEventsAdmin(@RequestParam(required = false) Set<Long> users,
                                              @RequestParam(required = false) Set<State> states,
                                              @RequestParam(required = false) Set<Long> categories,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                              @Positive @RequestParam(defaultValue = "10") Integer size,
                                              HttpServletRequest request) {
        log.info("{}: {}; получение списка событий",
                request.getRemoteAddr(), request.getRequestURI());
        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));

        }
        return eventService.findEventsAdmin(users, states, categories, start, end, from, size);
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateAdmin(@PathVariable Long eventId,
                               @RequestBody AdminUpdateEventRequest adminUpdateEventRequest,
                               HttpServletRequest request) {
        log.info("{}: {}; редактирование события ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId);
        return eventService.updateAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publish(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; публикация события ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId);
        return eventService.publish(eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectAdmin(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; отклонение события ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId);
        return eventService.rejectAdmin(eventId);
    }
}
