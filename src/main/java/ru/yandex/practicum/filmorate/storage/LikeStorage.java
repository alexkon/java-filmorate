package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeStorage {
    void addLike(long filmId,long userId);

    void deleteLike(long filmId,long userId);

    List<Film> getPopular(String count);
}
