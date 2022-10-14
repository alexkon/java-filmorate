package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public User create(User user);
    public User update(User user);
    public User getById(long userId);
    public List<User> getAll();
    public User deleteById(long userId);
    public void addFriend(User user, User film);
    public void deleteFriend(User user, User film);
}
