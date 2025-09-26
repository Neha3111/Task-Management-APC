package com.example.taskservice.controller;

import com.example.taskservice.model.Task;
import com.example.taskservice.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest; // ✅ updated import
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired private TaskRepository repo;

    @GetMapping
    public ResponseEntity<?> list(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error","missing token"));
        List<Task> tasks = repo.findByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<?> create(HttpServletRequest req, @RequestBody Map<String,String> body) {
        Long userId = (Long) req.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error","missing token"));
        Task t = new Task();
        t.setUserId(userId);
        t.setTitle(body.get("title"));
        t.setDescription(body.getOrDefault("description",""));
        t.setStatus(body.getOrDefault("status","TODO"));
        long id = repo.save(t);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(HttpServletRequest req, @PathVariable long id) {
        Long userId = (Long) req.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error","missing token"));
        Task t = repo.findByIdAndUserId(id,userId);
        if (t == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(t);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(HttpServletRequest req, @PathVariable long id, @RequestBody Map<String,String> body) {
        Long userId = (Long) req.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error","missing token"));
        Task exist = repo.findByIdAndUserId(id,userId);
        if (exist == null) return ResponseEntity.notFound().build();
        exist.setTitle(body.getOrDefault("title", exist.getTitle()));
        exist.setDescription(body.getOrDefault("description", exist.getDescription()));
        exist.setStatus(body.getOrDefault("status", exist.getStatus()));
        repo.update(exist);
        return ResponseEntity.ok(Map.of("updated", true));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(HttpServletRequest req, @PathVariable long id) {
        Long userId = (Long) req.getAttribute("userId");
        if (userId == null) return ResponseEntity.status(401).body(Map.of("error","missing token"));
        Task exist = repo.findByIdAndUserId(id,userId);
        if (exist == null) return ResponseEntity.notFound().build();
        repo.delete(id);
        return ResponseEntity.ok(Map.of("deleted", true));
    }
}
