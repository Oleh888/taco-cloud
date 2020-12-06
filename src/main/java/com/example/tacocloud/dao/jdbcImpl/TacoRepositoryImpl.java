package com.example.tacocloud.dao.jdbcImpl;

import com.example.tacocloud.dao.TacoRepository;
import com.example.tacocloud.model.Taco;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Date;

@Repository
public class TacoRepositoryImpl implements TacoRepository {
    private final JdbcTemplate jdbcTemplate;

    public TacoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Taco save(Taco taco) {
        long tacoId = saveTacoInfo(taco);
        taco.setId(tacoId);
        for (String ingredientId : taco.getIngredients()) {
            saveIngredientToTaco(ingredientId, tacoId);
        }

        return taco;
    }

    private long saveTacoInfo(Taco taco) {
        taco.setCreatedAt(new Date());
        PreparedStatementCreatorFactory factory =
                new PreparedStatementCreatorFactory(
                        "insert into Taco (name, createdAt) values (?, ?)",
                        Types.VARCHAR, Types.TIMESTAMP);
        factory.setReturnGeneratedKeys(true);
        PreparedStatementCreator preparedStatementCreator =factory.newPreparedStatementCreator(
                        Arrays.asList(taco.getName(), new Timestamp(taco.getCreatedAt().getTime()))
                );

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(preparedStatementCreator, keyHolder);

        return keyHolder.getKey().longValue();
    }

    private void saveIngredientToTaco(String ingredientId, long tacoId) {
        jdbcTemplate.update("insert into Taco_Ingredients (taco, ingredient) values (?, ?)",
                tacoId, ingredientId);
    }
}
