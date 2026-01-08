# Frontend Development Guide

## Overview
This guide helps frontend developers understand what to build for the EthAum platform.

---

## Tech Stack (Recommended)
- React 18+
- React Router DOM
- Axios (API calls)
- GSAP (animations)
- Three.js / React Three Fiber (3D background - optional)

---

## Pages to Build

### 1. Landing Page (`/`)
**Purpose:** Welcome page for visitors

**Components:**
- Hero section with tagline
- Features section (For Startups, For Enterprises, Collaborate)
- CTA buttons (Get Started, Sign In)

**Behavior:**
- If logged in → Show "Go to Profile" button
- If not logged in → Show "Get Started" and "Sign In" buttons

---

### 2. Register Page (`/register`)
**Purpose:** New user registration

**Form Fields:**
| Field | Type | Validation |
|-------|------|------------|
| Role Selection | Radio/Toggle | Required (STARTUP or ENTERPRISE) |
| Email | email input | Required, valid email format |
| Password | password input | Required, min 6 chars recommended |

**UI Elements:**
- Role selector (two buttons: 🚀 Startup, 🏢 Enterprise)
- Email input
- Password input
- "Create Account" button
- Link to Login page

**API Call:**
```javascript
POST /auth/register
Body: { email, password, role }
```

**On Success:** Redirect to `/login`
**On Error:** Show error message, shake animation

---

### 3. Login Page (`/login`)
**Purpose:** User authentication

**Form Fields:**
| Field | Type | Validation |
|-------|------|------------|
| Email | email input | Required |
| Password | password input | Required |

**UI Elements:**
- Email input
- Password input
- "Sign In" button
- Link to Register page

**API Call:**
```javascript
POST /auth/login
Body: { email, password }
Response: { token }
```

**On Success:** 
1. Store token in localStorage
2. Decode JWT to get user info
3. Redirect to `/profile`

**On Error:** Show error message, shake animation

---

### 4. Profile Page (`/profile`)
**Purpose:** User profile display (LinkedIn-style)

**Layout:**
```
┌─────────────────────────────────────┐
│         Cover Photo (gradient)       │
├─────────────────────────────────────┤
│ ┌───┐                               │
│ │ A │  Username                     │
│ └───┘  🚀 STARTUP badge             │
│                                     │
│ 📧 email  📍 location  📅 joined    │
├─────────────────────────────────────┤
│  Connections │ Projects │ Collabs   │
│      0       │    0     │    0      │
├─────────────────────────────────────┤
│ About                               │
│ Bio text here...                    │
└─────────────────────────────────────┘
```

**Data from JWT Token:**
- `sub` → email
- `role` → STARTUP or ENTERPRISE

**Protected Route:** Redirect to `/login` if no token

---

### 5. Navbar (Component)
**Elements:**
- Logo (left)
- Navigation links (center/right)
- Theme toggle button
- User avatar (when logged in)

**States:**
| Logged Out | Logged In |
|------------|-----------|
| Login link | Home link |
| Register button | Profile link |
| | Logout button |
| | User avatar |

---

## Authentication Flow

```
┌─────────┐     ┌─────────┐     ┌─────────┐
│ Register │ --> │  Login  │ --> │ Profile │
└─────────┘     └─────────┘     └─────────┘
                     │
                     ▼
              Store JWT Token
              in localStorage
                     │
                     ▼
              Decode token to
              get user info
```

---

## State Management

### Auth Context
```javascript
{
  user: { email, role } | null,
  token: string | null,
  isAuthenticated: boolean,
  login: (token) => void,
  logout: () => void
}
```

### Theme Context
```javascript
{
  isDark: boolean,
  toggleTheme: () => void
}
```

---

## JWT Token Handling

**Store Token:**
```javascript
localStorage.setItem('token', token);
```

**Decode Token:**
```javascript
const payload = JSON.parse(atob(token.split('.')[1]));
// payload = { sub: "email", role: "STARTUP", iat: ..., exp: ... }
```

**Check Expiry:**
```javascript
const isExpired = payload.exp * 1000 < Date.now();
```

**Logout:**
```javascript
localStorage.removeItem('token');
```

---

## API Integration

**Base URL:** `http://localhost:8081`

**Axios Setup:**
```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8081',
  headers: { 'Content-Type': 'application/json' }
});

// Add token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

---

## Theme Support

**Dark Theme (Default):**
- Background: #000000
- Card: #1a1a1a
- Text: #ffffff
- Accent: Instagram gradient (#405DE6 → #833AB4 → #E1306C)

**Light Theme:**
- Background: #f0f2f5
- Card: #ffffff
- Text: #000000

---

## Animations (GSAP)

**Page Enter:**
```javascript
gsap.from(element, { y: 40, opacity: 0, duration: 0.6 });
```

**Error Shake:**
```javascript
gsap.to(element, { x: [-8, 8, -8, 8, 0], duration: 0.4 });
```

**Stagger Children:**
```javascript
gsap.from(children, { y: 30, opacity: 0, stagger: 0.1 });
```

---

## Folder Structure (Recommended)

```
src/
├── components/
│   ├── Navbar.jsx
│   ├── ThemeToggle.jsx
│   └── ThreeBackground.jsx (optional)
├── context/
│   ├── AuthContext.jsx
│   └── ThemeContext.jsx
├── pages/
│   ├── Home.jsx
│   ├── Login.jsx
│   ├── Register.jsx
│   └── Profile.jsx
├── App.jsx
├── main.jsx
└── index.css
```

---

## Checklist

- [ ] Landing page with hero section
- [ ] Register page with role selection
- [ ] Login page with form
- [ ] Profile page (LinkedIn-style)
- [ ] Navbar with auth state
- [ ] Dark/Light theme toggle
- [ ] JWT token storage & decoding
- [ ] Protected routes
- [ ] GSAP animations
- [ ] Error handling with visual feedback
- [ ] Responsive design
