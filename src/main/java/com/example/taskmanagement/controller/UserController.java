package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* ---------------- CRUD ---------------- */

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.update(id, user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    /* ---------------- GET PROJECTS OF USER ---------------- */

    @GetMapping("/{userId}/projects")
    public List<Project> getUserProjects(@PathVariable Long userId) {
        User user = userService.getById(userId);
        return user != null ? user.getProjects() : null;
    }
}