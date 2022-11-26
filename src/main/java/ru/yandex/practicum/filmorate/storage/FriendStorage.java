package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {
    void addFriend(long user_id,long friendId);

    void deleteFriend(long user_id,long friendId);

    List<User> getFriendsById(long userId);

    List<User> getCommonFriends(long userId, long friendId);
}
