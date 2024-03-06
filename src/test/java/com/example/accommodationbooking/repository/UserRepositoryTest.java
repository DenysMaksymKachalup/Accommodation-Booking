package com.example.accommodationbooking.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.accommodationbooking.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/insert-user-repository-test.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = {"classpath:database/delete-all.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class UserRepositoryTest {
    private static final String EMAIL = "adminUser";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void existsUser_ByEmailIgnoreCase_returnBoolean() {
        boolean actual = userRepository.existsByEmailIgnoreCase(EMAIL);
        assertTrue(actual);
    }

    @Test
    public void findUser_ByEmail_returnOptionalUser() {
        Optional<User> actual = userRepository.findUserByEmail(EMAIL);
        User user = actual.orElseThrow();
        assertEquals(EMAIL, user.getEmail());
    }
}
