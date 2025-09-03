package com.simec.expense_tracker_api.dao;

import com.simec.expense_tracker_api.entity.Category;
import com.simec.expense_tracker_api.entity.Expense;
import com.simec.expense_tracker_api.filtering.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ExpenseDaoImpl implements ExpenseDao {

    private static final String SELECT_QUERY =
            "SELECT " +
                    "expense.id as expenseId, " +
                    "expense.name as expenseName, " +
                    "expense.applied_at, " +
                    "category.id as categoryId, " +
                    "category.name as categoryName " +
                    "FROM expense " +
                    "JOIN category " +
                    "ON expense.category_id = category.id";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExpenseDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Expense> find(long expenseId, long personId) {
        String sql = SELECT_QUERY + " WHERE expense.id = ? AND expense.person_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, new ExpenseRowMapper(), expenseId, personId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(long expenseId, long personId) {
        String sql = "DELETE FROM expense WHERE id = ? AND person_id = ?";
        jdbcTemplate.update(sql, expenseId, personId);
    }

    @Override
    public void update(Expense expense, long personId) {
        String sql = "UPDATE expense SET name = ?, applied_at = ?, category_id = ? WHERE id = ? AND person_id = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sql, PreparedStatement.NO_GENERATED_KEYS);
            statement.setString(1, expense.name());
            statement.setDate(2, Date.valueOf(expense.appliedAt()));
            statement.setLong(3, expense.category().id());
            statement.setLong(4, expense.id());
            statement.setLong(5, personId);
            return statement;
        });
    }

    @Override
    public long create(Expense expense, long personId) {
        String sql = "INSERT INTO expense (name, applied_at, person_id, category_id) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement statement = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, expense.name());
            statement.setDate(2, Date.valueOf(expense.appliedAt()));
            statement.setLong(3, personId);
            statement.setLong(4, expense.category().id());
            return statement;
        }, keyHolder);
        return ((Number) Objects.requireNonNull(keyHolder.getKeys()).get("id")).longValue();
    }

    @Override
    public List<Expense> findByFilter(Filter filter, long personId) {
        String sql = SELECT_QUERY + " WHERE expense.person_id = ? AND expense.applied_at BETWEEN ? AND ?";
        try {
            return jdbcTemplate.query(sql, new ExpenseRowMapper(), personId, filter.start(), filter.end());
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    @Override
    public List<Expense> findAll(long personId) {
        String sql = SELECT_QUERY + " WHERE expense.person_id = ?";
        try {
            return jdbcTemplate.query(sql, new ExpenseRowMapper(), personId);
        } catch (EmptyResultDataAccessException e) {
            return List.of();
        }
    }

    private static class ExpenseRowMapper implements RowMapper<Expense> {

        @Override
        public Expense mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Expense(
                    rs.getLong("expenseId"),
                    rs.getString("expenseName"),
                    rs.getDate("applied_at").toLocalDate(),
                    new Category(
                            rs.getLong("categoryId"),
                            rs.getString("categoryName")
                    )
            );
        }
    }
}
