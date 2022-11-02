package ru.practicum.explorewithme.request.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.practicum.explorewithme.request.Status;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @JsonProperty("event")
    @NotNull
    private Long eventId;
    @JsonProperty("requester")
    @NotNull
    private Long requesterId;
    private Status status;
}
