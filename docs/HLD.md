# High Level Design (HLD)

# Housing Society Management System (HSMS)

**Version:** 1.0

**Architecture:** Spring Boot Microservices

**Database:** MongoDB

**Prepared By:** Chetan Khachane

---

# 1. System Overview

The Housing Society Management System (HSMS) is a cloud-native, multi-tenant SaaS application designed to manage residential housing societies.

The application follows a microservices architecture where each business capability is implemented as an independent service.

All client requests pass through an API Gateway before reaching the respective microservice.

Each microservice owns its own database and communicates securely using REST APIs.

---

# 2. High Level Architecture

The system consists of the following major components:

- React Web Application
- API Gateway
- Authentication Service
- Society Service
- Resident Service
- Complaint Service
- Notice Service
- Notification Service
- Config Server
- Eureka Server
- MongoDB
- ---

# 3. Microservice Architecture

The application follows a Domain-Driven Design (DDD) inspired microservices architecture. Each microservice is responsible for a single business capability and owns its own data.

## Microservices

### 1. Auth Service

Responsibilities:
- User Registration
- User Login
- JWT Authentication
- Role Management
- Password Encryption
- Token Validation

Database:
- auth-db

---

### 2. Society Service

Responsibilities:
- Society Management
- Wing Management
- Flat Management

Database:
- society-db

---

### 3. Resident Service

Responsibilities:
- Resident Profile
- Resident Registration
- Flat Allocation
- Resident Search

Database:
- resident-db

---

### 4. Complaint Service

Responsibilities:
- Create Complaint
- Update Complaint Status
- Complaint History
- Complaint Assignment

Database:
- complaint-db

---

### 5. Notice Service

Responsibilities:
- Notice Management
- Event Management
- Announcement Publishing

Database:
- notice-db

---

### 6. Notification Service

Responsibilities:
- Email Notification
- SMS Notification
- Push Notification
- Notification History

Database:
- notification-db
- ---

# 4. Enterprise Architecture Components

The Housing Society Management System follows the Spring Cloud Microservices architecture.

The platform consists of the following infrastructure services.

## API Gateway

Responsibilities:

- Single entry point for all client requests
- Request routing
- Authentication validation
- Cross-cutting concerns
- Rate limiting (Future)
- Logging
- CORS configuration

---

## Eureka Server

Responsibilities:

- Service Discovery
- Dynamic service registration
- Service lookup
- Health monitoring

---

## Config Server

Responsibilities:

- Centralized configuration management
- Environment-specific properties
- Configuration refresh

---

## MongoDB

Responsibilities:

- Persistent data storage
- Independent database per microservice
- ---

# 5. Request Flow

## User Login Flow

1. User enters email and password in the React Web Application.
2. The request is sent to the API Gateway.
3. API Gateway forwards the request to the Auth Service.
4. Auth Service validates user credentials.
5. JWT token is generated upon successful authentication.
6. JWT token is returned to the client.
7. Client stores the JWT securely.
8. All subsequent requests include the JWT in the Authorization header.

---

## Complaint Creation Flow

1. Resident logs into the system.
2. Resident submits a complaint.
3. Request is routed through the API Gateway.
4. Complaint Service validates the request.
5. Complaint is stored in the Complaint Database.
6. Notification Service is informed (initially through REST; later this can be replaced with an event-driven approach).
7. Committee members can view and update the complaint status.

---

## Notice Publishing Flow

1. Society Admin logs into the system.
2. Admin creates a notice.
3. Notice Service stores the notice.
4. Notification Service notifies residents.
5. Residents can view notices from the dashboard.


# 6. Deployment Architecture

The application is designed as a cloud-native microservices platform.

## Deployment Components

- React Web Application
- API Gateway
- Eureka Server
- Config Server
- Auth Service
- Society Service
- Resident Service
- Complaint Service
- Notice Service
- Notification Service
- MongoDB
- Docker

Initially, all services will run locally using Docker Compose during development. The architecture is designed to support deployment on cloud platforms in the future.

---

# 7. Technology Stack

| Layer | Technology |
|--------|------------|
| Programming Language | Java 21 LTS |
| Backend Framework | Spring Boot 4.1.0 |
| Build Tool | Maven |
| Database | MongoDB |
| Security | Spring Security + JWT |
| Service Discovery | Eureka |
| API Gateway | Spring Cloud Gateway |
| Configuration | Spring Cloud Config |
| API Documentation | OpenAPI (Swagger) |
| Object Mapping | MapStruct |
| Boilerplate Reduction | Lombok |
| Testing | JUnit 5 + Mockito |
| Containerization | Docker |
| Version Control | Git & GitHub |
| IDE | IntelliJ IDEA |

---

# 8. Design Principles

The application follows the following software engineering principles.

- Single Responsibility Principle (SRP)
- Open/Closed Principle (OCP)
- Dependency Injection
- Domain-Driven Design (DDD) inspired architecture
- Database per Service
- Role-Based Access Control (RBAC)
- Stateless Authentication using JWT
- RESTful API Design
- Separation of Concerns
- Clean Code Practices

---

# 9. Conclusion

The proposed architecture provides a scalable, maintainable, and modular foundation for the Housing Society Management System.

The system is designed to support future enhancements such as payment integration, visitor management, mobile applications, and event-driven communication without requiring significant architectural changes.