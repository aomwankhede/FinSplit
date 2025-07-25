package aom.finsplit.finsplit.services;

import aom.finsplit.finsplit.entities.User;
import aom.finsplit.finsplit.interfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<User> users = Arrays.asList(new User(), new User());
        when(repository.findAll()).thenReturn(users);
        assertEquals(2, userService.getAll().size());
    }

    @Test
    void testGetUserFromID_Success() {
        User user = new User();
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        assertTrue(userService.getUserFromID(1L).isPresent());
    }

    @Test
    void testGetUserFromID_Failure() {
        when(repository.findById(2L)).thenThrow(new RuntimeException("DB Error"));
        assertTrue(userService.getUserFromID(2L).isEmpty());
    }

    @Test
    void testSaveUser_Success() {
        Map<String, String> input = Map.of(
                "userName", "aom",
                "password", "pass",
                "email", "aom@example.com",
                "walletBalance", "100"
        );
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.existsByUserName(anyString())).thenReturn(false);
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        assertTrue(userService.saveUser(input).isPresent());
    }

    @Test
    void testSaveUser_Duplicate() {
        Map<String, String> input = Map.of("userName", "aom", "email", "aom@example.com");
        when(repository.existsByEmail("aom@example.com")).thenReturn(true);
        assertTrue(userService.saveUser(input).isEmpty());
    }

    @Test
    void testUpdateUser_Success() {
        User user = new User("aom", "pass", "aom@example.com", "100");
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        Map<String, String> update = Map.of("email", "new@example.com");
        Optional<User> updated = userService.updateUser(1L, update);
        assertTrue(updated.isPresent());
        assertEquals("new@example.com", updated.get().getEmail());
    }

    @Test
    void testUpdateUser_Failure() {
        when(repository.existsById(1L)).thenReturn(false);
        assertTrue(userService.updateUser(1L, Map.of()).isEmpty());
    }

    @Test
    void testDeleteUser_Success() {
        User user = new User();
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(repository).delete(user);
        assertTrue(userService.deleteUser(1L).isPresent());
    }

    @Test
    void testDeleteUser_NotFound() {
        when(repository.existsById(1L)).thenReturn(false);
        assertTrue(userService.deleteUser(1L).isEmpty());
    }
}

//     @Test
//     void testLogin_ByEmail_Success() {
//         Map<String, String> input = Map.of(
//                 "email", "aom@example.com",
//                 "password", "pass",
//                 "userName", "aom"
//         );
//         User user = new User("aom", "pass", "aom@example.com", "100");
//         when(repository.existsByEmail("aom@example.com")).thenReturn(true);
//         when(repository.findByEmail("aom@example.com")).thenReturn(user);
//         assertTrue(userService.login(input).isPresent());
//     }

//     @Test
//     void testLogin_Failure() {
//         Map<String, String> input = Map.of(
//                 "email", "fail@example.com",
//                 "password", "wrong",
//                 "userName", "wrongUser"
//         );
//         when(repository.existsByEmail("fail@example.com")).thenReturn(false);
//         when(repository.existsByUserName("wrongUser")).thenReturn(false);
//         assertTrue(userService.login(input).isEmpty());
//     }
// }
