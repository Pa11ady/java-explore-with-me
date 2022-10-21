package ru.practicum.explorewithme.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;
import ru.practicum.explorewithme.user.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static List<UserDto> mapToUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(mapToUserDto(user));
        }

        return result;
    }

    public static User mapToUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setEmail( newUserRequest.getEmail());
        user.setName( newUserRequest.getName());
        user.setEmail( newUserRequest.getEmail());
        return user;
    }
}
