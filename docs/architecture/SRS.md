# Software Requirement Specification (SRS)

# Housing Society Management System (HSMS)

**Version:** 1.0

**Prepared By:** Chetan Khachane

**Project Type:** Multi-Tenant SaaS Application

**Architecture:** Spring Boot Microservices

**Database:** MongoDB

**Document Status:** Draft

**Last Updated:** 31 July 2026

---

# 1. Introduction

## 1.1 Purpose

The Housing Society Management System (HSMS) is a cloud-based multi-tenant Software-as-a-Service (SaaS) application designed to simplify the management of residential housing societies.

The system enables society administrators and committee members to manage societies, wings, flats, residents, complaints, notices, maintenance, and other day-to-day operations through a centralized platform.

The application is designed using a microservices architecture to ensure scalability, maintainability, and independent deployment of business services.

---

## 1.2 Scope

The system aims to digitize the daily operations of residential housing societies by providing secure and role-based access to different users.

The initial version (MVP) of the application will include:

- User Authentication
- Society Management
- Wing Management
- Flat Management
- Resident Management
- Complaint Management

Future releases will include maintenance billing, visitor management, event management, payment integration, and notification services.
---

# 2. Definitions and Terminology

| Term             | Description |
|------------------|-------------|
| HSMS             | Housing Society Management System |
| SaaS             | Software as a Service |
| Tenant           | An independent housing society registered on the platform with isolated data and users |
| Society          | A residential community registered in the system |
| Wing             | A building or block within a society |
| Flat             | A residential unit within a wing |
| Resident         | A person living in a flat (Owner or Tenant) |
| Committee Member | A society member responsible for management activities |
| Platform Admin   | Administrator responsible for managing the overall SaaS platform |
| JWT              | JSON Web Token used for secure authentication |
| REST API         | HTTP-based communication interface between client and server |
| Microservice     | Independently deployable service responsible for a specific business capability |
---

# 3. Stakeholders and User Roles

## 3.1 Stakeholders

The primary stakeholders of the system are:

- Housing Society
- Society Committee Members
- Residents
- Tenants
- Security Staff
- Maintenance Staff
- Platform Administrator

---

## 3.2 User Roles

The system will support the following user roles:

| Role | Responsibilities |
|------|------------------|
| Platform Admin | Manages the complete SaaS platform, societies, and subscriptions |
| Society Admin | Manages a specific housing society |
| Committee Member | Manages notices, complaints, and society operations |
| Resident | Views profile, raises complaints, receives notices |
| Tenant | Uses resident features with limited permissions |
| Security Guard | Manages visitor entries and exits (Future Module) |
---

# 4. Functional Requirements

The following functional requirements define the core business capabilities of the Housing Society Management System.

| Requirement ID | Requirement | Priority |
|----------------|-------------|----------|
| FR-001 | User Registration | High |
| FR-002 | User Login using JWT Authentication | High |
| FR-003 | Create Housing Society | High |
| FR-004 | Update Housing Society | High |
| FR-005 | Create Wing | High |
| FR-006 | Create Flat | High |
| FR-007 | Register Resident | High |
| FR-008 | Assign Roles to Users | High |
| FR-009 | View Resident Details | High |
| FR-010 | Raise Complaint | High |
| FR-011 | View Complaint Status | High |
| FR-012 | Update Complaint Status | High |
| FR-013 | Publish Notice | Medium |
| FR-014 | View Notices | Medium |
| FR-015 | Create Event | Medium |
| FR-016 | View Events | Medium |
---

# 5. Non-Functional Requirements

The system shall satisfy the following non-functional requirements to ensure performance, reliability, security, and maintainability.

| Requirement ID | Requirement |
|----------------|-------------|
| NFR-001 | The system shall support role-based authentication and authorization using JWT. |
| NFR-002 | API response time should generally be less than 2 seconds under normal load. |
| NFR-003 | All communication between client and server shall use HTTPS in production. |
| NFR-004 | The system shall follow RESTful API design principles. |
| NFR-005 | Each microservice shall own its respective database. |
| NFR-006 | The application shall maintain centralized logging for easier debugging. |
| NFR-007 | All APIs shall validate incoming request data before processing. |
| NFR-008 | The application shall provide meaningful error responses using a standardized response format. |
| NFR-009 | The system shall be containerized using Docker for deployment. |
| NFR-010 | The APIs shall be documented using OpenAPI (Swagger). |
| NFR-011 | The application shall be designed to support horizontal scaling of individual microservices. |
| NFR-012 | The system shall maintain audit information such as created date, modified date, and created by for important entities. |
---

# 6. Assumptions and Constraints

## 6.1 Assumptions

- The application will initially support residential housing societies.
- Each housing society will act as an independent tenant within the SaaS platform.
- Every user will have a unique email address for authentication.
- Internet connectivity is available for accessing the application.
- Society administrators are responsible for maintaining society-specific data.
- JWT will be used for stateless authentication.
- MongoDB will be the primary database for all microservices.

## 6.2 Constraints

- The initial release (MVP) will not include online payment integration.
- Visitor management will be implemented in a future release.
- SMS and Email notification services will not be included in the MVP.
- Mobile applications (Android/iOS) are out of scope for the first release.
- The system will initially support only English.
- Third-party integrations will be introduced in later phases.
- ---

# 6. Assumptions and Constraints

## 6.1 Assumptions

- The application will initially support residential housing societies.
- Each housing society will act as an independent tenant within the SaaS platform.
- Every user will have a unique email address for authentication.
- Internet connectivity is available for accessing the application.
- Society administrators are responsible for maintaining society-specific data.
- JWT will be used for stateless authentication.
- MongoDB will be the primary database for all microservices.

## 6.2 Constraints

- The initial release (MVP) will not include online payment integration.
- Visitor management will be implemented in a future release.
- SMS and Email notification services will not be included in the MVP.
- Mobile applications (Android/iOS) are out of scope for the first release.
- The system will initially support only English.
- Third-party integrations will be introduced in later phases.