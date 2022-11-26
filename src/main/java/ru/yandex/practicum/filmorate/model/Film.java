package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private long rate = 0L;
    private Set<Genre> genres = new HashSet<>();
    @JsonIgnore
    private Set<Long> likeUserIds = new HashSet<>();

    public Film(long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
    public Map<String, Object> toMap() {
        Map<String, Object> mapFilm = new HashMap<>();
        mapFilm.put("film_name", name);
        mapFilm.put("description", description);
        mapFilm.put("releaseDate", releaseDate);
        mapFilm.put("duration", duration);
        mapFilm.put("rate", rate);
        mapFilm.put("mpa_id", mpa.getId());
        return mapFilm;
    }
}
