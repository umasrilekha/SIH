# GovMesh — Food, Civil Supplies & Consumer Protection Department (Department 2)
## Phase 1 — Department Foundation

This application represents **Department 2 (Food, Civil Supplies & Consumer Protection Department - Government of Maharashtra)** in the GovMesh SIH 2026 interoperability ecosystem demonstration.

> **SIMULATED DEPARTMENT SYSTEM**  
> This system is built for the **GovMesh SIH 2026 Interoperability Framework Demonstration**. It provides an internal government officer portal backed by a Spring Boot REST API and PostgreSQL database architecture.

---

## 🏛️ Architectural Context

```
Citizen App
    ↓
API Gateway / BFF
    ↓
Identity + Consent + Compliance
    ↓
Workflow Orchestrator
    ↓
Interoperability Core
    ↓
Integration Router
    ↓
REST / SOAP / Legacy Departments (Dept 2: Food, Civil Supplies & Consumer Protection)
```

In the larger GovMesh architecture, Department 2 represents a legacy-style department system that will later expose **SOAP / XML** interfaces (Phase 3). Phase 1 establishes the departmental foundation: relational database models, role-based security, audit logging, system health monitors, and a realistic internal officer portal UI.

---

## 🛠️ Tech Stack

### Frontend
- **Framework**: React 18 + TypeScript + Vite
- **Styling**: Tailwind CSS (Government portal visual hierarchy & palette)
- **Icons**: Lucide React
- **Routing**: React Router v6
- **HTTP Client**: Axios with JWT Bearer Interceptors

### Backend
- **Framework**: Java 17/25 + Spring Boot 3
- **Security**: Spring Security + JWT Authentication (30-min session expiry) + BCrypt password encoding
- **Persistence**: Spring Data JPA + Hibernate
- **Database**: PostgreSQL 16 (Auto-configuring database initializer with demo records)

---

## 🔐 Phase 1 User Roles & Demo Credentials

| Role | Username | Password | Permissions |
|---|---|---|---|
| **Food Supply Officer** | `food.officer` | `Food@123` | View dashboard, search ration records, inspect applications |
| **Senior Officer** | `senior.officer` | `Senior@123` | All officer permissions + operational overview |
| **Department Admin** | `food.admin` | `Admin@123` | All permissions + department administration placeholder |
| **Auditor** | `auditor` | `Auditor@123` | Dashboard, ration records, audit logs (Read-Only) |

---

## 🗄️ Database Data Models

1. **`users`**: Officer user accounts (`id`, `username`, `password_hash`, `full_name`, `role`, `department`, `employee_id`, `is_active`, `created_at`, `updated_at`).
2. **`ration_records`**: Legacy master ration cards (`id`, `ration_card_no`, `holder_name`, `house_address`, `taluka_code`, `district_code`, `verification_flag`, `update_status`, `created_at`, `updated_at`).
3. **`applications`**: Interoperability update requests (`id`, `application_id`, `citizen_reference`, `ration_card_no`, `application_type`, `current_status`, `source_department`, `created_at`, `updated_at`).
4. **`audit_logs`**: System audit trail (`id`, `timestamp`, `application_id`, `officer_id`, `action`, `result`, `description`).
5. **`notifications`**: Officer notifications (`id`, `recipient_user_id`, `title`, `message`, `type`, `is_read`, `created_at`).

---

## 🔌 Backend REST APIs

### Authentication
- `POST /api/auth/login` — Officer authentication and JWT token generation
- `GET /api/auth/me` — Fetch current officer session profile

### Dashboard
- `GET /api/dashboard/summary` — Operational counts computed dynamically from DB
- `GET /api/dashboard/recent-applications` — Latest 5 incoming requests
- `GET /api/dashboard/recent-activity` — Recent audit log events

### Applications & Ration Records
- `GET /api/applications` — Query incoming requests with status & type filters
- `GET /api/applications/{id}` — View application details linked to legacy Ration Record
- `GET /api/ration-records` — Search Ration Card records by card number, holder name, district, or taluka
- `GET /api/ration-records/{id}` — Fetch specific Ration Record details

### Audit & System Health
- `GET /api/audit-logs` — Read-only audit log entries with filters
- `GET /api/notifications` — Retrieve officer notification feed
- `PATCH /api/notifications/{id}/read` — Mark notification as read
- `GET /api/system-health` — Infrastructure health monitor (DB, Auth, App Service: **OPERATIONAL**; SOAP & GovMesh: **NOT CONFIGURED**)

---

## 🚀 How to Run the Application

### Option A: Local Development (Recommended)

#### 1. Database & Backend Setup
```bash
cd food-department/backend

# Compile and start Spring Boot backend on port 8081
# Seeds 10 users, 20 ration records, 15 applications, 20 audit logs, 10 notifications on startup automatically
.\mvnw.cmd spring-boot:run
```

#### 2. Frontend Setup
```bash
cd food-department/frontend

# Install dependencies
npm install

# Start Vite React dev server on http://localhost:3000
npm run dev
```

Open `http://localhost:3000` in your browser and click any pre-populated demo account button to log in.

---

### Option B: Docker Compose

```bash
cd food-department
docker-compose up --build
```
- Frontend: `http://localhost:3000`
- Backend REST API: `http://localhost:8081`
- PostgreSQL: `localhost:5432`

---

## 🚫 Intentionally Not Implemented in Phase 1

As per specification, the following are scheduled for later phases:
- SOAP / XML / WSDL interfaces (Phase 3)
- GovMesh Workflow Orchestrator integration
- REST integration with Revenue Department
- Consent validation & canonical schema transformation
- Conflict detection & idempotency handlers
- Officer approval/rejection workflows (Phase 2)
- Real citizen Aadhaar/OTP integration

---

## 🎯 Phase 2 Next Step

The architecture and clean separation built in Phase 1 prepare the project for **Phase 2 — Ration Record Management** (implementing officer address update approval/rejection workflows against `ration_records`).
