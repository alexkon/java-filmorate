package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
//@RequestMapping("/films")
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        List<Film> films = filmService.getAll();
        log.info("Get-запрос: всего фильмов={} : {}", films.size(), films);
        return films;
    }

    @GetMapping("/films/{filmId}")
    public Film getFilmsById(@PathVariable long filmId) {
        Film film = filmService.getById(filmId);
        log.info("Get-запрос: с id={} - фильм {}", filmId, film);
        return film;
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) String count) {
        List<Film> films = filmService.getPopular(count);
        log.info("Get-запрос: первые {} популярных фильмов: {}", count, films);
        return films;
    }

    @GetMapping("/genres/{genreId}")
    public Genre getGenreById(@PathVariable int genreId) {
        Genre genre = filmService.getGenreById(genreId);
        log.info("Get-запрос: жанр с id = {} : {}", genreId, genre);
        return genre;
    }

    @GetMapping("/genres")
    public List<Genre> getAllGenres() {
        List<Genre> genres = filmService.getAllGenres();
        log.info("Get-запрос: всего жанров = {} : {}", genres.size(), genres);
        return genres;
    }
    @GetMapping("/mpa/{mpaId}")
    public Mpa getMpaById(@PathVariable int mpaId) {
        Mpa mpa = filmService.getMpaById(mpaId);
        log.info("Get-запрос: MPA с id = {} : {}", mpaId, mpa);
        return mpa;
    }

    @GetMapping("/mpa")
    public List<Mpa> getAllMpa() {
        List<Mpa> mpas = filmService.getAllMpa();
        log.info("Get-запрос: всего MPA = {} : {}", mpas.size(), mpas);
        return mpas;
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        verification(film);
        filmService.create(film);
        log.info("Post-запрос: добавлен в фильмотеку фильм {}", film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        verification(film);
        filmService.update(film);
        log.info("Put-запрос: обновлен фильм {}", film);
        return film;
    }

    @PutMapping("/films/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.addLike(filmId, userId);
        log.info("Put-запрос:  новый лайк у фильма с id={}", filmId);
    }

    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.deleteLike(filmId, userId);
        log.info("Delete-запрос:  дизлайк у фильма с id={}", filmId);
    }

    private void verification(Film film) {
        int descriptionMaxLength = 200;
        LocalDate releaseDateBefore = LocalDate.of(1895, 12, 28);

        if (!StringUtils.hasText(film.getName())) {
//            log.error("Запрос не выполнен: отсутствует название фильма.");
            throw new ValidationException("название не может быть пустым");
        }

        if (film.getReleaseDate().isBefore(releaseDateBefore)) {
//            log.error("Запрос не выполнен: дата релиза={} меньше {}",
//                    film.getReleaseDate(), releaseDateBefore);
            throw new ValidationException("дата релиза — раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
//            log.error("Запрос не выполнен: продолжительность фильма={} - должна быть больше 0",
//                    film.getDuration());
            throw new ValidationException("продолжительность фильма задана некорректно");
        }

        if (film.getDescription().length() > descriptionMaxLength) {
//            log.info("Запрос не выполнен: длина описания фильма={} символов - должна быть не более {}",
//                    film.getDescription().length(), descriptionMaxLength);
            throw new ValidationException("описание фильма превышает " + descriptionMaxLength + " символов");
        }
    }
}
