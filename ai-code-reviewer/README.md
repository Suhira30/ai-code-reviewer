# AI-Powered Code Review & Explanation Assistant

A web app where a user pastes code, picks a language and an action
(review / explain / find bugs / improve / generate tests), and an AI
gives back structured feedback.

See `docs/ROADMAP.md` for the full beginner step-by-step build plan.
See `REQUIREMENTS.md` for what you need installed before you start.

## Folder structure

```
ai-code-reviewer/
├── README.md              <- you are here
├── REQUIREMENTS.md        <- tools/accounts you need before starting
├── docs/
│   └── ROADMAP.md          <- full stage-by-stage build guide
├── backend/                <- Spring Boot app (Java)
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/aicode/reviewer/
│       │   │   ├── controller/   <- REST endpoints (e.g. ReviewController)
│       │   │   ├── service/      <- talks to the LLM API, builds prompts
│       │   │   ├── dto/          <- request/response Java objects
│       │   │   └── config/       <- config classes (e.g. RestTemplate bean)
│       │   └── resources/
│       │       └── application.properties
│       └── test/
│           └── java/com/aicode/reviewer/
└── frontend/                <- React app
    ├── package.json
    ├── public/
    └── src/
        ├── components/      <- reusable UI pieces (CodeInput, ResultCard, etc.)
        ├── pages/           <- full page views (HomePage, etc.)
        └── services/        <- functions that call your backend API
```

## How to start (short version)

1. Open `backend/` in your IDE (IntelliJ recommended), let it install
   Maven dependencies from `pom.xml`.
2. Set your LLM API key as an environment variable (see
   `backend/src/main/resources/application.properties`).
3. Run the Spring Boot app.
4. Open `frontend/` in a terminal, run `npm install` then `npm start`.

Full details are in `docs/ROADMAP.md` — follow it stage by stage,
don't skip ahead.
