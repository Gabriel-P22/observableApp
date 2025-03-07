package br.com.personal.observableApp.domain.service;


import br.com.personal.observableApp.application.dto.UserRequest;
import br.com.personal.observableApp.domain.entity.User;
import br.com.personal.observableApp.infra.persistence.user.UserEntity;
import br.com.personal.observableApp.infra.persistence.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(UserRequest body) {
        UserEntity entity = new UserEntity(
                body.getName(),
                body.getEmail(),
                body.getPassword()
        );
        return repository.save(entity).toDomain();
    }

    public List<User> getUserList() {
        return repository.findAll()
                .stream()
                .map(UserEntity::toDomain)
                .toList();
    }

    public User getUserById(Long id) {
        Optional<UserEntity> entity = repository.findById(id);
        return entity.map(UserEntity::toDomain).orElse(null);
    }

    public User updateUserString(UserRequest body, Long id) {
        Optional<UserEntity> entity = repository.findById(id);

        if (entity.isEmpty()) {
            return null;
        }

        if (body.getName() != null) {
            entity.get().setName(body.getName());
        }

        if (body.getEmail() != null) {
            entity.get().setEmail(body.getEmail());
        }

        if (body.getPassword() != null) {
            entity.get().setPassword(body.getPassword());
        }

        repository.save(entity.get());

        return entity.get().toDomain();
    }

    public User replaceUser(UserRequest body, Long id) {
        Optional<UserEntity> entity = repository.findById(id);

        if (entity.isEmpty()) {
            return null;
        }

        entity.get().setName(body.getName());
        entity.get().setEmail(body.getEmail());
        entity.get().setPassword(body.getPassword());

        repository.save(entity.get());

        return entity.get().toDomain();
    }

    public void delete(Long id) throws Exception {
        Optional<UserEntity> entity = repository.findById(id);

        if (entity.isEmpty()) {
            throw new Exception();
        }

        repository.delete(entity.get());
    }

}
