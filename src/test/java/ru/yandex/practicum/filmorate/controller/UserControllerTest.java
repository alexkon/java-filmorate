package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTest {
    @Autowired
    UserController userController;

    @Test
    void createUserFailEmail() {
        User user = new User();
        user.setEmail("user-yandex.ru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(1997, 11, 29));

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.createUser(user);});
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", trow.getMessage());
    }

    @Test
    void createUserEmptyEmail() {
        User user = new User();
        user.setEmail("user-yandex.ru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(1997, 11, 29));

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.createUser(user);});
        assertEquals("электронная почта не может быть пустой и должна содержать символ @", trow.getMessage());
    }

    @Test
    void createUserEmptyLogin() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("");
        user.setName("Name");
        user.setBirthday(LocalDate.of(1997, 11, 29));

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.createUser(user);});
        assertEquals("логин не может быть пустым и содержать только пробелы", trow.getMessage());
    }

    @Test
    void createUserOnlySpaceLogin() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("     ");
        user.setName("Name");
        user.setBirthday(LocalDate.of(1997, 11, 29));

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.createUser(user);});
        assertEquals("логин не может быть пустым и содержать только пробелы", trow.getMessage());
    }

    @Test
    void createUserFailBirthday() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.now().plusDays(1));

        RuntimeException trow = assertThrows(RuntimeException.class, () -> {
            userController.createUser(user);});
        assertEquals("дата рождения не может быть в будущем", trow.getMessage());
    }

    @Test
    void createUserFailName() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("Login");
        user.setName("");
        user.setBirthday(LocalDate.of(1997, 11, 29));

        userController.createUser(user);

        List<User> allUsers = userController.getUsers();
        assertEquals(user.getLogin(), allUsers.get(0).getName(), "имя пользователя не заменено на логин");
    }
}