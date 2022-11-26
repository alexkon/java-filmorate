package ru.yandex.practicum.filmorate.storage.inmemory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Long generatorId = 0L;

    private Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        generatorId++;
        film.setId(generatorId);
        films.put(generatorId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(long filmId) {
        return films.get(filmId);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film deleteById(long filmId) {
        return films.remove(filmId);
    }

    @Override
    public void addLike(Film film, User user) {
        film.getLikeUserIds().add(user.getId());
        film.setRate(film.getLikeUserIds().size());
    }

    @Override
    public void deleteLike(Film film, User user) {
        film.getLikeUserIds().remove(user.getId());
        film.setRate(film.getLikeUserIds().size());
    }
}
