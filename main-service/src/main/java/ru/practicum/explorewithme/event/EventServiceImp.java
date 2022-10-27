package ru.practicum.explorewithme.event;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.CategoryRepository;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.common.OffsetPage;
import ru.practicum.explorewithme.common.exception.ForbiddenException;
import ru.practicum.explorewithme.common.exception.NotFoundException;
import ru.practicum.explorewithme.common.exception.NotValidException;
import ru.practicum.explorewithme.event.dto.*;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.RequestService;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImp implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestService requestService;

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория ID=" + categoryId + " не найдена!"));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь ID=" + userId + " не найден!"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие ID=" + eventId + " не найдено!"));
    }

    @Override
    public List<EventShortDto> findEvents(String text, Set<Long> categories, Boolean paid, LocalDateTime parse, LocalDateTime parse1, Boolean onlyAvailable, EventSort sort, Integer from, Integer size) {
        return null;
    }

    @Override
    public EventFullDto findEventByID(Long eventId) {
        Event event = getEvent(eventId);
        if (!State.PUBLISHED.equals(event.getState())) {
            throw new ForbiddenException("Event not published.");
        }
        //todo статистика
        return EventMapper.mapToEventFullDto(event);
    }

    @Override
    public List<EventShortDto> findEventsByUser(Long userId, Integer from, Integer size) {
        getUser(userId);
        Pageable pageable = new OffsetPage(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        return EventMapper.mapToEventShortDto(events);
    }

    @Override
    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ForbiddenException("Дата и время события не могут быть раньше," +
                    " чем через два часа от текущего момента");
        }

        Category category = null;
        if (updateEventRequest.getCategoryId() != null) {
            category = getCategory(updateEventRequest.getCategoryId());
        }
        Event event = getEvent(updateEventRequest.getEventId());
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException("Отредактировать событие может только инициатор!");
        }
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Можно изменить только ожидающие или отмененные события");
        }
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategoryId() != null) {
            event.setCategory(category);
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }

        event.setState(State.PENDING);
        return EventMapper.mapToEventFullDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventFullDto createEvent(NewEventDto newEventDto, Long userId) {
        User user = getUser(userId);
        Category category = getCategory(newEventDto.getCategoryId());
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new NotValidException("Дата и время события не могут быть раньше, чем через два часа " +
                    "от текущего момента");
        }
        Event event = eventRepository.save(EventMapper.mapToEvent(newEventDto, user, category));
        return EventMapper.mapToEventFullDto(event);
    }

    @Override
    public EventFullDto findUserEventById(Long eventId, Long userId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException("Событие ID=" + eventId + " создано другим пользователем");
        }
        return EventMapper.mapToEventFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto cancel(Long eventId, Long userId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ForbiddenException("Только создатель события может его отменить.");
        }
        if (State.PENDING.equals(event.getState())) {
            event.setState(State.CANCELED);
        } else {
            throw new NotValidException("Можно изменить только ожидающие или отмененные события");
        }
        return EventMapper.mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<ParticipationRequestDto> findRequests(Long userId, Long eventId) {
        getUser(userId);
        Event event = getEvent(eventId);
        return requestService.findRequestsByEvent(event);
    }

    @Override
    public ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Запрос может подтверждать лишь инициатор!");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Нельзя подтверждать в неопубликованном событии!");
        }
        return requestService.confirm(event, reqId);
    }

    @Override
    public ParticipationRequestDto reject(Long userId, Long eventId, Long reqId) {
        getUser(userId);
        Event event = getEvent(eventId);
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Запрос может отклонять лишь инициатор!");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Нельзя отклонять в неопубликованном событии!");
        }
        return requestService.reject(reqId);
    }

    @Override
    public List<EventFullDto> findEventsAdmin(Set<Long> users, Set<State> states, Set<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from,
                                              Integer size) {
        return null;
    }

    @Transactional
    @Override
    public EventFullDto updateAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Category category = null;
        if (adminUpdateEventRequest.getCategoryId() != null) {
            category = getCategory(adminUpdateEventRequest.getCategoryId());
        }
        Event event = getEvent(eventId);
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategoryId() != null) {
            event.setCategory(category);
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLon(adminUpdateEventRequest.getLocation().getLon());
            event.setLat(adminUpdateEventRequest.getLocation().getLat());
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        return EventMapper.mapToEventFullDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventFullDto publish(Long eventId) {
        Event event = getEvent(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new NotValidException("Дата и время события не могут быть раньше, чем через час от " +
                    "текущего момента");
        }
        if (State.PENDING.equals(event.getState())) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(LocalDateTime.now());
        } else {
            throw new NotValidException("Только ожидающие события могут быть изменены");
        }
        return EventMapper.mapToEventFullDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public EventFullDto rejectAdmin(Long eventId) {
        Event event = getEvent(eventId);
        if (State.PENDING.equals(event.getState())) {
            event.setState(State.CANCELED);
        } else {
            throw new NotValidException("Только ожидающие события могут быть изменены");
        }
        return EventMapper.mapToEventFullDto(eventRepository.save(event));
    }
}
