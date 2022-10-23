package ru.practicum.explorewithme.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.category.dto.CategoryDto;
import ru.practicum.explorewithme.category.dto.NewCategoryDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("/categories/{categoryId}")
    public CategoryDto findCategoryById(@PathVariable Long categoryId, HttpServletRequest request) {
        log.info("{}: {}; на получение категории ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                categoryId);
        return categoryService.findCategoryById(categoryId);
    }

    @GetMapping("/categories")
    public List<CategoryDto> findCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                            @Positive @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("{}: {}; получение списка категорий",
                request.getRemoteAddr(),
                request.getRequestURI());
        return categoryService.findCategories(from, size);
    }

    @PatchMapping("/admin/categories")
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto, HttpServletRequest request) {
        log.info("{}: {}; на изменение категории {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                categoryDto.toString());
        return categoryService.update(categoryDto);
    }

    @PostMapping("/admin/categories")
    public CategoryDto create(@Valid @RequestBody NewCategoryDto newCategoryDto, HttpServletRequest request) {
        log.info("{}: {}; добавление категории {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                newCategoryDto.toString());
        return categoryService.create(newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public void delete(@PathVariable Long categoryId, HttpServletRequest request) {
        log.info("{}: {}; удаление категории ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                categoryId);
        categoryService.delete(categoryId);
    }
}
