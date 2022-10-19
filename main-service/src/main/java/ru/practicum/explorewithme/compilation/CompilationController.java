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
import java.util.Collection;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CompilationController {

    @GetMapping("/compilations")
    public Collection<CompilationDto> findCompilations(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(defaultValue = "10") Integer size,
                                                       @RequestParam(required = false) Boolean pinned, HttpServletRequest request) {
        log.info("{}: {};  получение подборок с {} в количестве {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                from,
                size);
        return null;
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto findCompilationById(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: {}; получение подборки ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        return null;
    }

    @PostMapping("/admin/compilations")
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto, HttpServletRequest request) {
        log.info("{}: {}; добавление подборки {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                newCompilationDto.toString());
        return null;
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void delete(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: {}; удаление подборки с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; удаление события ID={} из подборки ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId,
                compId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public CompilationDto addEventToComp(@PathVariable Long compId, @PathVariable Long eventId, HttpServletRequest request) {
        log.info("{}: {}; добавление события ID={} в подборку ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                eventId,
                compId);
        return null;
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public CompilationDto unpin(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: {}; открепление подборки ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        return null;
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public CompilationDto pin(@PathVariable Long compId, HttpServletRequest request) {
        log.info("{}: {}; закрепление подборки с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                compId);
        return null;
    }
}
