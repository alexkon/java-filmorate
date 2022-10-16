package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;

    public User create(User user) {
        if (userStorage.getAll().contains(user)) {
            throw new ValidationException(String.format("Пользователь {} - уже зарегистрирован", user));
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        getUserNotNull(user.getId());
        return userStorage.update(user);
    }

    public User getById(long userId) {
        final User user = getUserNotNull(userId);
        return user;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public List<User> getFriendsByUserId(long userId) {
        final User user = getUserNotNull(userId);
        return user.getFriendIds().stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long userId, Long otherId) {
        final User user = getUserNotNull(userId);
        final User otherUser = getUserNotNull(otherId);
        Set<Long> commonFriends = new HashSet<>(user.getFriendIds());
        commonFriends.retainAll(otherUser.getFriendIds());
        return commonFriends.stream()
                .map(this::getById)
                .collect(Collectors.toList());
    }

    public User deleteById(long userId) {
        final User user = getUserNotNull(userId);
        return userStorage.deleteById(userId);
    }

    public void addFriend(long userId, long friendId) {
        final User user = getUserNotNull(userId);
        final User friend = getUserNotNull(friendId);
        userStorage.addFriend(user, friend);
    }

    public void deleteFriend(long userId, long friendId) {
        final User user = getUserNotNull(userId);
        final User friend = getUserNotNull(friendId);
        userStorage.deleteFriend(user, friend);
    }

    private User getUserNotNull(long userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с id=%s не найден", userId));
        }
        return user;
    }
}
