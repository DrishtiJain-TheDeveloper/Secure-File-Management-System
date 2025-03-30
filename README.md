# 🔒 Secure File Management System

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Java 17+](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.oracle.com/java/)
![Platform](https://img.shields.io/badge/Platform-Windows%20|%20macOS%20|%20Linux-lightgrey)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1.5-brightgreen.svg)](https://spring.io/projects/spring-boot)

## 📖 Table of Contents
- [Project Description](#-project-description)
- [Key Features](#-key-features)
- [System Architecture](#-system-architecture)
- [Installation](#-installation)
- [Usage](#-usage)
- [API Documentation](#-api-documentation)
- [Technologies Used](#-technologies-used)
- [Screenshots](#-screenshots)
- [Contributing](#-contributing)
- [License](#-license)

## 📖 Project Description
The **Secure File Management System** is a Java-based application that provides:
- Secure file storage with AES-256 encryption
- Role-based access control (RBAC)
- File versioning and audit logging
- RESTful API for integration
- Built with Spring Boot and React

## 🌟 Key Features

### 🔐 Security Features
| Feature | Implementation |
|---------|---------------|
| AES-256 Encryption | Java Cryptography Architecture (JCA) |
| JWT Authentication | Spring Security |
| Password Hashing | Argon2 algorithm |
| Audit Logging | Spring AOP |

### 📂 Core Functionality
- File upload/download with encryption
- File sharing with expiration
- Version history tracking
- Metadata search

## 🏗️ System Architecture

### Backend Structure
mermaid
graph TD
    A[Client] --> B[Spring Security]
    B --> C[Controllers]
    C --> D[Services]
    D --> E[Repositories]
    E --> F[MySQL Database]
Database Schema
mermaid
Copy
erDiagram
    USER ||--o{ FILE : uploads
    USER {
        bigint id PK
        varchar(255) username
        varchar(255) password
        varchar(255) role
    }
    FILE {
        bigint id PK
        varchar(255) name
        blob content
        bigint user_id FK
        timestamp created_at
    }
🛠️ Installation
Prerequisites
Java 17+

Maven 3.8+

MySQL 8.0+

Node.js 16+ (for frontend)

Setup Instructions
bash
Copy
# Clone repository
git clone https://github.com/DrishtiJain-TheDeveloper/Secure-File-Management-System.git
cd Secure-File-Management-System

# Backend setup
cd backend
mvn clean install

# Database configuration
# Create MySQL database and update application.properties

# Frontend setup
cd ../frontend
npm install
🖥️ Usage
Running the Application
bash
Copy
# Start backend
cd backend
mvn spring-boot:run

# Start frontend
cd ../frontend
npm start
Default Credentials
Admin: admin@system.com / Admin@123

User: user@example.com / User@123

📚 API Documentation
Key Endpoints
Endpoint	Method	Description
/api/auth/login	POST	User authentication
/api/files	GET	List user files
/api/files/upload	POST	Upload new file
/api/files/{id}	GET	Download file
View Full API Docs

💻 Technologies Used
Backend
Java 17

Spring Boot 3.1.5

Spring Security

JPA/Hibernate

MySQL

Frontend
React 18

Material-UI

Axios

Security
JWT Authentication

AES-256 Encryption

Argon2 Password Hashing

📷 Screenshots
Login Page
Login Page

File Dashboard
Dashboard

Admin Panel
Admin Panel

🤝 Contributing
Development Workflow
Fork the repository

Create your feature branch:

bash
Copy
git checkout -b feature/new-feature
Commit your changes:

bash
Copy
git commit -m "Add new feature"
Push to the branch:

bash
Copy
git push origin feature/new-feature
Open a pull request

Coding Standards
Follow Google Java Style Guide

Use meaningful variable names

Include Javadoc for public methods

Write unit tests for new features

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
