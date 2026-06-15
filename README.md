# 🏦 Digital Banking Platform

Full-stack digital banking application built with **Spring Boot 3** and **Angular 17**.

## Features

* JWT Authentication & Authorization
* Customer Management
* Current & Saving Accounts
* Credit, Debit & Transfer Operations
* Dashboard & Analytics
* AI Assistant (Spring AI RAG)
* Telegram Bot Integration
* Swagger/OpenAPI Documentation

## Tech Stack

### Backend

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* JWT
* Spring AI

### Frontend

* Angular 17
* TypeScript
* Chart.js

### Database

* H2 (Development)
* MySQL (Production)

## Architecture

```text
Angular 17
    │
    ▼
Spring Boot REST API
    │
    ├── JWT Security
    ├── Spring AI (RAG)
    └── JPA/Hibernate
    │
    ▼
H2 / MySQL
```

## Run

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm start
```

## Author

**Nassim KHATIB**
EMSI Casablanca – 2025/2026
