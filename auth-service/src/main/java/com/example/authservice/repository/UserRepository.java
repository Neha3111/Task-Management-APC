package com.example.authservice.repository;

import com.example.authservice.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbc;

    public long save(User u) {
        jdbc.update("INSERT INTO users(username,password) VALUES(?,?)", u.getUsername(), u.getPassword());
        return jdbc.queryForObject(
                "SELECT id FROM users WHERE username = ?",
                new Object[]{u.getUsername()},
                Long.class
        );
    }

    public User findByUsername(String username) {
        try {
            return jdbc.queryForObject(
                    "SELECT id, username, password FROM users WHERE username = ?",
                    new Object[]{username},
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setId(rs.getLong("id"));
                        u.setUsername(rs.getString("username"));
                        u.setPassword(rs.getString("password"));
                        return u;
                    }
            );
        } catch (Exception e) {
            return null;
        }
    }
}
