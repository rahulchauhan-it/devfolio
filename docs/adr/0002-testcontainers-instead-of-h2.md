# ADR 0002: Test against real PostgreSQL with Testcontainers instead of H2 or mocks

- **Status:** Accepted
- **Date:** 2026-11-10

## Context

Many of the rules that matter in DevFolio live in the database: unique slugs and skill names, CHECK constraints (project status, skill proficiency range), foreign keys and the `project_skill` join table. Application code and the Flyway migrations must agree with each other. Tests need to prove that, not assume it.

## Decision

Run repository, API and context tests against a throwaway PostgreSQL container started by Testcontainers, using the same major version as production (`postgres:18-alpine`). The container is defined once, in a shared test configuration (`TestcontainersConfig`) wired with `@ServiceConnection`. Flyway applies the real migrations to the empty database on every run.

Pure unit tests (services, controllers with a mocked service) stay fast and use Mockito, with no database.

## Consequences

- Positive: constraints, types and migrations are exercised exactly as in production. A broken migration fails the build.
- Positive: every run starts from an empty database, so tests cannot depend on leftover data from earlier runs.
- Positive: no environment variables or local PostgreSQL are needed to run `mvnw test`.
- Negative: Docker must be running to run the tests, and the first run downloads the image.
- Negative: slower than in-memory tests. Mitigated by keeping most tests as plain unit tests and one container per test run.
- Rule learned the hard way: a failed statement aborts the PostgreSQL transaction, so each test expects at most one database error.

## Alternatives considered

- **H2 in-memory database:** fast, but a different engine with a different SQL dialect and behavior. A migration could pass on H2 and fail on PostgreSQL.
- **Mocking repositories only:** cannot verify constraints, mappings or queries at all.
- **A shared local PostgreSQL:** depends on the developer's machine and leaves data between runs.
