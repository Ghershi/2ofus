package com.example.backendversion.controller;

import com.example.backendversion.entity.UserEntity;
import com.example.backendversion.exceptions.EmailAlreadyExistsException;
import com.example.backendversion.exceptions.UserNameAlreadyExistsException;
import com.example.backendversion.klassen.JwtTokenUtil;
import com.example.backendversion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> req) {
        try {
            UserEntity user = userService.register(
                    (String) req.get("username"),
                    (String) req.get("password"),
                    (String) req.get("email"),
                    (int) req.get("age")
            );
            return ResponseEntity.ok(Map.of("message", "Erfolgreich angemeldet!", "username", user.getUsername()));
        } catch (EmailAlreadyExistsException | UserNameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));

        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        Optional<UserEntity> userOpt = userService.authenticate(req.get("username"), req.get("password"));
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Falscher Username oder Passwort"));
        }
        UserEntity user = userOpt.get();
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole());


        return ResponseEntity.ok(Map.of("token", token, "username", user.getUsername(), "userId", user.getUserId(), "userEmail", user.getUserEmail()));
    }
}


