package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film getById(long filmId);

    List<Film> getAll();

    Film deleteById(long filmId);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);
}
