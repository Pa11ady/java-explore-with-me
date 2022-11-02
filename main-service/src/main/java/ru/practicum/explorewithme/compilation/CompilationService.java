package ru.practicum.explorewithme.compilation;

import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> findCompilations(Integer from, Integer size, Boolean pinned);

    CompilationDto findCompilationById(Long compId);

    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    void deleteEvent(Long compId, Long eventId);

    CompilationDto addEvent(Long compId, Long eventId);

    CompilationDto unpin(Long compId);

    CompilationDto pin(Long compId);
}
