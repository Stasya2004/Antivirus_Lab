package com.example.taskmanagement.service;

import com.example.taskmanagement.model.User;
import com.example.taskmanagement.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------- GET -----------------
    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }

    // ----------------- CREATE -----------------
    public User create(User user) {
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return repository.save(user);
    }

    // ----------------- UPDATE (админ) -----------------
    public User updateByAdmin(Long id, User request) {
        User user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (request.getName() != null) user.setName(request.getName());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        // Поля блокировки можно тоже обновлять при необходимости
        if (request.getIsAccountExpired() != null) user.setIsAccountExpired(request.getIsAccountExpired());
        if (request.getIsAccountLocked() != null) user.setIsAccountLocked(request.getIsAccountLocked());
        if (request.getIsCredentialsExpired() != null) user.setIsCredentialsExpired(request.getIsCredentialsExpired());
        if (request.getIsDisabled() != null) user.setIsDisabled(request.getIsDisabled());

        return repository.save(user);
    }

    // ----------------- DELETE -----------------
    public void deleteByAdmin(Long id, Collection<? extends GrantedAuthority> roles) {
        boolean isAdmin = roles.stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new RuntimeException("Access denied: only admin can delete users");
        }
        repository.deleteById(id);
    }
}