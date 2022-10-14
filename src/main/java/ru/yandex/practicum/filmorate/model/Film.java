package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    @JsonIgnore
    private Set<Long> likeUserIds = new HashSet<>();
    @JsonIgnore
    private long rate = 0L;
}
