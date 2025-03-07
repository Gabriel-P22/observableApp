package br.com.personal.observableApp.application.controller;

import br.com.personal.observableApp.application.dto.UserRequest;
import br.com.personal.observableApp.application.dto.UserResponse;
import br.com.personal.observableApp.domain.entity.User;
import br.com.personal.observableApp.domain.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<UserResponse> create(@RequestBody UserRequest body) {
        return ResponseEntity.ok(
                service.create(body).toResponse()
        );
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> getUserList() {
        return ResponseEntity.ok(
                service.getUserList()
                        .stream()
                        .map(User::toResponse)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getUserById(id).toResponse());
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest body
    ) {
        return ResponseEntity.ok(service.updateUserString(body, id).toResponse());
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> replaceUser(
            @PathVariable Long id,
            @RequestBody UserRequest body
    ) {
        return ResponseEntity.ok(service.replaceUser(body, id).toResponse());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @PathVariable Long id
    ) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
