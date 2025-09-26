
package com.example.authservice.controller;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private UserRepository userRepo;
    @Autowired private JwtUtil jwtUtil;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body(Map.of("error","username & password required"));
        if (userRepo.findByUsername(username) != null) return ResponseEntity.badRequest().body(Map.of("error","username exists"));
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        long id = userRepo.save(u);
        return ResponseEntity.ok(Map.of("id", id, "username", username));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) return ResponseEntity.badRequest().body(Map.of("error","username & password required"));
        User u = userRepo.findByUsername(username);
        if (u == null) return ResponseEntity.status(401).body(Map.of("error","invalid credentials"));
        if (!encoder.matches(password, u.getPassword())) return ResponseEntity.status(401).body(Map.of("error","invalid credentials"));
        String token = jwtUtil.generateToken(u.getId(), u.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
