# ðŸ½ï¸ CampusBites â€” Campus Food Ordering System

A campus food ordering system built with Spring Boot, React, and Android (Kotlin).

---

## ðŸ“Œ Project Info

| Detail | Info |
|---|---|
| Course | IT342 â€” Software Engineering |
| Project | CampusBites |
| Repository | `backend/`, `web/`, `mobile/` |

---

## ðŸ› ï¸ Tech Stack

| Platform | Technology |
|---|---|
| Backend | Java 17 Â· Spring Boot 3.5.11 Â· Spring Data MongoDB Â· Spring Security |
| Web Frontend | React 19.2.0 Â· Vite 7.3.1 Â· React Router DOM 7.15.1 |
| Mobile | Android Â· Kotlin Â· OkHttp 4.11.0 Â· Gson 2.10.1 |

---

## ðŸš€ Getting Started

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

## ðŸ§ª Running Tests

```bash
cd backend
./mvnw clean test
```

---

## ðŸ‘¥ Contributors

- **Andre-12-maker** â€” Full Stack Developer

---

## ðŸ“„ License

This project is for educational purposes â€” IT342 System Integration and Architecture.

