package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private int id = 0;
    private Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        if (films.containsValue(film)) {
            log.error(String.format("Post-запрос не выполнен: %s - уже есть в фильмотеке", film));
            throw new ValidationException("Post-запрос не выполнен: такой фильм уже есть в фильмотеке");
        }
        verification(film);
        id++;
        film.setId(id);
        films.put(id, film);
        log.info(String.format("Post-запрос выполнен: добавлен в фильмотеку %s", film));
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            log.error(String.format("Put-запрос не выполнен: фильма с id=%s - нет в фильмотеке", film.getId()));
            throw new ValidationException("Put-запрос не выполнен, неправильный id");
        }
        verification(film);
        films.put(film.getId(), film);
        log.info(String.format("Put-запрос выполнен: обновлен %s", film));
        return film;
    }

    private void verification(Film film) {
        int descriptionMaxLength = 200;
        LocalDate releaseDateBefore = LocalDate.of(1895, 12, 28);

        if (!StringUtils.hasText(film.getName())) {
            log.error("Запрос не выполнен: отсутствует название фильма.");
            throw new ValidationException("название не может быть пустым");
        }

        if (film.getReleaseDate().isBefore(releaseDateBefore) ) {
            log.error(String.format("Запрос не выполнен: дата релиза=%s меньше %s",
                    film.getReleaseDate(), releaseDateBefore));
            throw new ValidationException("дата релиза — раньше 28 декабря 1895 года");
        }

        if (film.getDuration() <= 0) {
            log.error(String.format("Запрос не выполнен: продолжительность фильма=%s - должна быть больше 0",
                    film.getDuration()));
            throw new ValidationException("продолжительность фильма задана некорректно");
        }

        if (film.getDescription().length() > descriptionMaxLength) {
            log.info(String.format("Запрос не выполнен: длина описания фильма=%s символов - должна быть не более %s",
                    film.getDescription().length(), descriptionMaxLength));
            throw new ValidationException("описание фильма превышает " + descriptionMaxLength + " символов");
        }
    }
}
