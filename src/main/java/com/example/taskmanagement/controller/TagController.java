package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Tag;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.service.TagService;
import com.example.taskmanagement.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;
    private final TaskService taskService;

    public TagController(TagService tagService, TaskService taskService) {
        this.tagService = tagService;
        this.taskService = taskService;
    }

    /* ---------------- CRUD ---------------- */

    @GetMapping
    public List<Tag> getAll() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public Tag getById(@PathVariable Long id) {
        return tagService.getById(id);
    }

    @PostMapping
    public Tag create(@RequestBody Tag tag) {
        return tagService.create(tag);
    }

    @PutMapping("/{id}")
    public Tag update(@PathVariable Long id, @RequestBody Tag tag) {
        return tagService.update(id, tag);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }

    /* ---------------- ADD TAG TO TASK ---------------- */

    @PostMapping("/{tagId}/tasks/{taskId}")
    public Tag addTagToTask(@PathVariable Long tagId, @PathVariable Long taskId) {
        Tag tag = tagService.getById(tagId);
        Task task = taskService.getById(taskId);

        if (tag != null && task != null) {
            task.getTags().add(tag);
            taskService.create(task); // или update(taskId, task)
        }

        return tag;
    }
}