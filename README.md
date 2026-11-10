# DevFolio

   [![CI](https://github.com/rahulchauhan-it/devfolio/actions/workflows/ci.yml/badge.svg)](https://github.com/rahulchauhan-it/devfolio/actions/workflows/ci.yml)

> A containerized portfolio platform for a Full Stack Java Developer, currently under construction: REST API, admin CMS, live GitHub integration, observability, and CI/CD.

**Live demo:** _comming soon_

## About me

Hi, I'm **Rahul Chauhan**, a fresher Full Stack Java Developer.

I'm building DevFolio to learn how production systems are made: Spring Boot APIs, PostgreSQL, Docker and automated pipelines.

Reach me on [LinkedIn](https://www.linkedin.com/in/rahulchauhan-it/) or [GitHub](https://github.com/rahulchauhan-it).

## Why this project exists

A portfolio should *prove* skills, not just list them. DevFolio is built like a real product:
the site itself demonstrates Java/Spring Boot, database design, security, testing, and DevOps.

## Features

**Done**
- [x] Versioned database schema (Flyway) for projects, skills, experience and contact messages
- [x] Public read API for projects: paginated list and detail by slug (published projects only)
- [x] Consistent error responses (RFC 9457 `ProblemDetail`)
- [x] Automated tests: unit, web layer and integration tests on real PostgreSQL (Testcontainers)
- [x] Multi-stage Docker image and Docker Compose setup

**Planned**
- [ ] CI with GitHub Actions
- [ ] Public deployment
- [ ] React frontend: Home, Projects, Project detail, Skills, About, Contact, with dark/light mode
- [ ] OpenAPI/Swagger documentation
- [ ] Admin CMS (Spring Security + JWT) to manage projects and Markdown case studies
- [ ] Secure contact form (validation, rate limiting)
- [ ] Live GitHub integration with caching
- [ ] Public status page powered by Spring Actuator

## Tech stack

| Layer | Technology | Status |
|---|---|---|
| Backend | Java 21, Spring Boot 4 (Web, Validation, Data JPA, Actuator) | Done |
| Database | PostgreSQL 18, Flyway migrations | Done |
| Testing | JUnit 5, Mockito, AssertJ, MockMvc, Testcontainers | Done |
| DevOps | Docker (multi-stage build), Docker Compose | Done |
| CI/CD | GitHub Actions | Planned |
| Security | Spring Security, JWT | Planned |
| Frontend | React, TypeScript, Vite, Tailwind CSS, TanStack Query | Planned |
| Hosting | Vercel (frontend), Render or Fly.io (backend), Neon (PostgreSQL) | Planned |

## API

| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/projects?page=0&size=10` | Paginated list of published projects (page is zero-based, size is capped at 50) |
| GET | `/api/v1/projects/{slug}` | One published project with its skills; `404` if missing or not published |
| GET | `/actuator/health` | Health check |

Errors use the standard `application/problem+json` format.

## Run locally

**With Docker (recommended)**

```bash
git clone https://github.com/<your-github-username>/devfolio.git
cd devfolio
cp .env.example .env        # Windows (cmd): copy .env.example .env
docker compose up --build
```

- Health check: http://localhost:8080/actuator/health
- Projects API: http://localhost:8080/api/v1/projects

PostgreSQL is also available on `localhost:5433` for tools like psql or IntelliJ.

**Run the tests** (Docker must be running, because the tests start PostgreSQL with Testcontainers)

```bash
cd backend
./mvnw test
```

## Repository structure

```
devfolio/
├── backend/             Spring Boot API (Dockerfile, Flyway migrations, tests)
├── docs/                Architecture, database schema, wireframes, decision records
├── docker-compose.yml   PostgreSQL + backend
└── .env.example         Required environment variables
```

## Documentation

- [Architecture](docs/architecture.md)
- [Database schema](docs/db-schema.md)
- [Wireframes](docs/wireframes.md)
- [Decision records](docs/adr)

## Roadmap

| Step | Goal | Status |
|---|---|---|
| 1 | Setup, wireframes, database schema | Done |
| 2 | Backend core: Flyway, entities, public Projects API, tests | Done |
| 3 | Docker and Docker Compose | Done |
| 4 | CI with GitHub Actions | In progress |
| 5 | Deploy backend and database (public URL) | Next |
| 6 | React frontend | Planned |
| 7 | Security (JWT), admin CMS, contact form | Planned |
| 8 | GitHub integration, status page, case studies | Planned |

## Conventions

- Conventional Commits (`feat:`, `fix:`, `docs:`, `test:`, `chore:`)
- Feature branches + pull requests, even when working solo
- No secrets in Git; configuration through environment variables

## License

MIT
