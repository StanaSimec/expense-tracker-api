package com.simec.expense_tracker_api.dao;

import com.simec.expense_tracker_api.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class CategoryDaoImpl implements CategoryDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Category> findAll() {
        String sql = "SELECT id, name FROM category";
        return jdbcTemplate.query(sql, new CategoryRowMapper());
    }

    @Override
    public Optional<Category> findByName(String name) {
        String sql = "SELECT id, name FROM category WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new CategoryRowMapper(), name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private static class CategoryRowMapper implements RowMapper<Category> {

        @Override
        public Category mapRow(ResultSet rs, int rowNumber) throws SQLException {
            return new Category(rs.getLong("id"), rs.getString("name"));
        }
    }
}
