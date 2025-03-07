package br.com.personal.observableApp.application.controller;

import br.com.personal.observableApp.application.dto.UserRequest;
import br.com.personal.observableApp.application.dto.UserResponse;
import br.com.personal.observableApp.domain.entity.User;
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

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    Counter userCreateWithSuccess;
    Counter userUpdatedWithSuccess;
    Counter userDeletedWithSuccess;
    Counter userFindWithSuccess;
    Counter userErrors;

    public UserController(UserService service, MeterRegistry registry) {
        this.service = service;

        this.userCreateWithSuccess = Counter.builder("USER_CREATED")
                .description("User created!")
                .register(registry);

        this.userUpdatedWithSuccess = Counter.builder("USER_UPDATED")
                .description("User updated!")
                .register(registry);

        this.userDeletedWithSuccess = Counter.builder("USER_DELETED")
                .description("User deleted!")
                .register(registry);

        this.userFindWithSuccess = Counter.builder("USER_FIND")
                .description("User find!")
                .register(registry);

        this.userErrors = Counter.builder("USER_ERRORS")
                .description("Error on user job...")
                .register(registry);
    }

    @PostMapping
    ResponseEntity<UserResponse> create(@RequestBody UserRequest body) throws Exception {
        try {
            var user = service.create(body).toResponse();
            userCreateWithSuccess.increment();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            userErrors.increment();
            throw new Exception();
        }
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> getUserList() throws Exception {
        try {
            var users = service.getUserList()
                    .stream()
                    .map(User::toResponse)
                    .toList();

            userFindWithSuccess.increment();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            userErrors.increment();
            throw new Exception();
        }
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable Long id) throws Exception {
        try {
            var user = service.getUserById(id).toResponse();
            userFindWithSuccess.increment();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            userErrors.increment();
            throw new Exception();
        }
    }

    @PatchMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest body
    ) throws Exception {
        try {
            var user = service.updateUserString(body, id).toResponse();
            userUpdatedWithSuccess.increment();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            userErrors.increment();
            throw new Exception();
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> replaceUser(
            @PathVariable Long id,
            @RequestBody UserRequest body
    ) throws Exception {
        try {
            var user = service.replaceUser(body, id).toResponse();
            userUpdatedWithSuccess.increment();
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            userErrors.increment();
            throw new Exception();
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @PathVariable Long id
    ) throws Exception {
        try {
            service.delete(id);
            userDeletedWithSuccess.increment();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            userErrors.increment();
            throw new Exception();
        }
    }
}
