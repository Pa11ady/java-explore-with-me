package ru.practicum.explorewithme.category.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank
    private String name;
}
