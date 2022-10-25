package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.event.model.Event;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImp implements RequestService {
    @Override
    public List<ParticipationRequestDto> findRequestsByEvent(Event event) {
        return null;
    }

    @Override
    public ParticipationRequestDto confirm(Event event, Long reqId) {
        return null;
    }

    @Override
    public ParticipationRequestDto reject(Event event, Long reqId) {
        return null;
    }
}
