package ru.practicum.explorewithme.category;

import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto findCategoryById(Long categoryId);

    List<CategoryDto> findCategories(Integer from, Integer size);

    CategoryDto update(CategoryDto categoryDto);

    CategoryDto create(NewCategoryDto newCategoryDto);

    void delete(Long categoryId);
}
