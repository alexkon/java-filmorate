package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage implements GenreStorage {
    private final Logger log = LoggerFactory.getLogger(GenreDbStorage.class);
    private JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getById(int genreId) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs, rowNum), genreId);

        if(genres.size() != 1) {
            throw new NotFoundException(String.format("Жанр с id=%s не найден", genreId));
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs, rowNum));
    }

    public void setGenresForFilms(List<Film> films) {
        String forInSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        final String sql = "SELECT * FROM film_genres AS fg, genres AS g " +
                "WHERE fg.genre_id = g.genre_id and fg.film_id IN (" + forInSql + ")";
        jdbcTemplate.query(sql, (rs) -> {
            final Film film = filmById.get(rs.getLong("film_id"));
            film.addGenre(makeGenre(rs, 0));
        }, films.stream().map(Film::getId).toArray());
    }

    static Genre makeGenre(ResultSet rs, int rowNum) throws SQLException{   //24:41
        return new Genre(
                rs.getInt("genre_id"),
                rs.getString("genre_name"));
    }
}
