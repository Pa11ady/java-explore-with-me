package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping("/compilations")
    public List<CompilationDto> findCompilations(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size,
                                                 @RequestParam(defaultValue = "false") Boolean pinned,
                                                 HttpServletRequest request) {
        log.info("{}: {};  получение подборок с {} в количестве {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                from,
                size);
        return compilationService.findCompilations(from, size, pinned);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto findCompilationById(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: {}; получение подборки ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        return compilationService.findCompilationById(compId);
    }

    @PostMapping("/admin/compilations")
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto, HttpServletRequest request) {
        log.info("{}: {}; добавление подборки {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                newCompilationDto.toString());
        return compilationService.create(newCompilationDto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void delete(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: {}; удаление подборки с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        compilationService.delete(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEvent(@PathVariable Long compId, @PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; удаление события ID={} из подборки ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId,
                compId);
        compilationService.deleteEvent(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public CompilationDto addEvent(@PathVariable Long compId, @PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; добавление события ID={} в подборку ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId,
                compId);
        return compilationService.addEvent(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public CompilationDto unpin(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: {}; открепление подборки ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        return compilationService.unpin(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public CompilationDto pin(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: {}; закрепление подборки с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        return compilationService.pin(compId);
    }
}
