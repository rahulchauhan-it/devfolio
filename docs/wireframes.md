# Wireframes

Low-fidelity layouts for the three main pages. They are design intent for the frontend, not final designs: the structure and reasoning matter more than the boxes. Sections that depend on features that are not built yet (GitHub block, status footer, filters) are listed as planned in the [roadmap](../README.md#roadmap).

**Design principles**
- A recruiter should understand who I am and see proof within 10 seconds.
- Projects are the hero. Everything else supports them.
- Mobile-first: design the phone layout, then widen it.
- Every page has one clear primary action.

---

## 1. Home (desktop, 1200px)

```
+--------------------------------------------------------------------------+
| [Logo: Rahul Chauhan]   Projects   Skills   About   Contact   [Sun/Moon] |
+--------------------------------------------------------------------------+
|                                                                          |
|   Hi, I'm Rahul Chauhan.                              +------------+     |
|   Full Stack Java Developer                           |            |     |
|   I build secure, tested REST APIs with Spring Boot   |   Photo /  |     |
|   and React front ends.                               |   avatar   |     |
|                                                       |            |     |
|   [ View Projects ]   [ Download Resume ]             +------------+     |
|   GitHub  LinkedIn  Email                                                |
|                                                                          |
+--------------------------------------------------------------------------+
|  FEATURED PROJECTS                                       [ See all -> ]  |
|  +----------------+  +----------------+  +----------------+              |
|  | [cover image]  |  | [cover image]  |  | [cover image]  |              |
|  | DevFolio       |  | Project 2      |  | Project 3      |              |
|  | One-line pitch |  | One-line pitch |  | One-line pitch |              |
|  | Java Spring PG |  | Java React     |  | Java MySQL     |              |
|  +----------------+  +----------------+  +----------------+              |
+--------------------------------------------------------------------------+
|  TECH I USE                                                              |
|  Backend: Java, Spring Boot, JPA      Frontend: React, TypeScript        |
|  Data: PostgreSQL, Flyway             DevOps: Docker, GitHub Actions     |
+--------------------------------------------------------------------------+
|  LIVE FROM GITHUB   (cached by my API)                                   |
|  Public repos: 12   Recent commit: "feat: add admin CRUD" (2 days ago)   |
+--------------------------------------------------------------------------+
|  GET IN TOUCH:  short message + [ Contact me ]                           |
+--------------------------------------------------------------------------+
| (c) 2026 Rahul Chauhan   |  Status: all systems up  |  Source on GitHub  |
+--------------------------------------------------------------------------+
```

**Why:** the hero gives identity, role and two actions. Featured projects come before skills because proof beats claims. The GitHub block and the footer status link are the differentiators (live API data, observability).

**Data sources:** featured projects come from `GET /api/v1/projects` (the `featured` flag and `displayOrder` decide the order). The GitHub block and the status link depend on planned endpoints.

**Mobile:** single column; nav collapses to a hamburger; hero text first, photo below; project cards stacked.

---

## 2. Projects list (desktop)

```
+--------------------------------------------------------------------------+
| [Logo]                  Projects   Skills   About   Contact   [Sun/Moon] |
+--------------------------------------------------------------------------+
|  Projects                                                                |
|  Things I've built and what I learned from each.                         |
|                                                                          |
|  Filter by skill:  (All) (Java) (Spring Boot) (React) (PostgreSQL) ...   |
|  Sort: [ Featured v ]                          Search: [______________]  |
|                                                                          |
|  +---------------------------+   +---------------------------+           |
|  | [cover image]             |   | [cover image]             |           |
|  | DevFolio       [Featured] |   | Library Management        |           |
|  | Portfolio platform with   |   | Spring MVC app with       |           |
|  | CMS, CI/CD, monitoring.   |   | role-based access.        |           |
|  | [Java][Spring][PG][Docker]|   | [Java][Spring][MySQL]     |           |
|  | [Live demo]  [GitHub]     |   | [Live demo]  [GitHub]     |           |
|  +---------------------------+   +---------------------------+           |
|  +---------------------------+   +---------------------------+           |
|  | ...                       |   | ...                       |           |
|  +---------------------------+   +---------------------------+           |
|                                                                          |
|  [ < Prev ]   Page 1 of 2   [ Next > ]                                   |
+--------------------------------------------------------------------------+
|  footer                                                                  |
+--------------------------------------------------------------------------+
```

**States to design:** loading (skeleton cards), empty ("No projects match this filter" plus a reset button), error (message plus retry).

**Backend:**
- Available today: `GET /api/v1/projects?page=0&size=6`. The response has `items`, `page` (zero-based), `size`, `totalElements` and `totalPages`; each item has `slug`, `title`, `summary`, `coverImageUrl`, `featured`, `startedOn` and the skill names.
- Planned: filtering by skill (`?skill=java`), choosing the sort order, search, and `GET /api/v1/skills` for the skill chips.

**Mobile:** cards stack in one column; filters become a horizontally scrollable chip row.

---

## 3. Project detail (desktop)

```
+--------------------------------------------------------------------------+
| [Logo]                  Projects   Skills   About   Contact   [Sun/Moon] |
+--------------------------------------------------------------------------+
|  < Back to projects                                                      |
|                                                                          |
|  DevFolio                                                                |
|  Full-stack portfolio platform with CMS, CI/CD and monitoring            |
|  [Live demo]  [Source on GitHub]                          Started Aug 26 |
|                                                                          |
|  +--------------------------------------------------------------------+  |
|  |                   hero screenshot / cover image                    |  |
|  +--------------------------------------------------------------------+  |
|                                                                          |
|  +--------------------------------------+  +-----------------------+     |
|  | CASE STUDY (rendered Markdown)       |  | TECH USED             |     |
|  |  ## The problem                      |  | [Java 21] [Spring]    |     |
|  |  ## Architecture                     |  | [PostgreSQL] [Docker] |     |
|  |     [ Mermaid diagram ]              |  | [React] [TypeScript]  |     |
|  |  ## Key decisions and trade-offs     |  +-----------------------+     |
|  |  ## Challenges and what I learned    |  | LIVE REPO STATS       |     |
|  |  ## What I would improve next        |  | Stars 3  Commits 120  |     |
|  |                                      |  | Last push: 2 days ago |     |
|  +--------------------------------------+  +-----------------------+     |
|                                                                          |
|  MORE PROJECTS:  [card]  [card]                                          |
+--------------------------------------------------------------------------+
```

**Why:** interviewers read the case study to judge how I think. The sidebar gives quick facts. Repo stats come from the planned, cached GitHub endpoint.

**Backend:** `GET /api/v1/projects/{slug}` returns the published project with `caseStudy` (Markdown), `repoUrl`, `liveUrl` and its skills (name, category, proficiency, icon), sorted by proficiency and then by name. A draft or unknown slug returns a 404 Problem Details response.

**Mobile:** the sidebar blocks move below the title and above the case study; Mermaid diagrams scroll horizontally.
