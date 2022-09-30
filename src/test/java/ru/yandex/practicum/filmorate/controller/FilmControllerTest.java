package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    private FilmController filmController;

    @Test
    void createFilmFailName() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 12, 28));
        film.setDuration(100L);

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            filmController.createFilm(film);});
        assertEquals("название не может быть пустым", trow.getMessage());
    }

    @Test
    void createFilmFailReleaseDate() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        film.setDuration(100L);

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            filmController.createFilm(film);});
        assertEquals("дата релиза — раньше 28 декабря 1895 года", trow.getMessage());
    }

    @Test
    void createFilmFailDurationFor0() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 12, 28));
        film.setDuration(0L);

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            filmController.createFilm(film);});
        assertEquals("продолжительность фильма задана некорректно", trow.getMessage());
    }

    @Test
    void createFilmFailForNegativeDuration() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 12, 28));
        film.setDuration(-100L);

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            filmController.createFilm(film);});
        assertEquals("продолжительность фильма задана некорректно", trow.getMessage());
    }

    @Test
    void createFilmFailDescription() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. " +
                "Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги, " +
                "а именно 20 миллионов. о Куглов, который за время «своего отсутствия», стал кандидатом Коломбани.");
        film.setReleaseDate(LocalDate.of(2020, 12, 28));
        film.setDuration(100L);

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            filmController.createFilm(film);});
        assertEquals("описание фильма превышает 200 символов", trow.getMessage());
    }
}