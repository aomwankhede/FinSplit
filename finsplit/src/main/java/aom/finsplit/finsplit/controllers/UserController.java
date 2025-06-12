package aom.finsplit.finsplit.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aom.finsplit.finsplit.entities.User;
import aom.finsplit.finsplit.services.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/allusers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @GetMapping
    public ResponseEntity<?> getUserFromID(@RequestParam String param) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserFromID(Long.parseLong(param)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody Map<String, String> user) {
        Optional<User> u1 = userService.saveUser(user);
        if (u1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User can't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(u1.get().getId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> user) {
        boolean flag = userService.login(user);
        if (!flag) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body("User Found and logged in");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody Map<String, String> entity) {
        Optional<User> u1 = userService.updateUser(Long.parseLong(id), entity);
        if (u1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(u1.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        Optional<User> u1 = userService.deleteUser(Long.parseLong(id));
        if(u1.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(u1.get());
    }
}
