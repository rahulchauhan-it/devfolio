# ADR 0003: Flyway migrations with Hibernate schema validation

- **Status:** Accepted
- **Date:** 2026-11-10

## Context

The schema must be identical in every environment (local, tests, containers, production) and must change in a reviewable, repeatable way. Letting the ORM generate or alter tables hides changes and can silently differ between environments.

## Decision

- All schema changes are versioned Flyway migrations in `backend/src/main/resources/db/migration` (`V1__init.sql`, `V2__experience.sql`, ...).
- `spring.jpa.hibernate.ddl-auto` is `validate`: Hibernate only checks that the entities match the schema and refuses to start otherwise. It never creates or alters tables.
- An applied migration is never edited. Flyway checks its checksum and fails the startup on a mismatch. Any change is a new versioned file.
- Constraints are given explicit names (for example `uq_project_slug` and `ck_skill_proficiency`), so errors and tests can refer to a constraint by name.

## Consequences

- Positive: every environment reaches the same schema by running the same files in the same order, starting from an empty database.
- Positive: schema changes show up as SQL in pull requests and are tested against a real PostgreSQL.
- Positive: a mismatch between an entity and the schema is found at startup, not in production.
- Negative: more typing than letting Hibernate generate tables, and fixing a mistake in an applied migration needs a corrective migration.
- Note: `validate` does not check constraints, nullability or defaults, so integration tests cover those.

## Alternatives considered

- **`ddl-auto: update`:** silently alters the schema outside version control, and cannot express renames, constraints or data changes safely.
- **`ddl-auto: create` or `create-drop`:** drops and recreates tables, which is only acceptable for throwaway databases.
- **Manual SQL scripts:** no record of what each database has already run, and environments drift.
