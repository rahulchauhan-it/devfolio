package com.rahulchauhan.devfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
@Import(TestcontainersConfig.class)
class DevfolioApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
    }

    @Test
    void rejectsProjectWithInvalidStatus() {
        String sql = """
                INSERT INTO PROJECT (slug,title,summary,started_on, status)
                VALUES ('jobportal', 'jobportal', 'Job Portal Web App', Date '2026-10-01', 'WRONG')
                """;
        DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () -> jdbcTemplate.update(sql));
        assertTrue(ex.getMessage().contains("ck_project_status"));
    }

    @Test
    void acceptsProjectWithValidStatus() {
        String sql = """
                INSERT INTO PROJECT (slug,title,summary,started_on, status)
                VALUES ('jobportal', 'jobportal', 'Job Portal Web App', Date '2026-10-01', 'DRAFT')
                """;
        assertDoesNotThrow(() -> jdbcTemplate.update(sql));
    }
}
