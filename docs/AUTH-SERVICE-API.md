# Auth Service API Documentation

## Overview
Auth Service handles user registration, login, and JWT token generation for the EthAum platform.

**Base URL:** `http://localhost:8081`

---

## Endpoints

### 1. Register User
Creates a new user account.

**URL:** `POST /auth/register`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "yourpassword",
  "role": "STARTUP"
}
```

**Fields:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| email | string | Yes | Valid email address (unique) |
| password | string | Yes | User password (min 1 char) |
| role | string | Yes | Either `STARTUP` or `ENTERPRISE` |

**Success Response:** `200 OK`
```
User registered successfully
```

**Error Response:** `400 Bad Request`
```
Email already registered
```

---

### 2. Login User
Authenticates user and returns JWT token.

**URL:** `POST /auth/login`

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "yourpassword"
}
```

**Fields:**
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| email | string | Yes | Registered email |
| password | string | Yes | User password |

**Success Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6IlNUQVJUVVAiLCJpYXQiOjE3MDk..."
}
```

**Error Response:** `401 Unauthorized`
```
Bad credentials
```

---

## JWT Token Structure

The JWT token contains:
```json
{
  "sub": "user@example.com",    // User email
  "role": "STARTUP",            // User role (STARTUP/ENTERPRISE)
  "iat": 1709123456,            // Issued at timestamp
  "exp": 1709209856             // Expiration (24 hours)
}
```

**Token Usage:**
Include in Authorization header for protected routes:
```
Authorization: Bearer <token>
```

---

## User Roles

| Role | Description |
|------|-------------|
| `STARTUP` | Startup founders looking for enterprise partners |
| `ENTERPRISE` | Enterprise companies looking for startup collaborations |
| `ADMIN` | Platform administrators (internal use) |

---

## Database Schema

**Table: `users`**
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | Primary Key, Auto Increment |
| email | VARCHAR | Unique, Not Null |
| password | VARCHAR | Not Null (BCrypt hashed) |
| role | VARCHAR | Not Null (ENUM) |
| created_at | TIMESTAMP | |

---

## Error Codes

| Status | Description |
|--------|-------------|
| 200 | Success |
| 400 | Bad Request (validation error) |
| 401 | Unauthorized (invalid credentials) |
| 500 | Internal Server Error |

---

## Example cURL Commands

**Register:**
```bash
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"test123","role":"STARTUP"}'
```

**Login:**
```bash
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"test123"}'
```
