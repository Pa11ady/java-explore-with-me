package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class EventController {
    @GetMapping(path = "/events")
    public Collection<EventShortDto> findEvents(@RequestParam(defaultValue = "") String text,
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
        return null;
    }

    @GetMapping("/events/{eventId}")
    public EventFullDto findEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; получение события с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId);
        return null;
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> findEvents(@PathVariable Long userId,
                                         @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                         @Valid @Positive @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} " +
                        "на получение {} добавленных событий, начиная с {}",
                request.getRemoteAddr(), request.getRequestURI(), userId, size, from);
        return null;
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEvent(@Valid @RequestBody UpdateEventRequest updateEventRequest,
                                    @PathVariable Long userId,
                                    HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} "  +
                        "обновление события {}",
                request.getRemoteAddr(), request.getRequestURI(), userId, updateEventRequest);
        return null;
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto create(@Valid @RequestBody NewEventDto newEventDto,
                               @PathVariable Long userId,
                               HttpServletRequest request) {
        log.info("{}: {}; добавление события {}",
                request.getRemoteAddr(), request.getRequestURI(), newEventDto.toString());
        return null;
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto findUserEventById(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} получение события ID={}",
                request.getRemoteAddr(), request.getRequestURI(), userId, eventId);
        return null;
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancel(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               HttpServletRequest request) {
        log.info("{}: {}; отмена события ID={}",
                request.getRemoteAddr(), request.getRequestURI(), eventId);
        return null;
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findRequests(@PathVariable Long userId,
                                                      @PathVariable Long eventId,
                                                      HttpServletRequest request) {
        log.info("{}: {};  пользователь ID={} получение заявок на участие в событии ID={} ",
                request.getRemoteAddr(), request.getRequestURI(), userId, eventId);
        return null;
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirm(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @PathVariable Long reqId,
                                           HttpServletRequest request) {
        log.info("{}: {}; подтверждение заявки ID={} " +
                        "на участие в событии ID={} ",
                request.getRemoteAddr(), request.getRequestURI(), reqId, eventId);
        return null;
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto reject(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @PathVariable Long reqId,
                                          HttpServletRequest request) {
        log.info("{}: {}; отклонение заявки ID={} на участие в событии ID={} ",
                request.getRemoteAddr(), request.getRequestURI(), reqId, eventId);
        return null;
    }

    @GetMapping("/admin/events")
    public Collection<EventFullDto> findEvents(@RequestParam(required = false) Set<Long> users,
                                               @RequestParam(required = false) Set<State> states,
                                               @RequestParam(required = false) Set<Long> categories,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        log.info("{}: {}; получение списка событий",
                request.getRemoteAddr(), request.getRequestURI());
        return null;
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody AdminUpdateEventRequest adminUpdateEventRequest,
                               HttpServletRequest request) {
        log.info("{}: {}; редактирование события ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId);
        return null;
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publish(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; публикация события ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId);
        return null;
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto reject(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; отклонение события ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId);
        return null;
    }
}
