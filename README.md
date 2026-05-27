# 🍽️ CampusBites — Campus Food Ordering System

A campus food ordering system built with Spring Boot, React, and Android (Kotlin).

---

## 📌 Project Info

| Detail | Info |
|---|---|
| Course | IT342 — Software Engineering |
| Project | CampusBites |
| Repository | `backend/`, `web/`, `mobile/` |

---

## 🛠️ Tech Stack

| Platform | Technology |
|---|---|
| Backend | Java 17 · Spring Boot 3.5.11 · Spring Data MongoDB · Spring Security |
| Web Frontend | React 19.2.0 · Vite 7.3.1 · React Router DOM 7.15.1 |
| Mobile | Android · Kotlin · OkHttp 4.11.0 · Gson 2.10.1 |

---

## 🚀 Getting Started

### Prerequisites

- Java 17
- Node.js 20+ and npm
- Android Studio for the mobile app
- A working internet connection for MongoDB Atlas and npm packages

### Backend
```bash
cd backend
# macOS / Linux
./mvnw spring-boot:run
# Windows
./mvnw.cmd spring-boot:run
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
Open the `mobile/` folder in Android Studio and run the app on an emulator or device.

### Notes

- The backend connects to MongoDB Atlas via `backend/src/main/resources/application.properties`.
- If you want to use a different MongoDB instance, update the `spring.data.mongodb.uri` value.
- On Windows, use `./mvnw.cmd` for Maven wrapper commands.

---

## 🧪 Running Tests

```bash
cd backend
./mvnw clean test
```

---

## 👥 Contributors

- **Andre-12-maker** — Full Stack Developer

---

## 📄 License

This project is for educational purposes — IT342 System Integration and Architecture.
