package ru.practicum.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.request.model.ParticipationRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
    ParticipationRequest findByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findAllByEventIdAndStatus(Long eventId, Status status);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    List<ParticipationRequest> findAllByEventId(Long eventId);
}
