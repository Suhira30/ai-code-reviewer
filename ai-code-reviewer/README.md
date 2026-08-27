# AI-Powered Code Review & Explanation Assistant

A modern full-stack web application built with **Java 25 (Spring Boot)** and **React 18 (Vite)** that provides structured, educational code reviews, explanations, bug findings, and unit test generation using the **Google Gemini LLM API**.

---

## 🌟 Key Architectural Practices & Features Built

### 🤖 1. Advanced LLM & Prompt Engineering Practices
* **Native System Instructions (`system_instruction`):** Implemented Google Cloud's official `system_instruction` payload pattern in `LlmService.java` and `PromptBuilder.java`. This isolates persona rules, audit guidelines, and JSON schemas from untrusted user code snippets, preventing prompt injection attacks.
* **API-Level Structured JSON (`responseMimeType: "application/json"`):** Configured `generationConfig` in `LlmService.java` to enforce JSON generation at the Gemini model decoder level. Guarantees 100% type-safe JSON output without conversational markdown fences (` ```json `).
* **Language-Aware & Action-Aware Audit Engine:** Specialized prompt instructions for **Java** (null checks, try-with-resources), **Python** (PEP 8, type hints, GIL), **JavaScript** (async/await error handling), and **SQL** (injection & missing indexes).

### 🛡️ 2. Resilient Backend Architecture (Java 25 & Spring Boot)
* **Zero-Crash Exception Architecture:** Global `@ExceptionHandler` in `ReviewController.java` guarantees that network timeouts, API quota issues, or invalid keys return user-friendly alert banners instead of raw HTTP 500 internal server errors.
* **Robust Jackson Deserialization:** Annotated `ReviewResult.java` DTO with `@JsonIgnoreProperties(ignoreUnknown = true)` and disabled `FAIL_ON_UNKNOWN_PROPERTIES` to handle unexpected fields gracefully.

### 🎨 3. Modern Frontend & UX Features (React + Vite)
* **Vite Development Server:** Lightning-fast 100ms server startup and JSX module bundling.
* **Dark & Light Mode Switcher:** Seamless CSS variable theme engine (`#0D1117` Dark vs. `#F6F8FA` Light) with choice persisted in `localStorage`.
* **Recent Review History Drawer:** Saves your last 5 code reviews in `localStorage` with 1-click reload and score badges.
* **1-Click Sample Snippet Loaders:** Quick-test sample snippets for **Java**, **Python**, and **SQL**.
* **Keyboard Hotkey (`Ctrl + Enter`):** Submit code for review directly from your keyboard.
* **Real-time Code Statistics:** Live calculation of Lines of Code (LOC), word count, and character limit indicators.
* **Individual Copy Fix Buttons:** Single-click copy buttons for every suggested code fix card and generated unit test suite.

---

## 📁 Project Folder Structure

```
ai-code-reviewer/
├── README.md                           <- You are here
├── REQUIREMENTS.md                     <- Prerequisites & environment keys
├── docs/
│   ├── PRD_AI_Code_Reviewer.md         <- Product Requirement Document
│   ├── DESIGN_BRIEF_AI_Code_Reviewer.md<- UI/UX Design System & Tokens
│   └── ROADMAP.md                      <- Full stage-by-stage build guide
├── backend/                            <- Java 25 / Spring Boot 3.3 Backend
│   ├── pom.xml                         <- Maven configuration (Java 25)
│   └── src/main/java/com/aicode/reviewer/
│       ├── controller/                 <- REST API controllers (ReviewController, TestController)
│       ├── service/                    <- PromptBuilder & LlmService (Gemini API caller)
│       ├── dto/                        <- ReviewRequest & ReviewResult JSON DTOs
│       └── resources/
│           └── application.properties  <- Gemini endpoint & API key config
└── frontend/                           <- React 18 / Vite Frontend
    ├── package.json
    ├── vite.config.js                  <- Vite configuration
    ├── index.html                      <- Entry point HTML
    └── src/
        ├── App.jsx                     <- Main split-screen IDE workspace
        ├── App.css                     <- Theme tokens (Dark/Light mode) & card styles
        └── services/
            └── reviewService.js        <- Axios REST API client
```

---

## 🚀 How to Run the Application

### 1. Start the Backend (Spring Boot)
Ensure you have Java 25 installed and configured:
```cmd
cd backend
mvn spring-boot:run
```
*(Runs REST API on `http://localhost:8080`)*

### 2. Start the Frontend (React + Vite)
Open a separate terminal:
```cmd
cd frontend
npm install
npm start
```
*(Opens web interface on `http://localhost:3000`)*
