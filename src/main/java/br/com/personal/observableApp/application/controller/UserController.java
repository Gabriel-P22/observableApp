package br.com.personal.observableApp.application.controller;

import br.com.personal.observableApp.application.dto.UserRequest;
import br.com.personal.observableApp.application.dto.UserResponse;
import br.com.personal.observableApp.domain.service.UserService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<UserResponse> create(@RequestBody UserRequest body) {
        return ResponseEntity.ok(service.create(body));
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> getUserList() {
        return ResponseEntity.ok(service.getUserList());
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok(service.getUserById(id));
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @RequestBody UserRequest body
    ) {
        var user = service.updateUser(body, id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> replaceUser(
            @PathVariable String id,
            @RequestBody UserRequest body
    ) {
        var user = service.replaceUser(body, id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @PathVariable String id
    ) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
