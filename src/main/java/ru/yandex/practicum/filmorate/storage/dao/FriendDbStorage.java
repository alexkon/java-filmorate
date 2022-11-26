package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

@Component
public class FriendDbStorage implements FriendStorage {
    private JdbcTemplate jdbcTemplate;

    public FriendDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(long userId, long friendId) {
        final String sql = "MERGE INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);

    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        final String sql = "DELETE FROM friends WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(sql, userId, friendId);

    }

    @Override
    public List<User> getFriendsById(long userId) {
        final String sql = "SELECT * FROM users, friends WHERE users.user_id = friends.friend_id AND friends.user_id=?";
        return jdbcTemplate.query(sql, UserDbStorage::makeUser, userId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        final String sql = "select * from USERS u, FRIENDS f, FRIENDS o "
                + "where u.user_id = f.friend_id "
                + "and u.user_id = o.friend_id "
                + "and f.user_id= ? and o.user_id=?";
        return jdbcTemplate.query(sql, UserDbStorage::makeUser, userId, otherId);
    }
}
