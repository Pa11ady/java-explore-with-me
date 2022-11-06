package ru.practicum.explorewithme.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {
    @NotNull
    private Long eventId;
    @Size(min = 20, max = 2000)
    private  String text;
}
