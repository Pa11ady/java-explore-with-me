package ru.practicum.explorewithme.category;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;
import ru.practicum.explorewithme.category.model.Category;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryMapper {
    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static List<CategoryDto> mapToCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();

        for (var el : categories) {
            result.add(mapToCategoryDto(el));
        }

        return result;
    }

    public static Category mapToCategory(NewCategoryDto newCategoryDto) {
        Category category = new Category();
        category.setName(newCategoryDto.getName());
        return category;
    }
}
