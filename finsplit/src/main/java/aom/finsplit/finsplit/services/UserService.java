package aom.finsplit.finsplit.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aom.finsplit.finsplit.entities.User;
import aom.finsplit.finsplit.interfaces.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public List<User> getAll() {
        return repository.findAll();
    }

    public Optional<User> getUserFromID(long id) {
        return repository.findById(id);
    }

    public Optional<User> saveUser(Map<String, String> user) {
        if (repository.existsByEmail(user.get("email")) || repository.existsByUserName(user.get("userName"))) {
            return Optional.empty();
        }
        User newUser = new User(user.get("userName"), user.get("password"), user.get("email"),
                user.get("walletBalance"));
        Optional<User> u1 = Optional.of(repository.save(newUser));
        return u1;
    }

    public Optional<User> updateUser(long id, Map<String, String> entity) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
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
        return u1;
    }

    public Optional<User> deleteUser(long id) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }
        User user = repository.findById(id).get();
        repository.delete(user);
        return Optional.of(user);
    }

    public Boolean login(Map<String, String> user) {
        if ((!repository.existsByEmail(user.get("email"))) || (!repository.existsByUserName(user.get("userName")))) {
            return false;
        } else if (repository.existsByEmail(user.get("email"))) {
            User u1 = repository.findByEmail(user.get("email"));
            if (u1.getPassword().equals(user.get("password"))) {
                return true;
            }
        } else if (repository.existsByUserName(user.get("userName"))) {
            User u1 = repository.findByUserName(user.get("userName"));
            if (u1.getPassword().equals(user.get("password"))) {
                return true;
            }
        }
        return false;
    }

}
