package com.tokenService.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.tokenService.dao.User;
import com.tokenService.dao.UserExistsException;
import com.tokenService.dao.UserNotFoundException;
import com.tokenService.dao.UserRepository;
import com.tokenService.model.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class UserController {

    private final UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    UserController(UserRepository repository) {
        this.repository = repository;
    }

    @JsonView(Views.Public.class)
    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }

    @JsonView(Views.Public.class)
    @PostMapping("/users")
    User newUser(@RequestBody User user) {
        if (repository.findByUsername(user.getUsername()) == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return repository.save(user);
        }
        throw new UserExistsException(user.getUsername() + " already exists");
    }

    @JsonView(Views.Public.class)
    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @JsonView(Views.Public.class)
    @PutMapping("/users/{id}")
    User updateUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id) //
                .map(user -> {
                    user.setUsername(newUser.getUsername());
                    user.setRole(newUser.getRole());
                    user.setPassword(passwordEncoder.encode(newUser.getPassword()));
                    return repository.save(user);
                }) //
                .orElseGet(() -> {
                    if (repository.findByUsername(newUser.getUsername()) != null)
                        throw new UserExistsException(newUser.getUsername() + " already exists");
                    newUser.setId(id);
                    newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
