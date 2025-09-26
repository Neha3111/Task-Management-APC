package com.example.taskservice.repository;

import com.example.taskservice.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct; // ✅ updated import
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TaskRepository {

    @Autowired private JdbcTemplate jdbc;

    @PostConstruct
    public void init() {
        jdbc.execute("CREATE TABLE IF NOT EXISTS tasks (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "user_id BIGINT NOT NULL, " +
                "title VARCHAR(255) NOT NULL, " +
                "description TEXT, " +
                "status VARCHAR(50) DEFAULT 'TODO', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ");");
    }

    public long save(Task t) {
        jdbc.update("INSERT INTO tasks(user_id,title,description,status) VALUES(?,?,?,?)",
                t.getUserId(), t.getTitle(), t.getDescription(), t.getStatus());
        return jdbc.queryForObject(
                "SELECT id FROM tasks WHERE user_id=? ORDER BY id DESC LIMIT 1",
                new Object[]{t.getUserId()},
                Long.class
        );
    }

    public List<Task> findByUserId(Long userId) {
        return jdbc.query(
                "SELECT id,user_id,title,description,status FROM tasks WHERE user_id=?",
                new Object[]{userId},
                new TaskRowMapper()
        );
    }

    public Task findByIdAndUserId(Long id, Long userId) {
        try {
            return jdbc.queryForObject(
                    "SELECT id,user_id,title,description,status FROM tasks WHERE id=? AND user_id=?",
                    new Object[]{id, userId},
                    new TaskRowMapper()
            );
        } catch (Exception e) {
            return null;
        }
    }

    public void update(Task t) {
        jdbc.update(
                "UPDATE tasks SET title=?, description=?, status=? WHERE id=?",
                t.getTitle(), t.getDescription(), t.getStatus(), t.getId()
        );
    }

    public void delete(Long id) {
        jdbc.update("DELETE FROM tasks WHERE id=?", id);
    }

    static class TaskRowMapper implements RowMapper<Task> {
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task t = new Task();
            t.setId(rs.getLong("id"));
            t.setUserId(rs.getLong("user_id"));
            t.setTitle(rs.getString("title"));
            t.setDescription(rs.getString("description"));
            t.setStatus(rs.getString("status"));
            return t;
        }
    }
}
