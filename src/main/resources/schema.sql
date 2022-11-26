DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS FILMS CASCADE;
DROP TABLE IF EXISTS GENRES CASCADE;
DROP TABLE IF EXISTS MPA CASCADE;
DROP TABLE IF EXISTS FILM_GENRES CASCADE;
DROP TABLE IF EXISTS LIKES CASCADE;
DROP TABLE IF EXISTS FRIENDS CASCADE;

create table if not exists MPA
(
    mpa_id   int primary key auto_increment,
    mpa_name varchar(50) not null
);

create table if not exists GENRES
(
    genre_id         int primary key auto_increment,
    genre_name varchar(50) not null
);

create table if not exists FILMS
(
    film_id     bigint primary key auto_increment,
    film_name   varchar(255) not null,
    description varchar(200) not null,
    releaseDate date         not null,
    duration    int,
    rate        int,
    mpa_id      int references MPA (mpa_id)
);

create table if not exists USERS
(
    user_id   bigint primary key auto_increment,
    email     varchar(100) not null,
    login     varchar(100) not null,
    user_name varchar(255),
    birthday  timestamp,
    UNIQUE (email),
    UNIQUE (login)
);

create table if not exists FILM_GENRES
(
    film_id  int references FILMS (film_id),
    genre_id int references GENRES (genre_id),
    PRIMARY KEY (film_id, genre_id)
);

create table if not exists LIKES
(
    film_id bigint references FILMS (film_id),
    user_id bigint references USERS (user_id),
    PRIMARY KEY (film_id, user_id)
);

create table if not exists FRIENDS
(
    user_id   bigint references USERS (user_id),
    friend_id bigint references USERS (user_id),
    PRIMARY KEY (user_id, friend_id)
);