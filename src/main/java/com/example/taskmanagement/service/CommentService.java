package com.example.taskmanagement.service;

import com.example.taskmanagement.model.Comment;
import com.example.taskmanagement.model.Task;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TaskService taskService;
    private final UserService userService;

    // Обновлённый конструктор с новыми зависимостями
    public CommentService(CommentRepository commentRepository,
                          TaskService taskService,
                          UserService userService) {
        this.commentRepository = commentRepository;
        this.taskService = taskService;
        this.userService = userService;
    }

    // ---------- CRUD методы ----------
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public Comment getById(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment create(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment update(Long id, Comment updated) {
        Optional<Comment> opt = commentRepository.findById(id);
        if (opt.isPresent()) {
            Comment comment = opt.get();
            comment.setText(updated.getText());
            comment.setTask(updated.getTask());
            comment.setProject(updated.getProject());
            comment.setUser(updated.getUser());
            return commentRepository.save(comment);
        }
        return null;
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    // ---------- Новая бизнес-операция ----------
    @Transactional
    public Comment addCommentToTask(Long taskId, Long userId, String text) {
        // Проверяем существование задачи
        Task task = taskService.getById(taskId);
        if (task == null) {
            throw new EntityNotFoundException("Task not found with id: " + taskId);
        }

        // Проверяем существование пользователя
        User user = userService.getById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        // Создаём новый комментарий
        Comment comment = new Comment();
        comment.setText(text);
        comment.setTask(task);
        comment.setUser(user);
        comment.setProject(task.getProject()); // привязываем к проекту задачи (необязательно)

        return commentRepository.save(comment);
    }
}