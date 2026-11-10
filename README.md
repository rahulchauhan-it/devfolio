# DevFolio

> A containerized portfolio platform for a Full Stack Java Developer, currently under construction: REST API, admin CMS, live GitHub integration, observability, and CI/CD.

## About me

Hi, I'm **Rahul Chauhan**, a fresher Full Stack Java Developer.

I'm building DevFolio to learn how production systems are made: Spring Boot APIs, PostgreSQL, Docker and automated pipelines.

Reach me on [LinkedIn](https://www.linkedin.com/in/rahul-chauhan-dev/) or [GitHub](https://github.com/rahul-chauhan-dev).

## Why this project exists

A portfolio should *prove* skills, not just list them. DevFolio is built like a real product:
the site itself demonstrates Java/Spring Boot, database design, security, testing, and DevOps.

## Features

**Core**
- [ ] Home, About, Projects, Project detail, Skills, Experience, Contact
- [ ] Responsive, accessible, dark/light mode
- [ ] Resume download

**Differentiators**
- [ ] Admin CMS (JWT-secured) to manage projects and Markdown case studies
- [ ] Live GitHub integration with caching (Caffeine)
- [ ] Case studies with architecture diagrams (Mermaid)
- [ ] Public `/status` page powered by Spring Actuator
- [ ] Secure contact form (validation, rate limiting, honeypot, async email)
- [ ] Privacy-friendly analytics (no cookies, no personal data)

## Tech stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot, Spring Web, Validation, Data JPA, Security, Actuator |
| Database | PostgreSQL, Flyway migrations |
| Frontend | React, TypeScript, Vite, Tailwind CSS, TanStack Query |
| Testing | JUnit 5, Mockito, Testcontainers, Vitest, React Testing Library |
| DevOps | Docker (multi-stage), Docker Compose, GitHub Actions |
| Hosting | Vercel (frontend), Render or Fly.io (backend), Neon (Postgres) |

## Architecture

See [docs/architecture.md](docs/architecture.md). Decisions are recorded in [docs/adr](docs/adr).

## Design docs

- [Database schema](docs/db-schema.md)
- [Wireframes](docs/wireframes.md)

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

## Roadmap

| Week | Goal | Status |
|---|---|---|
| 1 | Setup, wireframes, DB schema | In progress |
| 2 | Backend core, Flyway, tests, Swagger | |
| 3 | Frontend core | |
| 4 | Auth, admin CMS, contact form | |
| 5 | GitHub integration, status page, case studies | |
| 6 | Docker, CI/CD, deploy, polish | |

## Conventions

- Conventional Commits (`feat:`, `fix:`, `docs:`, `test:`, `chore:`)
- Feature branches + pull requests, even when working solo
- No secrets in Git; configuration through environment variables

## License

MIT
