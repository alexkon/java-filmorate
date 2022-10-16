package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User getById(long userId);

    List<User> getAll();

    User deleteById(long userId);

    void addFriend(User user, User film);

    void deleteFriend(User user, User film);
}
