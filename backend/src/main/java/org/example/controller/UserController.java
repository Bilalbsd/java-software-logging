package org.example.controller;
import java.util.List;
import java.util.Map;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody
    User user) {
        try {
            User savedUser = userService.createUser(user);
            userService.setCurrentUser(savedUser);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody
    Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        try {
            User authenticatedUser = userService.loginUser(email, password);
            userService.setCurrentUser(authenticatedUser);
            return ResponseEntity.ok(authenticatedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        userService.setCurrentUser(null);
        return ResponseEntity.ok("Déconnexion réussie");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable
    String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable
    String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
