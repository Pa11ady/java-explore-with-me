package ru.practicum.explorewithme.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithme.comment.Status;
import ru.practicum.explorewithme.event.dto.EventShortDto;
import ru.practicum.explorewithme.user.dto.UserShortDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private  Long id;
    private  String text;
    private UserShortDto author;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private EventShortDto event;
    private Status status;
}
