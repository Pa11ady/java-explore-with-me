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
@RequestMapping(path = "/users")
public class RequestController {
    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findRequestsByUserId(@PathVariable Long userId,
                                                              HttpServletRequest request) {
        log.info("{}: {}; получение заявок от пользователя ID={}",
                request.getRemoteAddr(), request.getRequestURI(), userId);
        return null;
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto create(@RequestParam Long eventId,
                                          @PathVariable Long userId,
                                          HttpServletRequest request) {
        log.info("{}: {}; участие в событии ID={} от пользователя ID={}",
                request.getRemoteAddr(), request.getRequestURI(), eventId, userId);
        return null;
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancel(@PathVariable Long userId,
                                          @PathVariable Long requestId,
                                          HttpServletRequest request) {
        log.info("{}: {}; отмена заявки ID={} от пользователя ID={}",
                request.getRemoteAddr(), request.getRequestURI(), requestId, userId);
        return null;
    }
}
