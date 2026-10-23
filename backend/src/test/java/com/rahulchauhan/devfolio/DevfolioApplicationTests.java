package com.rahulchauhan.devfolio;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class DevfolioApplicationTests {

	@Container
	@ServiceConnection
	static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void rejectsProjectWithInvalidStatus(){
		String sql= """
				INSERT INTO PROJECT (slug,title,summary,started_on, status)
				VALUES ('jobportal', 'jobportal', 'Job Portal Web App', Date '2026-10-01', 'WRONG')
				""";
		DataIntegrityViolationException ex=assertThrows(
				DataIntegrityViolationException.class,
				()->jdbcTemplate.update(sql));
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
