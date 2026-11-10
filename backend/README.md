# DevFolio Backend

Spring Boot 4 (Java 21) REST API for DevFolio, backed by PostgreSQL with Flyway migrations.

## Run

**With Docker:** see the root [README](../README.md#run-locally).

**Without Docker:** you need Java 21 and a local PostgreSQL database. Set these environment variables (PowerShell example):

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/devfolio"
$env:DB_USERNAME = "<your-user>"
$env:DB_PASSWORD = "<your-password>"
.\mvnw.cmd spring-boot:run
```

Flyway creates the schema on startup. With `ddl-auto: validate`, Hibernate refuses to start if an entity does not match the database.

## Test

```bash
./mvnw test        # Windows: .\mvnw.cmd test
```

Docker must be running. The tests start a throwaway PostgreSQL container with Testcontainers, so no local database or environment variables are needed.

- Unit tests: services with Mockito
- Web layer tests: controllers with `@WebMvcTest`
- Integration tests: repositories and the API against real PostgreSQL

## Package layout (feature-based)

```
com.rahulchauhan.devfolio
├── project/    controller, service, repository, entity, DTOs
├── skill/      entity, repository, DTO
└── common/     page response, global exception handler
```

Entities never leave the service layer; the API returns DTOs.

## Database migrations

Flyway migrations are in `src/main/resources/db/migration`:

- `V1__init.sql`: projects, skills, project_skill, contact messages, admin users
- `V2__experience.sql`: experience

An applied migration is never edited: Flyway checks its checksum, and the app refuses to start on a mismatch. Changes go into a new versioned file (`V3__...`).

## Configuration

| Variable | Description |
|---|---|
| `DB_URL` | JDBC URL of the PostgreSQL database |
| `DB_USERNAME` | Database user |
| `DB_PASSWORD` | Database password |

See `.env.example` in the repository root for the full list.