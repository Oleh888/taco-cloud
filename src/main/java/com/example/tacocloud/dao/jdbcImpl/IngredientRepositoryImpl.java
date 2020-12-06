package com.example.tacocloud.dao.jdbcImpl;

import com.example.tacocloud.dao.IngredientRepository;
import com.example.tacocloud.model.Ingredient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class IngredientRepositoryImpl implements IngredientRepository {
    private final JdbcTemplate jdbcTemplate;

    public IngredientRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query("select id, name, type from Ingredient",
                this::mapRowToIngredient);
    }

    private Ingredient mapRowToIngredient(ResultSet resultSet, int rowNum) 
            throws SQLException {
        return new Ingredient(resultSet.getString("id"), 
                resultSet.getString("name"),
                Ingredient.Type.valueOf(resultSet.getString("type")));           
    }

    @Override
    public Ingredient findOne(String id) {
        return jdbcTemplate.queryForObject("select id, name, type from Ingredient where id=?",
                this::mapRowToIngredient, id);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update("insert into Ingredient (id, name, type) values (?, ?, ?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString());
        return ingredient;
    }
}
