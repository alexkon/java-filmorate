package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;

    public Film create(Film film) {
        if (filmStorage.getAll().contains(film)) {
            throw new ValidationException(String.format("Фильм {} - уже есть в фильмотеке", film));
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        final Long filmId = film.getId();
        if (filmStorage.getById(filmId) == null) {
            throw new NotFoundException(String.format("Фильм с id=%s не найден", filmId));
        }
        return filmStorage.update(film);
    }

    public Film getById(Long filmId) {
        final Film film = getFilmNotNull(filmId);
        return film;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film deleteById(Long filmId) {
        final Film film = getFilmNotNull(filmId);
        return filmStorage.deleteById(filmId);
    }

    public void addLike(long filmId, long userId){
        final Film film = getFilmNotNull(filmId);
        final User user = userStorage.getById(userId);

        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с id=%s не найден", userId));
        }

        filmStorage.addLike(film, user);
    }

    public void deleteLike(long filmId, long userId){
        final Film film = getFilmNotNull(filmId);
        final User user = userStorage.getById(userId);

        if (user == null) {
            throw new NotFoundException(String.format("Пользователь с id=%s не найден", userId));
        }

        filmStorage.deleteLike(film, user);
    }

    public List<Film> getPopular(String count) {
        List<Film> films = getAll();
        return films.stream()
                .sorted( (f0, f1) -> compare(f0, f1))
                .limit(Long.parseLong(count))
                .collect(Collectors.toList());
    }

    private int compare(Film film1, Film film2) {
        String f1 = String.valueOf(film1.getRate());
        String f2 = String.valueOf(film2.getRate());
        int result = f1.compareTo(f2);
        result = -1 * result;
        return result;
    }


    private Film getFilmNotNull(long filmId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с id=%s не найден", filmId));
        }
        return film;
    }
}
