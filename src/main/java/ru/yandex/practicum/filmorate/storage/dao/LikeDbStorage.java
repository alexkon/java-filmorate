package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
public class LikeDbStorage implements LikeStorage {
    private JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(long filmId, long userId) {
        String sql = "MERGE INTO LIKES (film_id, user_id) values ( ?, ? )";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public List<Film> getPopular(String count) {
        final String sgl = "SELECT * FROM films AS f, mpa AS m WHERE f.mpa_id = m.mpa_id ORDER BY rate DESC LIMIT ?";
        return jdbcTemplate.query(sgl, FilmDbStorage::makeFilm, count);
    }

    private void updateRate(long filmId) {
        long countUserId = jdbcTemplate.queryForObject("SELECT COUNT(user_id) FROM likes WHERE film_id =?",
                Long.class, filmId);
        String sql = "UPDATE films SET rate =? WHERE film_id =?";
        jdbcTemplate.update(sql, countUserId, filmId);
    }

}
