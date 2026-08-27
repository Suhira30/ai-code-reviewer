import React, { useState, useEffect } from "react";
import { reviewCode } from "./services/reviewService";
import "./App.css";

const SAMPLES = {
  Java: `public class Calculator {
    public int divide(int a, int b) {
        // Potential Division by Zero bug
        return a / b;
    }
    
    public void printLength(String str) {
        // Potential NullPointerException
        System.out.println(str.length());
    }
}`,
  Python: `def fetch_user_data(user_id, cursor):
    # Potential SQL Injection vulnerability
    query = f"SELECT * FROM users WHERE id = '{user_id}'"
    cursor.execute(query)
    return cursor.fetchone()`,
  SQL: `SELECT * FROM orders WHERE status = 'PENDING';
-- Missing index on status column, potential full table scan`
};

function App() {
  const [theme, setTheme] = useState(() => localStorage.getItem("theme") || "dark");
  const [code, setCode] = useState("");
  const [language, setLanguage] = useState("Java");
  const [action, setAction] = useState("Review");
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [activeFilter, setActiveFilter] = useState("All");
  const [expandedIssueIndex, setExpandedIssueIndex] = useState(0);
  const [copiedGenerated, setCopiedGenerated] = useState(false);
  const [copiedFixIndex, setCopiedFixIndex] = useState(null);
  const [history, setHistory] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("review_history")) || [];
    } catch {
      return [];
    }
  });

  useEffect(() => {
    document.documentElement.setAttribute("data-theme", theme);
    localStorage.setItem("theme", theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme((prev) => (prev === "dark" ? "light" : "dark"));
  };

  const handleReview = async () => {
    if (!code.trim()) {
      setError("Please paste or write some code to review.");
      return;
    }

    setError(null);
    setLoading(true);
    setResult(null);

    try {
      const data = await reviewCode(code, language, action);
      setResult(data);
      if (data.issues && data.issues.length > 0) {
        setExpandedIssueIndex(0);
      }

      // Save to local history
      const newEntry = {
        id: Date.now(),
        time: new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }),
        language,
        action,
        code,
        result: data
      };
      const updatedHistory = [newEntry, ...history.slice(0, 4)];
      setHistory(updatedHistory);
      localStorage.setItem("review_history", JSON.stringify(updatedHistory));
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Failed to communicate with AI review service. Make sure backend is running on http://localhost:8080."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleLoadHistory = (item) => {
    setCode(item.code);
    setLanguage(item.language);
    setAction(item.action);
    setResult(item.result);
    setError(null);
    if (item.result?.issues?.length > 0) {
      setExpandedIssueIndex(0);
    }
  };

  const handleClearHistory = () => {
    setHistory([]);
    localStorage.removeItem("review_history");
  };

  const handleKeyDown = (e) => {
    if ((e.ctrlKey || e.metaKey) && e.key === "Enter") {
      e.preventDefault();
      if (!loading && code.trim()) {
        handleReview();
      }
    }
  };

  const handleClear = () => {
    setCode("");
    setResult(null);
    setError(null);
  };

  const handleLoadSample = (lang) => {
    setLanguage(lang);
    setCode(SAMPLES[lang] || "");
    setError(null);
  };

  const handleCopyGeneratedCode = () => {
    if (result?.generatedCode) {
      navigator.clipboard.writeText(result.generatedCode);
      setCopiedGenerated(true);
      setTimeout(() => setCopiedGenerated(false), 2000);
    }
  };

  const handleCopyFix = (fixText, idx) => {
    navigator.clipboard.writeText(fixText);
    setCopiedFixIndex(idx);
    setTimeout(() => setCopiedFixIndex(null), 2000);
  };

  const filteredIssues =
    result?.issues?.filter(
      (issue) => activeFilter === "All" || issue.category === activeFilter
    ) || [];

  const getScoreColor = (score) => {
    if (score >= 80) return "green";
    if (score >= 60) return "yellow";
    return "red";
  };

  const lineCount = code ? code.split("\n").length : 0;
  const wordCount = code ? code.trim().split(/\s+/).filter(Boolean).length : 0;

  return (
    <div className="app-container">
      {/* Header */}
      <header className="app-header">
        <div className="logo-area">
          <span className="logo-icon">⚡</span>
          <span className="logo-text">AI Code Reviewer</span>
          <span className="badge">Java 25 & Spring Boot</span>
        </div>
        <div className="header-actions">
          <button className="theme-toggle-btn" onClick={toggleTheme}>
            {theme === "dark" ? "☀️ Light Mode" : "🌙 Dark Mode"}
          </button>
        </div>
      </header>

      {/* Main Workbench: 2-Column Split Screen */}
      <main className="main-workbench">
        {/* LEFT PANEL: Workspace */}
        <section className="workspace-panel">
          <div className="panel-title">
            <span>Workspace</span>
            <div style={{ display: "flex", gap: 6 }}>
              <span className="form-label" style={{ alignSelf: "center", marginRight: 4 }}>Samples:</span>
              <button className="chip" onClick={() => handleLoadSample("Java")}>Java</button>
              <button className="chip" onClick={() => handleLoadSample("Python")}>Python</button>
              <button className="chip" onClick={() => handleLoadSample("SQL")}>SQL</button>
            </div>
          </div>

          <div className="controls-row">
            <div className="form-group">
              <label className="form-label">Language</label>
              <select
                className="select-input"
                value={language}
                onChange={(e) => setLanguage(e.target.value)}
              >
                <option value="Java">Java (JDK 25)</option>
                <option value="Python">Python 3</option>
                <option value="JavaScript">JavaScript (Node.js)</option>
                <option value="SQL">SQL</option>
                <option value="C++">C++</option>
              </select>
            </div>

            <div className="form-group">
              <label className="form-label">Action</label>
              <div className="radio-group">
                {["Review", "Explain", "Find Bugs", "Generate Tests"].map((act) => (
                  <button
                    key={act}
                    className={`radio-btn ${action === act ? "active" : ""}`}
                    onClick={() => setAction(act)}
                  >
                    {act}
                  </button>
                ))}
              </div>
            </div>
          </div>

          <div className="editor-container">
            <textarea
              className="code-textarea"
              placeholder="// Paste your code snippet here... (Press Ctrl + Enter to submit)"
              value={code}
              onChange={(e) => setCode(e.target.value)}
              onKeyDown={handleKeyDown}
              maxLength={5000}
            />
            <div className="editor-footer">
              <span className="char-counter">
                {lineCount} lines | {wordCount} words | {code.length}/5000 chars
              </span>
              <div className="action-buttons">
                <button className="btn btn-secondary" onClick={handleClear}>
                  Clear
                </button>
                <button
                  className="btn btn-primary"
                  onClick={handleReview}
                  disabled={loading || !code.trim()}
                >
                  {loading ? "Analyzing Code..." : "Review Code"}
                </button>
              </div>
            </div>
          </div>
        </section>

        {/* RIGHT PANEL: Results Dashboard */}
        <section className="results-panel">
          <div className="panel-title">
            <span>Review Results</span>
            {history.length > 0 && (
              <button className="chip" onClick={handleClearHistory} style={{ fontSize: 11 }}>
                Clear History
              </button>
            )}
          </div>

          {/* History Bar */}
          {history.length > 0 && (
            <div className="history-bar">
              <span className="form-label" style={{ alignSelf: "center" }}>Recent:</span>
              {history.map((item) => (
                <button
                  key={item.id}
                  className="history-item"
                  onClick={() => handleLoadHistory(item)}
                >
                  <span>{item.language} ({item.action})</span>
                  <span className={`severity-badge ${getScoreColor(item.result.score) === 'green' ? 'Info' : getScoreColor(item.result.score) === 'yellow' ? 'Warning' : 'Critical'}`}>
                    {item.result.score}/100
                  </span>
                </button>
              ))}
            </div>
          )}

          {error && (
            <div
              className="callout-box"
              style={{
                backgroundColor: "var(--critical-bg)",
                borderColor: "var(--critical-main)",
                color: "var(--critical-main)",
              }}
            >
              <strong>Error:</strong> {error}
            </div>
          )}

          {loading && (
            <div style={{ display: "flex", flexDirection: "column", gap: 12 }}>
              <div className="shimmer-card" />
              <div className="shimmer-card" />
              <div className="shimmer-card" />
            </div>
          )}

          {!loading && !result && !error && (
            <div className="empty-state">
              <span className="empty-icon">🔍</span>
              <h3>No Code Analyzed Yet</h3>
              <p>
                Paste code or click a <strong>Sample Snippet</strong> above, select your target
                action, and click <strong>"Review Code"</strong> (or press <code>Ctrl + Enter</code>).
              </p>
            </div>
          )}

          {!loading && result && (
            <>
              {/* Score & Summary Card */}
              <div className="score-card">
                <div className={`score-circle ${getScoreColor(result.score)}`}>
                  {result.score}
                </div>
                <div className="score-info">
                  <span className="score-title">
                    Overall Code Quality Score: {result.score}/100
                  </span>
                  <span className="score-summary">{result.summary}</span>
                </div>
              </div>

              {/* Generated Code Output (e.g. Unit Tests or Explanation) */}
              {result.generatedCode && (
                <div style={{ marginTop: 12 }}>
                  <div
                    style={{
                      display: "flex",
                      justifyContent: "space-between",
                      alignItems: "center",
                      marginBottom: 8,
                    }}
                  >
                    <strong>Generated Output / Unit Tests</strong>
                    <button className="btn btn-secondary" onClick={handleCopyGeneratedCode}>
                      {copiedGenerated ? "Copied! ✓" : "Copy Code"}
                    </button>
                  </div>
                  <pre className="code-fix-block">{result.generatedCode}</pre>
                </div>
              )}

              {/* Issue Category Filters */}
              {result.issues && result.issues.length > 0 && (
                <>
                  <div className="filter-chips">
                    {["All", "Bug", "Security", "Performance", "Style"].map((cat) => (
                      <button
                        key={cat}
                        className={`chip ${activeFilter === cat ? "active" : ""}`}
                        onClick={() => setActiveFilter(cat)}
                      >
                        {cat}
                      </button>
                    ))}
                  </div>

                  {/* Issues List Accordions */}
                  <div className="issues-list">
                    {filteredIssues.map((issue, idx) => (
                      <div key={idx} className="issue-card">
                        <div
                          className="issue-header"
                          onClick={() =>
                            setExpandedIssueIndex(
                              expandedIssueIndex === idx ? null : idx
                            )
                          }
                        >
                          <div className="issue-header-left">
                            <span className={`severity-badge ${issue.severity}`}>
                              {issue.severity}
                            </span>
                            <span className="issue-title">
                              {issue.title || issue.description}
                            </span>
                          </div>
                          <span>{expandedIssueIndex === idx ? "▲" : "▼"}</span>
                        </div>

                        {expandedIssueIndex === idx && (
                          <div className="issue-body">
                            <p>
                              <strong>Description:</strong> {issue.description}
                            </p>
                            {issue.why && (
                              <div className="callout-box">
                                <div className="callout-title">💡 Why this matters</div>
                                <div>{issue.why}</div>
                              </div>
                            )}
                            {issue.risk && (
                              <p>
                                <strong>Risk:</strong> {issue.risk}
                              </p>
                            )}
                            {issue.suggestion && (
                              <div>
                                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 6 }}>
                                  <strong>Suggested Fix:</strong>
                                  <button
                                    className="btn btn-secondary"
                                    style={{ padding: "2px 8px", fontSize: 11 }}
                                    onClick={() => handleCopyFix(issue.suggestion, idx)}
                                  >
                                    {copiedFixIndex === idx ? "Copied! ✓" : "Copy Fix"}
                                  </button>
                                </div>
                                <pre className="code-fix-block">
                                  {issue.suggestion}
                                </pre>
                              </div>
                            )}
                          </div>
                        )}
                      </div>
                    ))}
                  </div>
                </>
              )}
            </>
          )}
        </section>
      </main>
    </div>
  );
}

export default App;
