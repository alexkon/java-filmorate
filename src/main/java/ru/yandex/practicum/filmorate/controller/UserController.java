package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {
        List<User> users = userService.getAll();
        log.info("Get-запрос: всего пользователей={} : {}", users.size(), users);
        return users;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable long userId) {
        User user = userService.getById(userId);
        log.info("Get-запрос: id={} - пользователь: {}", userId, user);
        return user;
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsUserId(@PathVariable long userId) {
        List<User> friends = userService.getFriendsByUserId(userId);
        log.info("Get-запрос: всего друзей={} : {}", friends.size(), friends);
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        List<User> friendsCommon = userService.getCommonFriends(userId, otherId);
        log.info("Get-запрос: общих друзей={} : {}",
                friendsCommon.size(), friendsCommon);
        return friendsCommon;
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        verification(user);
        userService.create(user);
        log.info("Post-запрос: добавлен новый пользователь: {}", user);
        return user;
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        verification(user);
        userService.update(user);
        log.info("Put-запрос: обновлен пользователь: {}", user);
        return user;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.addFriend(userId, friendId);
        log.info("Put-запрос:  у пользователя с id={} новый друг c id={}", userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable long userId, @PathVariable long friendId) {
        userService.deleteFriend(userId, friendId);
        log.info("Delete-запрос:  у пользователя с id={} удален друг c id={}", userId, friendId);
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
