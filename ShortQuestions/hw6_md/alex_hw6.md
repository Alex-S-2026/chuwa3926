# HW

- alex_shen
- 2026-3-30
- Homework
- Topics Covered:
    - SQL
    - API
    - HTTP
- Submission: Submit your answers as a single document or code files

# SQL Questions

Install MySQL/PostgreSQL on your computer, setup proper accounts, try SQL queries included in attached two files:
- `SQL_Referential_Integrity (1) (2).sql`
- `SQL_Join (1) (2).sql`

## Q1. Explain Referential Integrity in RDBMS/SQL with sample queries.

Referential integrity is basically the database’s way of making sure foreign keys don’t lie. If `student.dept_id` points at `department`, that value has to either match a real `dept_id` in `department` or be NULL (when the column allows it). We don’t get rows that reference a department or school that doesn’t exist.

In the class script, `department` ties to `school` and `student` ties to `department` with lines like:

```sql
FOREIGN KEY (school_id) REFERENCES school(school_id)
FOREIGN KEY (dept_id) REFERENCES department(dept_id)
```

So if we try to insert a student with a `dept_id` that isn’t in the department table, the insert should fail. Same idea for bad updates. Deletes can also get blocked if we try to remove a parent row that still has children, unless we set up CASCADE or similar—MySQL’s default is usually to just say no.

Examples:

```sql
-- Works if department 101 is already there
INSERT INTO student (student_id, student_name, gender, birth_date, dept_id)
VALUES (3001, 'Test', 'Male', '2000-01-01', 101);

-- Should fail if 999 isn’t a real dept_id
INSERT INTO student (student_id, student_name, gender, birth_date, dept_id)
VALUES (3002, 'Bad', 'Male', '2000-01-01', 999);
```

When we run the homework file, create `school` first, then `department`, then `student`, so the referenced tables exist. Some of the extra inserts in the file are messy on purpose (wrong columns, duplicate keys)—step through them and we’ll see exactly which error the engine throws.

## Q2. Explain Join in RDBMS/SQL with sample queries.

A join is how we pull related tables together instead of staring at three separate result sets. We match on keys—like `student.dept_id` to `department.dept_id`—and only keep the combinations that make sense (depending on inner vs outer join).

The homework file shows the older comma style and the explicit `JOIN` style. They can do the same thing:

```sql
SELECT s.student_name, d.dept_name, sc.school_name
FROM student s, department d, school sc
WHERE s.dept_id = d.dept_id
  AND d.school_id = sc.school_id;
```

```sql
SELECT s.student_name, d.dept_name, sc.school_name
FROM student s
JOIN department d ON s.dept_id = d.dept_id
JOIN school sc ON d.school_id = sc.school_id;
```

I like the second one because the join logic sits in `ON` and we can tack real filters in `WHERE` without mixing everything together.

An inner join keeps only rows that match on both sides. A left join keeps every row from the left table and fills in NULL on the right when there’s no match—handy for students with a NULL `dept_id`. Right join is the same idea flipped. MySQL doesn’t do `FULL OUTER JOIN` in one keyword, so the script fakes it with a `LEFT JOIN ... UNION ... RIGHT JOIN` pattern.

Watch out for listing `FROM student s, department d, school sc` with no join condition: we get every student × every department × every school, which explodes. The assignment contrasts that with a proper join so we can see the difference side by side.

# API HTTP Questions

## Q1. Compare Developer API vs User API

A developer API is built for people writing code: HTTP endpoints, JSON, API keys or OAuth, maybe OpenAPI docs and versioned paths like `/v1/...`. We’re supposed to integrate, automate, or call it from another service.

A “user API” in the assignment sense usually means the product surface normal humans use—screens, buttons, forms—not raw requests. The app might still call APIs under the hood, but the user isn’t typing URLs or managing tokens.

Developers care about breaking changes and deprecation; end users mostly care that the UI still makes sense. Same company often ships both: a public REST API for partners and a web app that talks to internal services the browser never exposes directly.

## Q2. Explain components of REST API?

People say “REST” loosely. In practice we’re looking at resources (things we name in URLs, e.g. `/users/5`), HTTP verbs to say what we’re doing (GET to read, POST to create, etc.), and a body format—almost always JSON now. Requests are supposed to be stateless in the sense that the server shouldn’t need hidden session memory for a single call; we send whatever identity or IDs we need each time (tokens in headers are normal).

Responses use standard status codes so clients don’t have to guess: 200 for OK, 201 when something was created, 4xx for client mistakes, 5xx for server problems. Pagination, filtering, and versioning often show up in query strings or paths. HATEOAS (links inside responses) exists in theory but plenty of “REST” APIs skip it and still work fine day to day.

## Q3. Compare each type of HTTP Method

GET is for reads. It shouldn’t change server state in a meaningful way, and calling it twice should be harmless (safe + idempotent). HEAD is like GET but only returns headers—useful for checking size or existence without downloading a body.

POST is the catch‑all for creating something or kicking off an action. It’s not idempotent: hit it twice, we might create two things. PUT usually replaces a whole resource at a URL; repeating it should leave us in the same end state. PATCH is a partial update; whether it’s strictly idempotent depends how the server implements it. DELETE removes something and is treated as idempotent in spirit (deleting twice shouldn’t blow up).

OPTIONS shows what methods and CORS rules apply; browsers fire it for preflight. So in short: GET/HEAD for safe reads, POST for create/non-idempotent work, PUT/PATCH for updates, DELETE for removal, OPTIONS for discovery.

## Q4. Explain authentication field in http header?

There isn’t one magic header named “Authentication” in every case. The usual one we’ll see is `Authorization`, where the client sends a scheme plus credentials—`Bearer` plus a JWT, or `Basic` with base64-encoded username/password, or something custom the API documents.

When the server rejects us with 401, it may send `WWW-Authenticate` back to tell the client what schemes it accepts. API keys sometimes live in a custom header like `X-API-Key` or, worse, the query string (easy to leak). Session cookies can also prove who we are in a browser, but that’s separate from putting a bearer token in `Authorization`. So when homework says “authentication field,” they usually mean `Authorization` and the related challenge headers—not a single fixed field beyond that pattern.

## Q5. Explain cookies field in http header?

The browser stores small name=value pairs the server asked for. On later requests, the client sends them back in the `Cookie` header for matching domains/paths—think session IDs, “remember me,” or analytics IDs.

The server sets them with `Set-Cookie` on the response, optionally with flags like `HttpOnly` (JS can’t read it easily, helps against some XSS), `Secure` (HTTPS only), `SameSite` (limits cross-site sends), and an expiration.

Cookies are good for web sessions and things the browser should send automatically. They’re not the same as putting `Authorization: Bearer ...` in a script calling an API—different mechanics, sometimes used together on the same site.

## Q6. Explain the purpose of http response headers? Why it is necessary?

The status code and body tell us what happened and the payload, but headers carry the metadata. `Content-Type` tells the client whether to parse JSON, HTML, or something else. Length and transfer encoding tell us how to read the body without cutting off early or waiting forever.

Caching headers (`Cache-Control`, `ETag`, etc.) let browsers and CDNs reuse responses instead of hammering the origin. `Location` tells us where to go on a redirect or where a new resource lives after a 201. `Set-Cookie` establishes session. CORS headers tell the browser whether JavaScript on another origin is allowed to see the response. Security headers push HTTPS, reduce MIME sniffing, or set a content security policy.

Without headers, every client would be guessing encoding, cache behavior, and security rules. They’re part of how HTTP actually works, not optional fluff.
