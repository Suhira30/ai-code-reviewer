# Requirements

This project mixes Java (backend) and JavaScript (frontend), so there's
no single `requirements.txt` like a Python project would have. Instead,
here's everything you need before you start, split by area.

## Tools to install

| Tool | Why | Check with |
|---|---|---|
| JDK 17+ | Run the Spring Boot backend | `java -version` |
| Maven | Build the backend, manage Java dependencies | `mvn -version` |
| Node.js 18+ & npm | Run the React frontend | `node -v` / `npm -v` |
| Git | Version control | `git --version` |
| An IDE | IntelliJ IDEA (backend) + VS Code (frontend) recommended | — |
| Postman (or curl) | Test backend endpoints before building the UI | — |

## Accounts / keys

- **LLM API key** — from Anthropic, OpenAI, or another provider.
  Never commit this key to GitHub. Store it as an environment variable,
  e.g. `LLM_API_KEY`.
- **GitHub account** — to host your repo.
- **(Later) PostgreSQL** — local install or a free-tier hosted DB
  (e.g. Railway, Supabase) for saving review history — only needed
  from Stage 10 onward.
- **(Later) Railway / Render / Vercel account** — only needed when you
  deploy, at the very end.

## Backend dependencies (already listed in `backend/pom.xml`)

- `spring-boot-starter-web` — REST API
- `spring-boot-starter-test` — testing
- (Add later) `spring-boot-starter-data-jpa` + `postgresql` — database
- (Add later) `spring-boot-starter-security` — authentication

## Frontend dependencies (already listed in `frontend/package.json`)

- `react`, `react-dom` — core framework
- `axios` — calling your backend API
- (Add later) `@mui/material` — UI components, once functionality works

## Rule of thumb

Don't install anything from the "Later" rows until the roadmap actually
tells you to. Extra tools you're not using yet just add confusion.
