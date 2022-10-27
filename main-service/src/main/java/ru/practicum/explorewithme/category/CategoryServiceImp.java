package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.model.Category;
import ru.practicum.explorewithme.common.OffsetPage;
import ru.practicum.explorewithme.common.exception.AlreadyExistException;
import ru.practicum.explorewithme.common.exception.NotFoundException;
import ru.practicum.explorewithme.common.exception.NotValidException;
import ru.practicum.explorewithme.event.EventRepository;
import ru.practicum.explorewithme.event.model.Event;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    private Category getCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Категория ID=" + categoryId + " не найдена!"));
    }

    @Override
    public CategoryDto findCategoryById(Long categoryId) {
        return CategoryMapper.mapToCategoryDto(getCategory(categoryId));
    }

    @Override
    public List<CategoryDto> findCategories(Integer from, Integer size) {
        Pageable pageable = new OffsetPage(from, size, Sort.by(Sort.Direction.ASC, "id"));
        List<Category> categories = categoryRepository.findAll(pageable).getContent();
        return CategoryMapper.mapToCategoryDto(categories);
    }

    @Transactional
    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = getCategory(categoryDto.getId());
        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        return CategoryMapper.mapToCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        try {
            Category category = categoryRepository.save(CategoryMapper.mapToCategory(newCategoryDto));
            return CategoryMapper.mapToCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyExistException("Категория с названием " + newCategoryDto.getName() +
                    " уже существует!");
        }
    }

    @Transactional
    @Override
    public void delete(Long categoryId) {
        List<Event> events =  eventRepository.findByCategoryId(categoryId);
        if (!events.isEmpty()) {
            throw new NotValidException("Нельзя удалить категорию с событиями!");
        }
        getCategory(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
