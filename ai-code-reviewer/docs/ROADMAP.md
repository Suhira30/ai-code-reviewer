# AI-Powered Code Review & Explanation Assistant — Beginner Roadmap

A step-by-step plan, broken into small stages. Do NOT skip ahead. Each stage should feel "easy" before you move to the next one. If a stage feels hard, that's the signal to slow down, not speed up.

---

## STAGE 0 — Before you write any code (Day 1)

Goal: know exactly what you're building, in one sentence.

> "A web app where a user pastes code, picks a language and an action (review/explain/test), and an AI gives back structured feedback."

**Tasks:**
- [ ] Write that sentence down somewhere (README.md in a new folder).
- [ ] Create a GitHub repo called something like `ai-code-reviewer`.
- [ ] Get an API key from an LLM provider (Anthropic Claude, OpenAI, etc.) — just sign up, generate a key, save it somewhere safe (never commit it to GitHub).

**Why this matters:** you're not "starting from zero" — you already know Java/Spring. The only *new* skill is talking to an LLM API. Everything else is stuff you've done before.

---

## STAGE 1 — The "hello world" of LLM calls (Day 1–2)

Goal: prove you can send text to an LLM and get text back — nothing else.

**Tasks:**
- [ ] Create a brand-new, tiny Spring Boot project (Spring Initializr: Web dependency only).
- [ ] Write ONE endpoint: `POST /api/test` that takes a plain string and forwards it to the LLM API using `RestTemplate` or `WebClient`.
- [ ] Print the raw LLM response to the console.
- [ ] Test it with Postman or curl — not the frontend yet.

**Definition of done:** you type "Say hello in French" into Postman, and you see the LLM's answer in your terminal.

Don't build anything else until this works. This is the riskiest, most unfamiliar part — get it out of the way first.

---

## STAGE 2 — Turn it into a real "review code" prompt (Day 2–3)

Goal: instead of sending raw user text, build a proper prompt behind the scenes.

**Tasks:**
- [ ] Create a `PromptBuilder` class in Java.
- [ ] It takes the user's code + language, and wraps it in a structured instruction (the "You are an experienced software engineer..." prompt from your notes).
- [ ] Send that constructed prompt to the LLM instead of raw text.
- [ ] Just print the response for now — still no structured JSON, still no frontend.

**Definition of done:** you paste in a buggy Java method, and you get back a paragraph pointing out the bug.

---

## STAGE 3 — Structured output (Day 3–5)

Goal: get the LLM to return JSON you can actually use in Java, not just prose.

**Tasks:**
- [ ] Update your prompt to explicitly say: "Respond ONLY with valid JSON matching this schema: {...}" and give the exact shape you want (score, issues[], improvements[]).
- [ ] Create Java DTO classes matching that JSON shape (`ReviewResult`, `Issue`, etc.).
- [ ] Use Jackson (`ObjectMapper`) to parse the LLM's JSON string into those Java objects.
- [ ] Handle the case where the LLM adds extra text around the JSON (strip it, or ask again).

**Definition of done:** your endpoint returns a proper Java object / JSON response, not a giant string.

This is the single most valuable skill in the whole project — getting AI output into a shape your code can trust.

---

## STAGE 4 — Minimal frontend (Day 5–7)

Goal: a plain page where you paste code and see the result. Ugly is fine.

**Tasks:**
- [ ] Create a React app (`create-react-app` or Vite).
- [ ] One textarea for code input, one dropdown for language, one button "Review".
- [ ] On click, call your Spring Boot API and display the returned issues as a simple list.
- [ ] Don't worry about MUI/styling yet — just get data flowing end to end.

**Definition of done:** you can go from "paste code in browser" → "see AI feedback in browser" without touching Postman.

---

## STAGE 5 — Version 2: multiple languages (Week 2)

**Tasks:**
- [ ] Add a language dropdown (Java, Python, JS, C, C++, SQL).
- [ ] Make `PromptBuilder` adjust its instructions per language (e.g. mention language-specific issues like Python's GIL, or SQL injection for SQL).
- [ ] Pass the selected language to the backend and into the prompt.

---

## STAGE 6 — Version 3: multiple actions (Week 2)

**Tasks:**
- [ ] Add radio buttons: Review / Explain / Find Bugs / Improve / Generate Tests.
- [ ] Create a different prompt template per action (you already have the "review" one; write "explain", "bugs only", etc.).
- [ ] Backend picks the right template based on the action selected.

---

## STAGE 7 — Version 4: test generation (Week 3)

**Tasks:**
- [ ] Add a prompt template that asks the LLM to output test code (JUnit/PyTest/Jest depending on language).
- [ ] Display the generated test in a code block on the frontend (a `<pre><code>` block is enough at first — syntax highlighting can come later).

---

## STAGE 8 — Version 5: code health dashboard (Week 3–4)

**Tasks:**
- [ ] Your JSON schema already has a `score` and `issues` with categories — use that data to render a simple dashboard: a big score number + counts per category (bugs, security, performance, etc.).
- [ ] This is pure frontend work at this point — no new AI skills needed, just displaying data you already have.

---

## STAGE 9 — The "why" feature (Week 4)

**Tasks:**
- [ ] Extend your JSON schema so every issue has `description`, `why`, `risk`, and `recommendation` fields instead of just `description`.
- [ ] Update the prompt to ask for this richer structure.
- [ ] Display it nicely on the frontend (expandable cards work well).

---

## STAGE 10 — Polish (ongoing, whenever you have time)

- [ ] Swap MUI in for basic styling.
- [ ] Add PostgreSQL to save review history (a `reviews` table: id, code, language, action, result_json, created_at).
- [ ] Add Spring Security/JWT so users can log in and see their own history.
- [ ] Deploy backend (Railway/Render) and frontend (Vercel).

---

## A few beginner-friendly rules to keep you sane

1. **One stage at a time.** Don't add the frontend until the backend logic works in Postman. Don't add multiple languages until one language works perfectly.
2. **Commit after every stage.** Small git commits = easy to roll back when something breaks.
3. **It's OK if the LLM sometimes returns bad JSON.** This happens to everyone — you'll build small safety nets (retry, or ask the model to "return ONLY JSON, no explanation") as you go.
4. **You don't need RAG or agents for this project.** Those are good *next* projects once this one is solid — don't let them distract you now.
5. **When stuck, isolate the failure.** Is it the prompt? The API call? The JSON parsing? The frontend fetch? Test each piece alone before assuming the whole thing is broken.

---

### Suggested order to actually start today
1. Spring Initializr → new project → Web dependency.
2. Get your LLM API key.
3. Build the one `/api/test` endpoint from Stage 1.
4. Get it working in Postman.
5. Stop for the day. That's a real, complete win.
