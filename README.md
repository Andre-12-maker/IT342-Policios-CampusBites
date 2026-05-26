# 🍽️ CampusBites — Campus Food Ordering System

A multi-platform food ordering system for campus environments, built with Spring Boot, React, and Android (Kotlin).

---

## 📌 Project Info

| Detail | Info |
|---|---|
| Course | IT342 — Software Engineering |
| Project | CampusBites |
| Branch | `feature/vertical-slice-refactor` |
| Architecture | Vertical Slice Architecture |

---

## 🛠️ Tech Stack

| Platform | Technology |
|---|---|
| Backend | Java 17 · Spring Boot 3.5 · MongoDB Atlas |
| Web Frontend | React 18 · Vite · React Router |
| Mobile | Android · Kotlin · OkHttp · Gson |

---

## 🏗️ Project Structure (Vertical Slice Architecture)

### Backend

backend/src/main/java/edu/cit/policios/campusbites/
├── features/
│   ├── auth/          # User registration & login
│   ├── food/          # Food menu & categories
│   └── order/         # Order placement & history
├── shared/
│   └── security/      # Spring Security config
└── CampusbitesApplication.java

### Web Frontend

web/src/
├── features/
│   ├── auth/          # Login & Register pages
│   ├── food/          # Food display & filtering
│   ├── home/          # Home page, header, menu
│   └── order/         # Cart & Place Order pages
└── shared/
├── assets/        # Images & static data
├── components/    # Navbar (shared)
└── context/       # Global cart state

### Mobile (Android)

mobile/app/src/main/java/com/cit/policios/campusbites/
├── features/
│   ├── auth/          # Login & Register activities
│   └── home/          # Home & category browsing
└── shared/
└── network/       # API client

---

## ✅ Features

- 👤 User Registration & Login (BCrypt password hashing)
- 🍕 Browse Food Menu by Category
- 🛒 Add/Remove Items from Cart
- 📦 Place Orders with Delivery Address
- 📋 View Order History
- 📱 Mobile Login & Registration (Android)
- 🔐 Spring Security authentication

---

## 🚀 Getting Started

### Backend
```bash
cd backend
./mvnw spring-boot:run
```
Runs on `http://localhost:8080`

### Web Frontend
```bash
cd web
npm install
npm run dev
```
Runs on `http://localhost:5173`

### Mobile
Open `mobile/` folder in Android Studio and run on emulator or device.

---

## 🧪 Running Tests

```bash
cd backend
./mvnw clean test
```

Expected output:
Tests run: 17, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS

### Test Coverage

| Test Class | Tests | Status |
|---|---|---|
| AuthServiceTest | 5 | ✅ PASS |
| FoodServiceTest | 6 | ✅ PASS |
| OrderServiceTest | 6 | ✅ PASS |
| **Total** | **17** | ✅ **ALL PASS** |

---

## 🔀 Git Branches

| Branch | Purpose |
|---|---|
| `main` | Stable production code |
| `feature/vertical-slice-refactor` | Vertical slice refactoring + tests |
| `backend` | Backend development |
| `web` | Web frontend development |
| `mobile` | Mobile development |

---

## 📡 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login user |
| GET | `/api/food` | Get all food items |
| GET | `/api/food/category/{category}` | Get food by category |
| GET | `/api/food/{id}` | Get food by ID |
| POST | `/api/food` | Create food item |
| POST | `/api/orders` | Place new order |
| GET | `/api/orders/user/{userId}` | Get orders by user |
| GET | `/api/orders/{id}` | Get order by ID |
| PATCH | `/api/orders/{id}/status` | Update order status |

---

## 👥 Contributors

- **Andre-12-maker** — Full Stack Developer

---

## 📄 License

This project is for educational purposes — IT342 Software Engineering.
