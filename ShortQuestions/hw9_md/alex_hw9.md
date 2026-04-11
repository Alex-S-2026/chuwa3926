# HW 9

- alex_shen
- 2026-4-10
- Homework 9
- Topics Covered:
    - Testing types (unit → UAT), deployment environments (dev → prod)
- Submission: Submit your answers as a single document or code files

# Conceptual Questions

Explain and compare following concepts, provide specific examples when doing comparison:

## Q1. Testing related:

**How to remember the map:** *Unit* = smallest piece; *Integration* = pieces wired together; *Functional* = “does the feature behave as specified?”; *E2E* = full user journey through real-ish stack; *Regression* = “did we break old stuff?”; *Smoke* = “is it basically alive?”; *Performance* = speed/capacity under expected load; *Stress* = breaking point; *A/B* = product experiment on users; *UAT* = business sign-off.

---

### 1. Unit Testing

- **Purpose:** Prove one function/class/module behaves correctly in isolation (fast feedback, pinpoints bugs).
- **Mechanism:** Call code directly with inputs; assert outputs/side effects; mocks/stubs for dependencies (DB, HTTP, clock).
- **Trade-offs:** Very fast and precise, but can pass while integrations fail; over-mocking can hide real integration bugs.

**Example:** `calculateTax(price, rate)` returns correct cents; `UserValidator` rejects invalid email.

---

### 2. Functional Testing

- **Purpose:** Verify the system meets **functional requirements** (what it should do), often from a black-box perspective.
- **Mechanism:** Drive the app through its public interface (API, UI) with scenarios; compare actual vs expected behavior.
- **Trade-offs:** Closer to user value than raw units, but slower/flakier than unit tests; scope can blur with integration/E2E.

**Example:** “POST `/orders` with valid cart returns 201 and order id”; “Login with wrong password shows error message.”

---

### 3. Integration Testing

- **Purpose:** Verify **multiple components work together** (service + DB, API + message queue, two microservices).
- **Mechanism:** Use real or test doubles for boundaries; often test containers or a shared test DB; fewer mocks than unit tests.
- **Trade-offs:** Catches wiring/schema/contract bugs unit tests miss; slower, more setup, harder to debug than unit tests.

**Example:** Repository saves a row and can read it back; payment service calls sandbox gateway and handles timeout.

---

### 4. Regression Testing

- **Purpose:** Ensure **new changes did not break existing behavior** (safety net over time).
- **Mechanism:** Re-run a curated suite (often automated unit + integration + selected E2E) on every PR or nightly; compare results to baseline.
- **Trade-offs:** Essential for velocity at scale; suite can grow heavy—needs prioritization, parallelization, and flaky-test discipline.

**Example:** After refactoring auth, all old auth tests still green; visual snapshot for checkout page unchanged.

---

### 5. Smoke Testing

- **Purpose:** **Very quick** check that a build is not catastrophically broken before deeper testing or promotion.
- **Mechanism:** Minimal critical path (app starts, health check, login, one core transaction); minutes, not hours.
- **Trade-offs:** Cheap gate, but shallow—passing smoke ≠ quality; choose paths that cover “if this fails, nothing else matters.”

**Example:** After deploy to staging: homepage loads, DB connection OK, “create draft post” works once.

---

### 6. Performance Testing

- **Purpose:** Validate **speed, throughput, latency, resource usage** under **expected** load (SLAs/SLOs).
- **Mechanism:** Load tools (e.g. JMeter, k6); define scenarios (RPS, think time); measure p50/p95/p99, CPU, memory, DB query time.
- **Trade-offs:** Finds bottlenecks before users do; needs representative data and environment or results mislead; not about correctness of business rules.

**Example:** “Under 500 concurrent users, p95 API latency < 200 ms”; “Batch job finishes within 1 hour.”

---

### 7. Stress Testing

- **Purpose:** Find **limits and failure modes** by pushing **beyond** normal load (stability, degradation, recovery).
- **Mechanism:** Ramp traffic until errors/timeouts; may include chaos (kill nodes, disk full); observe backpressure, circuit breakers, autoscaling.
- **Trade-offs:** Reveals breaking point and resilience; can be destructive—use isolated env; different intent than “happy path” performance test.

**Example:** Spike to 10× traffic until queue backs up; verify no data corruption and graceful errors when DB pool exhausted.

---

### 8. A/B Testing

- **Purpose:** **Product/experiment** testing: compare two variants on **real users** to optimize metrics (conversion, retention).
- **Mechanism:** Feature flags + random assignment + metrics; statistical analysis (significance, sample size); guardrails (errors, latency).
- **Trade-offs:** Data-driven decisions; ethical/consent issues, confounding variables, and “peeking” at results too early can mislead—not a substitute for engineering tests.

**Example:** 50% users see new checkout button color; measure completion rate for two weeks.

---

### 9. End-to-End Testing (E2E)

- **Purpose:** Validate **full user journeys** across UI/API, backend, DB, queues—closest to “real user” in automation.
- **Mechanism:** Browser automation (Playwright/Cypress) or multi-step API scripts against staging-like stack; seed test data.
- **Trade-offs:** High confidence for critical paths; slow, brittle (timing, selectors), expensive to maintain—use sparingly on top of unit/integration.

**Example:** “Sign up → verify email → place order → see order in account history” in browser.

---

### 10. User Acceptance Testing (UAT)

- **Purpose:** **Business/customer** confirms the system **meets agreed requirements** and is acceptable to release (“definition of done” from user view).
- **Mechanism:** Scripted scenarios with real stakeholders (or pilot customers) in pre-prod; sign-off checklist; may include legal/compliance checks.
- **Trade-offs:** Catches misunderstanding of requirements early; subjective, manual, scheduling-heavy—not a replacement for automated regression.

**Example:** HR approves payroll export format before go-live; client runs UAT on staging and signs contract milestone.

---

### Quick comparison table (interview sound bite)

| Type | Main question | Scope | Typical speed |
|------|----------------|-------|----------------|
| Unit | Does this small piece work? | One module | Fastest |
| Integration | Do parts connect correctly? | Subsystem | Medium |
| Functional | Does behavior match spec? | Feature/API | Medium–slow |
| E2E | Does the full journey work? | Whole stack | Slowest |
| Regression | Did we break anything old? | Any of above | Depends on suite |
| Smoke | Is this build basically OK? | Critical slice | Very fast |
| Performance | Is it fast enough at expected load? | System under load | Slow (runs) |
| Stress | Where does it break? | Beyond normal load | Slow, risky |
| A/B | Which product variant wins? | Live users + metrics | Ongoing |
| UAT | Will the customer accept it? | Human + scenarios | Manual |

---

## Q2. Environment related:

**How to remember:** Data and risk increase as you move **Dev → QA → Staging → Prod**. Code flows **left to right**; only prod serves real customers with real consequences.

---

### 1. Development

- **Purpose:** Individual engineers integrate and debug **locally or in personal sandboxes** without blocking others.
- **Mechanism:** Local machine, Docker, or personal cloud namespace; often points to dev DB; hot reload; verbose logging; may use fake APIs.
- **Trade-offs:** Fastest iteration, safest to break; least like production (data, scale, config), so “works on my machine” risk.

**Example:** Developer runs Spring Boot + local Postgres with seed script; feature branch deployed to `dev-alex.example.com`.

---

### 2. QA (Quality Assurance)

- **Purpose:** Dedicated place for **testers (and sometimes devs)** to run functional/regression/exploratory tests **before** wider release.
- **Mechanism:** Shared server(s); test data refreshed periodically; bug tracking integrated; may mirror prod topology loosely.
- **Trade-offs:** More stable than personal dev; can still differ from prod; contention if many teams share one QA env.

**Example:** QA team executes test plan for sprint release on `qa.example.com` with anonymized copy of schema.

---

### 3. Pre-prod / Staging

- **Purpose:** **Last rehearsal** before production: integration with real-ish dependencies, final smoke, migrations, runbooks, performance checks.
- **Mechanism:** Prod-like config (feature flags off for unreleased features), similar hardware/scaling, often **prod-like data** (masked) or refreshed subset; may receive blue/green or canary deploy first.
- **Trade-offs:** Best predictor of prod issues; expensive to maintain; if staging drifts from prod, false confidence.

**Example:** Run DB migration on staging, verify E2E payment with sandbox PSP, then promote same artifact to prod.

---

### 4. Production

- **Purpose:** **Live system** serving **real users** and **real data**; source of revenue and reputation.
- **Mechanism:** Strict access, monitoring/alerting, backups, DR, change control, SLOs, incident response; minimal experimentation without guardrails.
- **Trade-offs:** Highest correctness and reliability bar; changes are high-stakes—mitigate with feature flags, canary, rollbacks.

**Example:** `app.example.com` with real PII, billing, and 99.9% uptime target.

---

### Environment comparison (interview sound bite)

| Environment | Primary audience | Data | Risk if broken |
|---------------|------------------|------|----------------|
| Development | Engineers | Fake / local | Low (individual) |
| QA | QA + dev | Test / anonymized | Medium (release delay) |
| Staging | Eng + sometimes stakeholders | Prod-like masked | High (miss prod bug) |
| Production | Customers | Real | Very high (money, trust, legal) |

**Typical promotion flow:** Dev (implement) → QA (validate quality) → Staging (prod dress rehearsal) → Production (controlled release + monitor).

**Trade-off across all:** Fidelity to prod vs cost and speed—more fidelity catches more issues later but costs more to run and keep in sync.



# Programming Questions

## Q3. Write unit test for CommentServiceImpl.java: https://github.com/CTYue/springboot-redbook/blob/10_testing/src/main/java/com/chuwa/redbook/service/impl/CommentServiceImpl.java
- Try to cover as many lines/branches as possible.
- Prove your code coverage using Jacoco Report.