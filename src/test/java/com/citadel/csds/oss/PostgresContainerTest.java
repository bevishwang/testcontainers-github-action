package com.citadel.csds.oss;

import com.citadel.csds.oss.config.PostgresTestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest(properties = "spring.test.database.replace=none")
@ContextConfiguration(classes = {PostgresTestConfiguration.class})
public class PostgresContainerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void cleanUp() {
        jdbcTemplate.execute("DELETE FROM users");
    }

    @Test
    public void givenUser_WhenInserting_ThenUserIsPersisted() {
        jdbcTemplate.execute("INSERT INTO users (id, name, email, password) VALUES (1, 'user', 'hello@gmail.com', 123456)");
        var username = jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = 1", (rs, rowNum) -> rs.getString("name"));

        Assertions.assertEquals("user", username);
    }

    @Test
    public void givenNothing_WhenQuerying_ThenNoUsersAreFound() {
        var users = jdbcTemplate.queryForList("SELECT * FROM users");

        Assertions.assertTrue(users.isEmpty());
    }

    @Test
    public void giveUsers_whenInsertAndDelete_thenNoUsersAreFound() {
        jdbcTemplate.execute("INSERT INTO users (id, name, email, password) VALUES (1, 'user', 'hello@gmail.com', 123456)");
        jdbcTemplate.execute("DELETE FROM users WHERE id = 1");

        jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> {
            Assertions.fail("No users should be found");
            return null;
        });
    }

}
