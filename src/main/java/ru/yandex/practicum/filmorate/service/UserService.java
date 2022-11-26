package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    //@Qualifier("inMemoryUserStorage")
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private FriendStorage friendStorage;

    public User create(User user) {
        if (userStorage.getAll().contains(user)) {
            throw new ValidationException(String.format("Пользователь {} - уже зарегистрирован", user));
        }
        User newUser = userStorage.create(user);
        return newUser;
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
        getUserNotNull(userId);
        return friendStorage.getFriendsById(userId);
    }

    public List<User> getCommonFriends(long userId, Long otherId) {
        getUserNotNull(userId);
        getUserNotNull(otherId);
        return friendStorage.getCommonFriends(userId, otherId);
    }

    public User deleteById(long userId) {
        final User user = getUserNotNull(userId);
        return userStorage.deleteById(userId);
    }

    public void addFriend(long userId, long friendId) {
        getUserNotNull(userId);
        getUserNotNull(friendId);
        friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        getUserNotNull(userId);
        getUserNotNull(friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    private User getUserNotNull(long userId) {
        User user = userStorage.getById(userId);
        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с id=%s не найден", userId));
        }
        return user;
    }
}
