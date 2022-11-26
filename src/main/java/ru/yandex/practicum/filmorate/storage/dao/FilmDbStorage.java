package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Primary
@Component
public class FilmDbStorage implements FilmStorage {
    @Autowired
    MpaDbStorage mpaDbStorage;
    private JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                        .usingGeneratedKeyColumns("film_id");
        long filmId = simpleJdbcInsert.executeAndReturnKey(film.toMap()).longValue();
        film.setId(filmId);

//        updateGenres(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET film_name= ?, description = ?, releaseDate = ?, " +
                "duration = ?, rate = ?, mpa_id = ? WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRate(), film.getMpa().getId(), film.getId());
        updateGenres(film);
        return film;
    }

    @Override
    public Film getById(long filmId) {
        String sql = "SELECT * FROM films AS f, mpa AS m WHERE f.mpa_id = m.mpa_id AND f.film_id = ?";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs, rowNum), filmId);

        if(films.size() != 1) {
            throw new NotFoundException(String.format("Фильм с id=%s не найден", filmId));
        }
        return films.get(0);
    }

    @Override
    public List<Film> getAll() {
//        final String sql = "SELECT * FROM films AS f, mpa AS m WHERE f.film_id = m.mpa_id";
        final String sql = "SELECT * FROM films AS f JOIN mpa AS m ON f.mpa_id = m.mpa_id";
        final List<Film> films = jdbcTemplate.query(sql, FilmDbStorage::makeFilm);
        return films;
    }

    @Override
    public Film deleteById(long filmId) {
        String sql = "DELETE FROM films WHERE film_id= ?";  // удаление film_Genres, Likes ???
        jdbcTemplate.update(sql, filmId);
        return getById(filmId);
    }

    @Override
    public void addLike(Film film, User user) { // ----------------------------------?

    }

    @Override
    public void deleteLike(Film film, User user) { // ----------------------------------?

    }

    private Set<Genre> getGenreByIdFilm(long filmId){
        final String sql = "SELECT * FROM film_genres AS fg " +
                "JOIN genres AS g ON fg.genre_id = g.genre_id WHERE fg.filmId = filmId";
        final List<Genre> genres = jdbcTemplate.query(sql, GenreDbStorage::makeGenre);
        return new HashSet<>(genres);
    }
    private void updateGenres(Film film) {     // обновить жанры в ДВ updateGenreDb
        final long filmId = film.getId();
        //final Set<Integer> genresId = film.getGenreId();
        ArrayList<Genre> genres = new ArrayList<>(film.getGenres());

        String sgl = "DELETE FROM film_genres WHERE film_id=?";
        jdbcTemplate.update(sgl, filmId);

        if (genres == null || genres.isEmpty()) {
            return;
        }

        sgl = "INSERT INTO film_genres (film_id, genre_id) values (?, ?)";
        jdbcTemplate.batchUpdate(sgl, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

    }

    static Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
            rs.getLong("film_id"),
            rs.getString("film_name"),
            rs.getString("description"),
            rs.getDate("releaseDate").toLocalDate(),
            rs.getInt("duration"),
            new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name"))
            );
    }
}
