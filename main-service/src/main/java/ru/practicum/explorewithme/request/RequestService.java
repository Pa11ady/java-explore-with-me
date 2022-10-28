package ru.practicum.explorewithme.request;

import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    long getAmountConfirmedParticipants(Long eventId);

    List<ParticipationRequestDto> findRequestsByEvent(Event event);

    ParticipationRequestDto confirm(Event event, Long reqId);

    ParticipationRequestDto reject(Long reqId);

    ParticipationRequestDto create(Long userId, Long eventId);

    List<ParticipationRequestDto> findRequestsByUserId(Long userId);

    ParticipationRequestDto cancel(Long userId, Long requestId);
}
