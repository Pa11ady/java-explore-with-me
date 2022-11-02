package ru.practicum.explorewithme.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.user.dto.NewUserRequest;
import ru.practicum.explorewithme.user.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> findUsers(@RequestParam Long[] ids,
                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                   @Positive @RequestParam(defaultValue = "10") Integer size,
                                   HttpServletRequest request) {
        log.info("{}: {}; получение списка пользователей",
                request.getRemoteAddr(),
                request.getRequestURI());
        return userService.findUsers(ids, from, size);
    }

    @PostMapping()
    public UserDto create(@Valid @RequestBody NewUserRequest newUserRequest, HttpServletRequest request) {
        log.info("{}: {}; добавление пользователя {}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                newUserRequest.toString());
        return userService.create(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId, HttpServletRequest request) {
        log.info("{}: {};  удаление пользователя с ID={}",
                request.getRemoteAddr(),
                request.getRequestURI(),
                userId);
        userService.delete(userId);
    }
}
