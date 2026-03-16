
// ProjectService.java
package com.example.taskmanagement.service;

import com.example.taskmanagement.exception.DuplicateAssignmentException;
import com.example.taskmanagement.model.Project;
import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository repository;
    private final UserService userService;

    public ProjectService(ProjectRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<Project> getAll() { return repository.findAll(); }

    public Project getById(Long id) { return repository.findById(id).orElse(null); }

    public Project create(Project project) { return repository.save(project); }

    public Project update(Long id, Project updatedProject) {
        Optional<Project> projOpt = repository.findById(id);
        if(projOpt.isPresent()) {
            Project project = projOpt.get();
            project.setName(updatedProject.getName());
            project.setDescription(updatedProject.getDescription());
            project.setUsers(updatedProject.getUsers());
            return repository.save(project);
        }
        return null;
    }

    @Transactional
    public Project assignUser(Long projectId, Long userId) {
        Project project = getById(projectId);
        if (project == null) {
            throw new EntityNotFoundException("Project not found with id: " + projectId);
        }

        User user = userService.getById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }

        // Инициализируем список, если он null
        if (project.getUsers() == null) {
            project.setUsers(new ArrayList<>());
        }

        // Проверяем, есть ли уже такой пользователь в проекте
        if (project.getUsers().contains(user)) {
            throw new DuplicateAssignmentException("User already assigned to this project");
        }

        project.getUsers().add(user);
        return repository.save(project);
    }

    public void delete(Long id) { repository.deleteById(id); }
}
