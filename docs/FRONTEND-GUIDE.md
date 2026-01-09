# EthAum Frontend Development Guide

## Overview
Complete API documentation for frontend developers to build the EthAum platform.

---

## Services & Ports

| Service | Port | Description |
|---------|------|-------------|
| Auth Service | 8081 | User registration, login, JWT |
| Startup Service | 8082 | Startup profile management |
| Launch Service | 8083 | Product launches & upvotes |
| Review Service | 8084 | Enterprise reviews & ratings |
| Credibility Service | 8085 | Trust score calculation |
| Deal Service | 8086 | Pilot/POC marketplace |

---

## Tech Stack (Recommended)
- React 18+ / Next.js
- React Router DOM
- Axios (API calls)
- GSAP (animations)
- Tailwind CSS

---

## 1️⃣ AUTH SERVICE (Port 8081)

### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "Pass@123",
  "role": "STARTUP"  // or "ENTERPRISE"
}
```
**Response:** `"User registered successfully"`

### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "Pass@123"
}
```
**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### JWT Token Structure
```javascript
// Decode: JSON.parse(atob(token.split('.')[1]))
{
  "sub": "john@example.com",  // email
  "role": "STARTUP",          // STARTUP | ENTERPRISE
  "iat": 1767953741,
  "exp": 1768040141
}
```

---

## 2️⃣ STARTUP SERVICE (Port 8082)

### Create Startup Profile
```http
POST /startups
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "My Startup",
  "website": "https://mystartup.com",
  "industry": "FinTech",
  "fundingStage": "Series A",
  "arrRange": "1M-5M",
  "description": "AI-powered fintech solution"
}
```
**Response:**
```json
{
  "id": 1,
  "name": "My Startup",
  "website": "https://mystartup.com",
  "industry": "FinTech",
  "fundingStage": "Series A",
  "arrRange": "1M-5M",
  "description": "AI-powered fintech solution"
}
```

### Get My Startup
```http
GET /startups/me
Authorization: Bearer <token>
```

---

## 3️⃣ LAUNCH SERVICE (Port 8083)

### Launch a Product
```http
POST /launches
Authorization: Bearer <token>
Content-Type: application/json

{
  "startupId": 1,
  "productName": "EthAum AI",
  "tagline": "AI-powered marketplace",
  "description": "Connect startups with enterprises",
  "category": "AI/ML"
}
```
**Response:**
```json
{
  "id": 1,
  "productName": "EthAum AI",
  "tagline": "AI-powered marketplace",
  "category": "AI/ML",
  "upvotes": 0
}
```

### Upvote a Launch
```http
POST /launches/{id}/upvote
```
**No body required, no auth required**

### Get Trending Launches
```http
GET /launches/trending
```
**Response:**
```json
[
  {
    "id": 1,
    "productName": "EthAum AI",
    "tagline": "AI-powered marketplace",
    "category": "AI/ML",
    "upvotes": 15
  }
]
```

---

## 4️⃣ REVIEW SERVICE (Port 8084)

### Add Review (Enterprise only)
```http
POST /reviews
Authorization: Bearer <token>
Content-Type: application/json

{
  "launchId": 1,
  "rating": 5,
  "comment": "Amazing product!",
  "reviewerRole": "CTO",
  "companySize": "ENTERPRISE"
}
```
**Response:**
```json
{
  "rating": 5,
  "comment": "Amazing product!",
  "reviewerRole": "CTO",
  "companySize": "ENTERPRISE"
}
```

### Get Reviews for Launch
```http
GET /reviews/launch/{launchId}
```
**Response:**
```json
[
  {
    "rating": 5,
    "comment": "Amazing product!",
    "reviewerRole": "CTO",
    "companySize": "ENTERPRISE"
  }
]
```

**Reviewer Roles:** `CTO`, `CXO`, `MANAGER`, `VP`
**Company Sizes:** `SMB`, `MID`, `ENTERPRISE`

---

## 5️⃣ CREDIBILITY SERVICE (Port 8085)

### Calculate Credibility Score
```http
POST /credibility/calculate?startupId=1&launchUpvotes=10&avgRating=4.5&enterpriseReviews=3
```
**Response:**
```json
{
  "startupId": 1,
  "score": 93.0,
  "level": "Enterprise Ready",
  "badge": "Enterprise Ready"
}
```

### Get Credibility Score
```http
GET /credibility/startup/{startupId}
```

**Score Formula:**
```
score = (launchUpvotes × 0.3) + (avgRating × 10) + (enterpriseReviews × 15)
```

**Levels & Badges:**
| Score | Level | Badge |
|-------|-------|-------|
| 0-30 | Emerging | EthAum Verified |
| 31-60 | Growing | High Growth Startup |
| 61-100 | Enterprise Ready | Enterprise Ready |

---

## 6️⃣ DEAL SERVICE (Port 8086)

### Create Deal/Pilot (STARTUP only)
```http
POST /deals
Authorization: Bearer <token>
Content-Type: application/json

{
  "startupId": 1,
  "title": "Free 30-Day Pilot",
  "description": "Try our AI solution free",
  "targetIndustry": "FinTech",
  "durationDays": 30,
  "maxSlots": 5
}
```
**Response:**
```json
{
  "id": 1,
  "title": "Free 30-Day Pilot",
  "status": "OPEN",
  "durationDays": 30,
  "maxSlots": 5
}
```

### Get Open Deals
```http
GET /deals/open
```

### Apply to Deal (ENTERPRISE only)
```http
POST /deals/{id}/apply
Authorization: Bearer <token>
```

### Accept Application (STARTUP only)
```http
POST /deals/{dealId}/applications/{appId}/accept
Authorization: Bearer <token>
```

**Deal Status:** `OPEN`, `CLOSED`
**Application Status:** `APPLIED`, `ACCEPTED`, `REJECTED`

---

## Authentication Flow

```
┌──────────┐     ┌─────────┐     ┌─────────────┐
│ Register │ --> │  Login  │ --> │  Dashboard  │
└──────────┘     └─────────┘     └─────────────┘
                      │
                      ▼
               Store JWT Token
               localStorage
                      │
                      ▼
               Decode for role
               STARTUP | ENTERPRISE
```

---

## Role-Based Access

| Feature | STARTUP | ENTERPRISE |
|---------|---------|------------|
| Create Startup Profile | ✅ | ❌ |
| Launch Product | ✅ | ❌ |
| Add Review | ❌ | ✅ |
| Create Deal/Pilot | ✅ | ❌ |
| Apply to Deal | ❌ | ✅ |
| Accept Application | ✅ | ❌ |
| Upvote Launch | ✅ | ✅ |
| View Trending | ✅ | ✅ |

---

## Axios Setup

```javascript
import axios from 'axios';

// Service instances
const authApi = axios.create({ baseURL: 'http://localhost:8081' });
const startupApi = axios.create({ baseURL: 'http://localhost:8082' });
const launchApi = axios.create({ baseURL: 'http://localhost:8083' });
const reviewApi = axios.create({ baseURL: 'http://localhost:8084' });
const credibilityApi = axios.create({ baseURL: 'http://localhost:8085' });
const dealApi = axios.create({ baseURL: 'http://localhost:8086' });

// Auth interceptor
const addAuthInterceptor = (api) => {
  api.interceptors.request.use(config => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
};

[startupApi, launchApi, reviewApi, dealApi].forEach(addAuthInterceptor);
```

---

## Pages to Build

### Public Pages
- `/` - Landing page
- `/login` - Login form
- `/register` - Registration with role selection
- `/trending` - Trending launches
- `/deals` - Open pilot deals

### Protected Pages (Auth Required)
- `/profile` - User profile
- `/dashboard` - Role-based dashboard
- `/startup/create` - Create startup (STARTUP only)
- `/launch/create` - Launch product (STARTUP only)
- `/deals/create` - Create pilot deal (STARTUP only)
- `/review/add` - Add review (ENTERPRISE only)

---

## UI Components

### Role Badge
```jsx
const RoleBadge = ({ role }) => (
  <span className={role === 'STARTUP' ? 'bg-purple-500' : 'bg-blue-500'}>
    {role === 'STARTUP' ? '🚀 Startup' : '🏢 Enterprise'}
  </span>
);
```

### Credibility Badge
```jsx
const CredibilityBadge = ({ level, badge }) => {
  const colors = {
    'Emerging': 'bg-gray-500',
    'Growing': 'bg-yellow-500',
    'Enterprise Ready': 'bg-green-500'
  };
  return <span className={colors[level]}>{badge}</span>;
};
```

### Deal Status
```jsx
const DealStatus = ({ status }) => (
  <span className={status === 'OPEN' ? 'text-green-500' : 'text-red-500'}>
    {status}
  </span>
);
```

---

## Error Handling

```javascript
try {
  const response = await authApi.post('/auth/login', credentials);
  // success
} catch (error) {
  if (error.response?.status === 403) {
    // Invalid credentials
  } else if (error.response?.status === 400) {
    // Validation error
  } else {
    // Server error
  }
}
```

---

## Theme Support

**Dark Theme (Default):**
```css
--bg-primary: #000000;
--bg-card: #1a1a1a;
--text-primary: #ffffff;
--accent: linear-gradient(45deg, #405DE6, #833AB4, #E1306C);
```

**Light Theme:**
```css
--bg-primary: #f0f2f5;
--bg-card: #ffffff;
--text-primary: #000000;
```

---

## Complete User Flow

### Startup Journey
```
Register (STARTUP) → Login → Create Startup Profile → Launch Product 
→ Get Upvotes → Get Reviews → Credibility Score → Create Pilot Deal 
→ Accept Enterprise Applications
```

### Enterprise Journey
```
Register (ENTERPRISE) → Login → Browse Trending Launches 
→ Add Reviews → Browse Open Deals → Apply to Pilots
```

---

## Checklist

### Auth
- [ ] Register page with role selection
- [ ] Login page
- [ ] JWT storage & decoding
- [ ] Protected routes
- [ ] Logout functionality

### Startup Features
- [ ] Create startup profile
- [ ] Launch product
- [ ] View my startup
- [ ] Create pilot deal
- [ ] Accept applications

### Enterprise Features
- [ ] Browse launches
- [ ] Add reviews
- [ ] Browse deals
- [ ] Apply to pilots

### Common Features
- [ ] Trending launches page
- [ ] Upvote functionality
- [ ] Credibility display
- [ ] Open deals listing
- [ ] Dark/Light theme
- [ ] Responsive design
- [ ] Loading states
- [ ] Error handling
