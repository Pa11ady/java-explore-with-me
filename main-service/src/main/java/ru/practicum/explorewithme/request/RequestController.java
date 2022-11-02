package ru.practicum.explorewithme.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.request.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> findRequestsByUserId(@PathVariable Long userId,
                                                              HttpServletRequest request) {
        log.info("{}: {}; получение заявок от пользователя ID={}",
                request.getRemoteAddr(), request.getRequestURI(), userId);
        return requestService.findRequestsByUserId(userId);
    }

    @PostMapping
    public ParticipationRequestDto create(@RequestParam Long eventId,
                                          @PathVariable Long userId,
                                          HttpServletRequest request) {
        log.info("{}: {}; участие в событии ID={} от пользователя ID={}",
                request.getRemoteAddr(), request.getRequestURI(), eventId, userId);
        return requestService.create(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId,
                                          HttpServletRequest request) {
        log.info("{}: {}; отмена заявки ID={} от пользователя ID={}",
                request.getRemoteAddr(), request.getRequestURI(), requestId, userId);
        return requestService.cancel(userId, requestId);
    }
}
