package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.common.exception.ForbiddenException;
import ru.practicum.explorewithme.common.exception.NotFoundException;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.State;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.request.model.ParticipationRequest;
import ru.practicum.explorewithme.user.UserRepository;
import ru.practicum.explorewithme.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImp implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь ID=" + userId + " не найден!"));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие ID=" + eventId + " не найдено!"));
    }

    private ParticipationRequest getParticipationRequest(Long requestId) {
        return requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException(
                "Запрос ID=" + requestId + " не найден!"));
    }

    @Override
    public long getAmountConfirmedParticipants(Long eventId) {
        return requestRepository.findAllByEventIdAndStatus(eventId, Status.CONFIRMED).size();
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByEvent(Event event) {
        List<ParticipationRequest> participationRequests = requestRepository.findAllByEventId(event.getId());
        return RequestMapper.mapToParticipationRequestDto(participationRequests);
    }

    @Transactional
    @Override
    public ParticipationRequestDto confirm(Event event, Long reqId) {
        ParticipationRequest participationRequest = getParticipationRequest(reqId);
        long participants = 0;
        if (event.getParticipantLimit() != 0) {
            participants = getAmountConfirmedParticipants(event.getId());
            if (participants > event.getParticipantLimit()) {
                throw new ForbiddenException("У события достигнут лимит запросов на участие!");
            }
        }
        participationRequest.setStatus(Status.CONFIRMED);
        participationRequest = requestRepository.save(participationRequest);
        participants++;

        //достигнут лимит участия и отклоняем автоматом все остальные запросы
        if (participants >= event.getParticipantLimit()) {
            List<ParticipationRequest> requestsToReject = requestRepository.findAllByEventIdAndStatus(event.getId(),
                    Status.PENDING);
            requestsToReject.forEach(el -> el.setStatus(Status.REJECTED));
            requestRepository.saveAll(requestsToReject);
        }
        return RequestMapper.mapToParticipationRequestDto(participationRequest);
    }

    @Transactional
    @Override
    public ParticipationRequestDto reject(Long reqId) {
        ParticipationRequest participationRequest = getParticipationRequest(reqId);
        participationRequest.setStatus(Status.REJECTED);
        return RequestMapper.mapToParticipationRequestDto(requestRepository.save(participationRequest));
    }

    @Transactional
    @Override
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User user = getUser(userId);
        Event event = getEvent(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Инициатор события не может добавить запрос на участие в своём событии");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ForbiddenException("Нельзя участвовать в неопубликованном событии!");
        }
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            throw new ForbiddenException("Нельзя добавить повторный запрос!");
        }
        if (event.getParticipantLimit() != 0) {
            if (getAmountConfirmedParticipants(eventId) > event.getParticipantLimit()) {
                throw new ForbiddenException("Достигнут лимит запросов на участие!");
            }
        }

        ParticipationRequest participationRequest = new ParticipationRequest(null, LocalDateTime.now(), event, user,
                Status.PENDING);
        if (!event.getRequestModeration()) {
            participationRequest.setStatus(Status.CONFIRMED);
        }
        return RequestMapper.mapToParticipationRequestDto(requestRepository.save(participationRequest));
    }

    @Override
    public List<ParticipationRequestDto> findRequestsByUserId(Long userId) {
        getUser(userId);
        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);
        return RequestMapper.mapToParticipationRequestDto(requests);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        getUser(userId);
        ParticipationRequest participationRequest = getParticipationRequest(requestId);
        if (participationRequest.getRequester().getId().equals(userId)) {
            participationRequest.setStatus(Status.CANCELED);
        } else {
            throw new ForbiddenException("Пользователь ID=" + userId + " не подавал заявку  ID=" + requestId);
        }
        return RequestMapper.mapToParticipationRequestDto(requestRepository.save(participationRequest));
    }
}
