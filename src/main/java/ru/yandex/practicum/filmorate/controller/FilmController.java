package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    @Autowired
    FilmService filmService;
    @Autowired
    UserController userController;

    @GetMapping()
    public List<Film> getAllFilms() {
        List<Film> films = filmService.getAll();
        log.info("Get-запрос: всего фильмов={} : {}", films.size(), films);
        return films;
    }

    @GetMapping("/{filmId}")
    public Film getFilmsById(@PathVariable long filmId) {
        Film film = filmService.getById(filmId);
        log.info("Get-запрос: с id={} - фильм {}", filmId, film);
        return film;
    }

        @GetMapping("/popular")
        public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", required = false) String count) {
            List<Film> films = filmService.getPopular(count);
            log.info("Get-запрос: первые {} популярных фильмов: {}", count, films);
            return films;
    }

    @PostMapping()
    public Film createFilm(@RequestBody Film film) {
        verification(film);
        filmService.create(film);
        log.info("Post-запрос: добавлен в фильмотеку фильм {}", film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        verification(film);
        filmService.update(film);
        log.info("Put-запрос: обновлен фильм {}", film);
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.addLike(filmId, userId);
        log.info("Put-запрос:  новый лайк у фильма {}, всего лайков={} ",
                filmService.getById(filmId), filmService.getById(filmId).getRate());
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable long filmId, @PathVariable long userId) {
        filmService.deleteLike(filmId, userId);
        log.info("Put-запрос:  дизлайк у фильма {} , осталось лайков={} ",
                filmService.getById(filmId), filmService.getById(filmId).getRate());
    }

    private void verification(Film film) {
        int descriptionMaxLength = 200;
        LocalDate releaseDateBefore = LocalDate.of(1895, 12, 28);

        if (!StringUtils.hasText(film.getName())) {
            log.error("Запрос не выполнен: отсутствует название фильма.");
            throw new ValidationException("название не может быть пустым");
        }

        if (film.getReleaseDate().isBefore(releaseDateBefore) ) {
            log.error("Запрос не выполнен: дата релиза={} меньше {}",
                    film.getReleaseDate(), releaseDateBefore);
            throw new ValidationException("дата релиза — раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.error("Запрос не выполнен: продолжительность фильма={} - должна быть больше 0",
                    film.getDuration());
            throw new ValidationException("продолжительность фильма задана некорректно");
        }

        if (film.getDescription().length() > descriptionMaxLength) {
            log.info("Запрос не выполнен: длина описания фильма={} символов - должна быть не более {}",
                    film.getDescription().length(), descriptionMaxLength);
            throw new ValidationException("описание фильма превышает " + descriptionMaxLength + " символов");
        }
    }
}
