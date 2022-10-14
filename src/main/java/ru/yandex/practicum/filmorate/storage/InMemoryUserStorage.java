package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Long generatorId = 0L;
    private Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        generatorId++;
        user.setId(generatorId);
        users.put(generatorId, user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getById(long userId) {
        return users.get(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User deleteById(long userId) {
        return users.remove(userId);
    }

    @Override
    public void addFriend(User user, User friend) {
        user.getFriendIds().add(friend.getId());
        friend.getFriendIds().add(user.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        user.getFriendIds().remove(friend.getId());
        friend.getFriendIds().remove(user.getId());
    }


}
