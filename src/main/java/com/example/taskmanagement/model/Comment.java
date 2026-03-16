package com.example.taskmanagement.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "task_id")
    @JsonIgnoreProperties({"comments", "users", "project"})
    private Task task;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({"tasks", "users"})
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"projects"})
    private User user;

    public Comment() {}

    public Comment(Long id, String text, Task task, Project project, User user) {
        this.id = id;
        this.text = text;
        this.task = task;
        this.project = project;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}