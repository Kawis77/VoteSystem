# 🗳️ Voting System API

RESTful API for managing elections, candidates, voters and votes.

### 🚀 Key Features
- REST API built with Spring Boot
- Simple homepage with link to Swagger UI
- OpenAPI (Swagger) documentation available at `/swagger-ui.html`
- Unit tests using JUnit 5 and Mockito
- Global exception handling with domain-specific exceptions
- Clean architecture (Controller -> Service -> Repository)
- Docker and Docker Compose support for easy startup

---


## 🐳 Run with Docker

Start the application (8081 port) with:

```bash
docker-compose up --build
```

---

## 🧠 Business Rules
- One voter can cast only one vote per election (enforced in service and by DB unique constraint)
- A blocked voter cannot vote
- Votes can be cast only when election status is `OPEN`
- Candidate must belong to the same election where vote is cast
- Candidate cannot be moved to another election after creation



