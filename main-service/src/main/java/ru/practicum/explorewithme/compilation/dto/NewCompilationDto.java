package ru.practicum.explorewithme.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank
    private String title;
    private boolean pinned = false;
    private List<Long> events;
}
