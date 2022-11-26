package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    @Autowired
    private FilmStorage filmStorage;    // 15:51
    //@Qualifier("inMemoryUserStorage")
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private LikeStorage likeStorage;
    @Autowired
    private GenreStorage genreStorage;
    @Autowired
    private MpaStorage mpaStorage;


    public Film create(Film film) {
        if (filmStorage.getAll().contains(film)) {
            throw new ValidationException(String.format("Фильм {} - уже есть в фильмотеке", film));
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        System.out.println(film);
        getFilmNotNull(film.getId());
        System.out.println(film);
        return filmStorage.update(film);
    }

    public Film getById(Long filmId) {
        final Film film = getFilmNotNull(filmId);
        return film;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Genre getGenreById(int genreId) {
        return genreStorage.getById(genreId);
    }
    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public Mpa getMpaById(int mpaId) {
        return mpaStorage.getById(mpaId);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }

    public Film deleteById(Long filmId) {
        final Film film = getFilmNotNull(filmId);
        return filmStorage.deleteById(filmId);
    }

    public void addLike(long filmId, long userId) {
        final Film film = getFilmNotNull(filmId);
        final User user = userStorage.getById(userId);

//        if (user == null) {
//            throw new NotFoundException(String.format("Пользователь с id=%s не найден", userId));
//        }

        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        final Film film = getFilmNotNull(filmId);
        final User user = userStorage.getById(userId);

//        if (user == null) {
//            throw new NotFoundException(String.format("Пользователь с id=%s не найден", userId));
//        }

        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopular(String count) {
        return likeStorage.getPopular(count);
    }

    private Film getFilmNotNull(long filmId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            throw new NotFoundException(String.format("Фильм с id=%s не найден", filmId));
        }
        return film;
    }
}
