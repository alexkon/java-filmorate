package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {
    public Film create(Film film);
    public Film update(Film film);
    public Film getById(long filmId);
    public List<Film> getAll();
    public Film deleteById(long filmId);
    public void addLike(Film film, User user);
    public void deleteLike(Film film, User user);
}
