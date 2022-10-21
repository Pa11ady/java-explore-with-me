package ru.practicum.explorewithme.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
