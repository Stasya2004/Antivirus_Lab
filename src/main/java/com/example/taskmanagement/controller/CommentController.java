package com.example.taskmanagement.controller;

import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /* ---------------- GET ALL ---------------- */
    @GetMapping
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    /* ---------------- GET BY ID ---------------- */
    @GetMapping("/{id}")
    public Comment getById(@PathVariable Long id) {
        return commentService.getById(id);
    }

    /* ---------------- CREATE ---------------- */
    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        return commentService.create(comment);
    }

    /* ---------------- UPDATE ---------------- */
    @PutMapping("/{id}")
    public Comment update(@PathVariable Long id, @RequestBody Comment comment) {
        return commentService.update(id, comment);
    }

    /* ---------------- DELETE ---------------- */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        commentService.delete(id);
    }
}