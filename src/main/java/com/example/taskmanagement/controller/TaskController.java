package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.model.Tag;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.service.CommentService;
import com.example.taskmanagement.service.TagService;
import com.example.taskmanagement.service.TaskService;
import com.example.taskmanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final CommentService commentService;
    private final TagService tagService;

    public TaskController(TaskService taskService, UserService userService,
                          CommentService commentService, TagService tagService) {
        this.taskService = taskService;
        this.userService = userService;
        this.commentService = commentService;
        this.tagService = tagService;
    }

    /* ---------------- CRUD ---------------- */

    @GetMapping
    public List<Task> getAll() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public Task getById(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    public Task create(@RequestBody Task task) {
        return taskService.create(task);
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @RequestBody Task task) {
        return taskService.update(id, task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }

    /* ---------------- Assign User to Task ---------------- */

    @PostMapping("/{taskId}/users/{userId}")
    public Task assignUser(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskService.getById(taskId);
        User user = userService.getById(userId);

        if (task == null || user == null) {
            throw new RuntimeException("Task or User not found");
        }

        if (task.getUsers() == null) {
            task.setUsers(new ArrayList<>());
        }

        if (!task.getUsers().contains(user)) {
            task.getUsers().add(user);
        }

        return taskService.update(taskId, task);
    }

    /* ---------------- Add Comment to Task ---------------- */

    @PostMapping("/{taskId}/comments")
    public Comment addComment(@PathVariable Long taskId,
                              @RequestParam Long userId,
                              @RequestParam String text) {
        return commentService.addCommentToTask(taskId, userId, text);
    }

    /* ---------------- Add Tag to Task ---------------- */

    @PostMapping("/{taskId}/tags/{tagId}")
    public Task addTagToTask(@PathVariable Long taskId, @PathVariable Long tagId) {
        Task task = taskService.getById(taskId);
        Tag tag = tagService.getById(tagId);

        if (task == null || tag == null) {
            throw new RuntimeException("Task or Tag not found");
        }

        if (task.getTags() == null) {
            task.setTags(new ArrayList<>());
        }

        if (!task.getTags().contains(tag)) {
            task.getTags().add(tag);
        }

        return taskService.update(taskId, task);
    }
}