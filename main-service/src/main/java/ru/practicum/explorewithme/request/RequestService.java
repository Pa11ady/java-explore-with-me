package ru.practicum.explorewithme.request;

import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> findRequestsByEvent(Event event);

    ParticipationRequestDto confirm(Event event, Long reqId);

    ParticipationRequestDto reject(Event event, Long reqId);
}
