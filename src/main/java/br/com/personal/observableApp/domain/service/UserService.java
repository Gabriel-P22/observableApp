package br.com.personal.observableApp.domain.service;


import br.com.personal.observableApp.application.dto.UserRequest;
import br.com.personal.observableApp.application.dto.UserResponse;
import br.com.personal.observableApp.domain.entity.User;
import br.com.personal.observableApp.infra.exception.RequestErrorException;
import br.com.personal.observableApp.infra.exception.UserNotFoundException;
import br.com.personal.observableApp.infra.persistence.user.UserEntity;
import br.com.personal.observableApp.infra.persistence.user.UserRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.personal.observableApp.constants.ExceptionConstants.EMAIL_ALREADY_IN_USE;
import static br.com.personal.observableApp.constants.ExceptionConstants.USER_NOT_FOUND;
import static br.com.personal.observableApp.constants.LogConstants.USER_CREATED;
import static br.com.personal.observableApp.constants.LogConstants.USER_DELETED;
import static br.com.personal.observableApp.constants.LogConstants.USER_FIND;
import static br.com.personal.observableApp.constants.LogConstants.USER_UPDATED;
import static br.com.personal.observableApp.constants.LogMessagesConstants.USER_CREATED_COUNT;
import static br.com.personal.observableApp.constants.LogMessagesConstants.USER_DELETED_COUNT;
import static br.com.personal.observableApp.constants.LogMessagesConstants.USER_FIND_COUNT;
import static br.com.personal.observableApp.constants.LogMessagesConstants.USER_UPDATED_COUNT;

@Service
public class UserService {

    private final UserRepository repository;

    Counter userCreateWithSuccess;
    Counter userUpdatedWithSuccess;
    Counter userDeletedWithSuccess;
    Counter userFindWithSuccess;

    public UserService(UserRepository repository, MeterRegistry registry) {
        this.repository = repository;

        this.userCreateWithSuccess = Counter.builder(USER_CREATED.name())
                .description(USER_CREATED_COUNT.getValue())
                .register(registry);

        this.userUpdatedWithSuccess = Counter.builder(USER_UPDATED.name())
                .description(USER_UPDATED_COUNT.getValue())
                .register(registry);

        this.userDeletedWithSuccess = Counter.builder(USER_DELETED.name())
                .description(USER_DELETED_COUNT.getValue())
                .register(registry);

        this.userFindWithSuccess = Counter.builder(USER_FIND.name())
                .description(USER_FIND_COUNT.getValue())
                .register(registry);
    }

    public UserResponse create(UserRequest body) {
        try {
            UserEntity entity = new UserEntity(
                    body.getName(),
                    body.getEmail(),
                    body.getPassword()
            );

            userCreateWithSuccess.increment();

            return repository.save(entity).toDomain().toResponse();
        } catch (Exception e) {
            throw new RequestErrorException(EMAIL_ALREADY_IN_USE.name());
        }
    }

    public List<UserResponse> getUserList() {
        List<UserResponse> entityList = repository.findAll()
                .stream()
                .map(UserEntity::toDomain)
                .map(User::toResponse)
                .toList();

        userFindWithSuccess.increment();

        return entityList;
    }

    public UserResponse getUserById(String id) {
        UserEntity entity = repository.findById(id).orElseThrow(() ->
                new UserNotFoundException(USER_NOT_FOUND.name()));
        userFindWithSuccess.increment();

        return entity.toDomain().toResponse();
    }

    public UserResponse updateUser(UserRequest body, String id) {
        UserEntity entity = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(USER_NOT_FOUND.name()));

        if (body.getName() != null) {
            entity.setName(body.getName());
        }

        if (body.getEmail() != null) {
            entity.setEmail(body.getEmail());
        }

        if (body.getPassword() != null) {
            entity.setPassword(body.getPassword());
        }

        repository.save(entity);

        userUpdatedWithSuccess.increment();

        return entity.toDomain().toResponse();
    }

    public UserResponse replaceUser(UserRequest body, String id) {
        UserEntity entity = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(USER_NOT_FOUND.name()));

        entity.setName(body.getName());
        entity.setEmail(body.getEmail());
        entity.setPassword(body.getPassword());

        repository.save(entity);
        userUpdatedWithSuccess.increment();

        return entity.toDomain().toResponse();
    }

    public void delete(String id) {
        UserEntity entity = repository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(USER_NOT_FOUND.name()));

        userDeletedWithSuccess.increment();
        repository.delete(entity);
    }

}
