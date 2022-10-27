package ru.practicum.explorewithme.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.common.OffsetPage;
import ru.practicum.explorewithme.common.exception.AlreadyExistException;
import ru.practicum.explorewithme.common.exception.NotFoundException;
import ru.practicum.explorewithme.compilation.dto.CompilationDto;
import ru.practicum.explorewithme.compilation.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilation.model.Compilation;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImp implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private Compilation getCompilation(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation with id=" +
                compId + " was not found."));
    }

    private Event getEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Событие ID=" + eventId +
                " не найдено!"));
    }

    private Set<Event> getEvents(Iterable<Long> ids) {
        return new HashSet<>(eventRepository.findAllById(ids));
    }

    private CompilationDto pinUnpin(Long compId, boolean pin) {
        Compilation compilation = getCompilation(compId);
        compilation.setPinned(pin);
        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> findCompilations(Integer from, Integer size, Boolean pinned) {
        Pageable pageable = new OffsetPage(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Compilation> Compilations;
        if (pinned) {
            Compilations = compilationRepository.findAllByPinned(pinned, pageable);

        } else {
            Compilations = compilationRepository.findAll(pageable).getContent();
        }
        return CompilationMapper.mapToCompilationDto(Compilations);
    }

    @Override
    public CompilationDto findCompilationById(Long compId) {
        return CompilationMapper.mapToCompilationDto(getCompilation(compId));
    }

    @Transactional
    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        try {
            Set<Event> events = getEvents(newCompilationDto.getEvents());
            Compilation compilation = CompilationMapper.mapToCompilation(newCompilationDto, events);
            return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilation));

        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("Подборка названием = " + newCompilationDto.getTitle() +
                    " уже существует!");
        }
    }

    @Transactional
    @Override
    public void delete(Long compId) {
        try {
            compilationRepository.deleteById(compId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found.");
        }
    }

    @Transactional
    @Override
    public void deleteEvent(Long compId, Long eventId) {
        Event event = getEvent(eventId);
        Compilation compilation = getCompilation(compId);
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Transactional
    @Override
    public CompilationDto addEvent(Long compId, Long eventId) {
        Event event = getEvent(eventId);
        Compilation compilation = getCompilation(compId);
        compilation.getEvents().add(event);
        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Transactional
    @Override
    public CompilationDto unpin(Long compId) {
        return pinUnpin(compId, false);
    }

    @Transactional
    @Override
    public CompilationDto pin(Long compId) {
        return pinUnpin(compId, true);
    }
}
