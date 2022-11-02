package ru.practicum.explorewithme.event;

import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    List<EventShortDto> findEvents(String text, Set<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from,
                                   Integer size);

    EventFullDto findEventByID(Long eventId);

    List<EventShortDto> findEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto createEvent(NewEventDto newEventDto, Long userId);

    EventFullDto findUserEventById(Long eventId, Long userId);

    EventFullDto cancel(Long eventId, Long userId);

    List<ParticipationRequestDto> findRequests(Long userId, Long eventId);

    ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto reject(Long userId, Long eventId, Long reqId);

    List<EventFullDto> findEventsAdmin(Set<Long> users, Set<State> states, Set<Long> categories, LocalDateTime parse, LocalDateTime parse1, Integer from, Integer size);

    EventFullDto updateAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto publish(Long eventId);

    EventFullDto rejectAdmin(Long eventId);
}
