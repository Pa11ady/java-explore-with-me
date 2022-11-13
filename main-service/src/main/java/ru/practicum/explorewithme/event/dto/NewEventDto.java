package ru.practicum.explorewithme.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ru.practicum.explorewithme.event.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotNull
    @JsonProperty("category")
    private Long categoryId;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private boolean paid = false;

    private Integer participantLimit = 10;

    private boolean requestModeration = true;

    @NotNull
    @Size(min = 3, max = 120)
    private String title;
}
