package aom.finsplit.finsplit.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aom.finsplit.finsplit.entities.User;
import aom.finsplit.finsplit.services.JwtService;
import aom.finsplit.finsplit.services.UserService;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/allusers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @GetMapping
    public ResponseEntity<?> getUserFromID(@RequestParam String param) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserFromID(Long.parseLong(param)));
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody Map<String, String> user) {
        Optional<User> u1 = userService.saveUser(user);
        Map<String, String> claims = new HashMap<>();
        claims.put("id", String.valueOf(u1.get().getId()));
        String token = jwtService.createToken(claims);
        if (u1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User can't be created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> user) {
        Optional<User> u1 = userService.login(user);
        if (u1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        }
        Map<String, String> claims = new HashMap<>();
        claims.put("id", String.valueOf(u1.get().getId()));
        String token = jwtService.createToken(claims);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(token);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal User user, @RequestBody Map<String, String> entity) {
        Optional<User> u1 = userService.updateUser(user.getId(), entity);
        if (u1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(u1.get());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal User user) {
        Optional<User> u1 = userService.deleteUser(user.getId());
        if (u1.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(u1.get());
    }
}
