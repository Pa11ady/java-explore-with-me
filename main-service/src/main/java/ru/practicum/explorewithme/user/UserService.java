package ru.practicum.explorewithme.user;

import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findUsers(Long[] ids, Integer from, Integer size);

    UserDto create(NewUserRequest newUserRequest);

    void delete(Long userId);
}
