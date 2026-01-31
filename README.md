# FinTrack - Personal Finance Management System

## 🚀 Project Overview

FinTrack is a full-stack personal finance management application built with Spring Boot (backend) and Flutter (frontend). Track income, expenses, debts, and achieve financial goals.

## 🛠️ Tech Stack

**Backend:**
- Java 17
- Spring Boot 3.2.2
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Maven

## ✨ Features Implemented

### Authentication Module ✅
- User registration with email validation
- User login with JWT token authentication
- Password encryption using BCrypt
- Protected API endpoints
- Token-based stateless authentication

## 📡 API Endpoints

### Public Endpoints (No authentication required)
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/health` - Health check

### Protected Endpoints (JWT token required)
- `GET /api/users/me` - Get current user profile

## 🏗️ Architecture
```
┌─────────────────┐
│   Controller    │  ← REST API Layer
└────────┬────────┘
         │
┌────────▼────────┐
│    Service      │  ← Business Logic
└────────┬────────┘
         │
┌────────▼────────┐
│   Repository    │  ← Data Access Layer
└────────┬────────┘
         │
┌────────▼────────┐
│   PostgreSQL    │  ← Database
└─────────────────┘
```

## 🔒 Security Features

- BCrypt password hashing
- JWT token-based authentication
- CSRF protection disabled (stateless API)
- Generic error messages (prevent username enumeration)
- Token expiration (24 hours)
- Entity-DTO separation (never expose passwords)

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 15+

### Installation

1. Clone the repository
```bash
git clone https://github.com/YOUR_USERNAME/fintrack-backend.git
cd fintrack-backend
```

2. Create PostgreSQL database
```sql
CREATE DATABASE fintrack_db;
CREATE USER fintrack_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE fintrack_db TO fintrack_user;
```

3. Update `application.properties`
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fintrack_db
spring.datasource.username=fintrack_user
spring.datasource.password=your_password
jwt.secret=your_secret_key_minimum_256_bits
```

4. Run the application
```bash
mvn spring-boot:run
```

Application runs on: `http://localhost:8080`

## 📝 API Documentation

### Register User
```bash
POST /api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe"
}
```

### Login
```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "SecurePass123!"
}
```

Response:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "type": "Bearer",
    "expiresIn": 86400000,
    "user": { ... }
  }
}
```

### Get Current User (Protected)
```bash
GET /api/users/me
Authorization: Bearer <jwt-token>
```

## 🎯 Roadmap

- [x] Authentication Module
- [ ] Bank Accounts Management
- [ ] Transactions & Categories
- [ ] Debt Tracking & EMI Calculator
- [ ] Budget Management
- [ ] Financial Reports & Analytics
- [ ] Flutter Mobile App

## 👨‍💻 Author

**Sabari**
- Learning full-stack development (Spring Boot + Flutter)
- Building enterprise-level applications
- [GitHub](https://github.com/YOUR_USERNAME)

## 📄 License

This project is for educational purposes.

---

⭐ Star this repo if you find it helpful!

### Environment Variables Setup

1. Copy the example environment file:
```bash
cp .env.example .env
```

2. Update `.env` with your actual values:
```env
DB_URL=jdbc:postgresql://localhost:5432/fintrack_db
DB_USERNAME=fintrack_admin
DB_PASSWORD=YOUR_ACTUAL_PASSWORD
JWT_SECRET=YOUR_ACTUAL_SECRET_KEY_MINIMUM_256_BITS
```

**⚠️ IMPORTANT:** Never commit `.env` to Git! It's already in `.gitignore`.

### Running Locally

**Option 1: Using IntelliJ with EnvFile plugin**
- Install EnvFile plugin
- Add `.env` file to Run Configuration
- Run application

**Option 2: Using command line**
```bash
# Load environment variables and run
export $(cat .env | xargs) && mvn spring-boot:run
```