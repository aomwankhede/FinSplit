package aom.finsplit.finsplit.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aom.finsplit.finsplit.entities.User;
import aom.finsplit.finsplit.interfaces.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository repository;

    public List<User> getAll() {
        return repository.findAll();
    }

    public Optional<User> getUserFromID(long id) {
        try {
            log.info("User fetched successfully with ID: {}", id);
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching user with ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }

    }

    public Optional<User> saveUser(Map<String, String> user) {
        if (repository.existsByEmail(user.get("email")) || repository.existsByUserName(user.get("userName"))) {
            log.warn("User with email {} or username {} already exists", user.get("email"), user.get("userName"));
            return Optional.empty();
        }
        try {
            User newUser = new User(user.get("userName"), user.get("password"), user.get("email"),
                    user.get("walletBalance"));
            Optional<User> u1 = Optional.of(repository.save(newUser));
            log.info("User created successfully with ID: {}", u1.get().getId());
            return u1;
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<User> updateUser(long id, Map<String, String> entity) {
        if (!repository.existsById(id)) {
            log.warn("User with ID {} does not exist", id);
            return Optional.empty();
        }
        try {
            User user = repository.findById(id).get();
            if (entity.get("email") != null) {
                user.setEmail(entity.get("email"));
            }
            if (entity.get("password") != null) {
                user.setPassword(entity.get("password"));
            }
            if (entity.get("userName") != null) {
                user.setUserName(entity.get("userName"));
            }
            if (entity.get("walletBalance") != null) {
                user.setWalletBalance(Integer.parseInt(entity.get("walletBalance")));
            }
            Optional<User> u1 = Optional.of(repository.save(user));
            log.info("User updated successfully with ID: {}", id);
            return u1;
        } catch (Exception e) {
            log.error("Error updating user with ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<User> deleteUser(long id) {
        if (!repository.existsById(id)) {
            log.warn("User with ID {} does not exist", id);
            return Optional.empty();
        }
        try {
            User user = repository.findById(id).get();
            repository.delete(user);
            log.info("User deleted successfully with ID: {}", id);
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Error deleting user with ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<User> login(Map<String, String> user) {
        if ((!repository.existsByEmail(user.get("email"))) || (!repository.existsByUserName(user.get("userName")))) {
            log.info("Failed login attempt");
            return Optional.empty();
        } else if (repository.existsByEmail(user.get("email"))) {
            User u1 = repository.findByEmail(user.get("email"));
            if (u1.getPassword().equals(user.get("password"))) {
                log.info("User logged in successfully with email: {}", user.get("email"));
                return Optional.of(u1);
            }
        } else if (repository.existsByUserName(user.get("userName"))) {
            User u1 = repository.findByUserName(user.get("userName"));
            if (u1.getPassword().equals(user.get("password"))) {
                log.info("User logged in successfully with username: {}", user.get("userName"));
                return Optional.of(u1);
            }
        }
        log.warn("Login failed for user with email: {} or username: {}", user.get("email"), user.get("userName"));
        return Optional.empty();
    }

}
