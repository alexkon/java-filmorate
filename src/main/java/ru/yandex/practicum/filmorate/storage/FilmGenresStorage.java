package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmGenresStorage {
    List<Genre> getGenresByFilmId(Long film_id);

    void addGenre(long film_id,long genre_id);

    void deleteGenre(long film_id,long genre_id);

}
