package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private int id = 0;
    private Map<Integer, User> users = new HashMap<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        if (users.containsValue(user)) {
            log.error("Post-запрос не выполнен: {} - уже зарегистрирован", user);
            throw new ValidationException("Post-запрос не выполнен, такой пользователь уже зарегистрирован");
        }
        verification(user);
        id++;
        user.setId(id);
        users.put(id, user);
        log.info("Post-запрос выполнен: добавлен новый {}", user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Put-запрос не выполнен: пользователь с id={} - не зарегистрирован", user.getId());
            throw new ValidationException("Put-запрос не выполнен, неправильный id");
        }
        verification(user);
        users.put(user.getId(), user);
        log.info("Put-запрос выполнен: обновлен {}", user);
        return user;
    }

    private void verification(User user) {

        if (!StringUtils.hasLength(user.getEmail()) || !user.getEmail().contains("@")) {
            log.error("Запрос не выполнен: email={} - задан некорректно", user.getEmail());
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }

        if (!StringUtils.hasText(user.getLogin())) {
            log.error("Запрос не выполнен: логин либо пустой, либо содержит только пробелы");
            throw new ValidationException("логин не может быть пустым и содержать только пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Запрос не выполнен: дата рождения={} - больше текущей даты", user.getBirthday());
            throw new ValidationException("дата рождения не может быть в будущем");
        }

        if (!StringUtils.hasText(user.getName())) {
            log.info("Запрос выполнен с ограничением: имя пользователя отсутствует - заменено на логин={}",
                    user.getLogin());
            user.setName(user.getLogin());
        }
    }
}
