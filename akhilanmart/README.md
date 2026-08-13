# Akhilan Mart — Centralized Multi-Seller E-Commerce Marketplace MVP

**Project Identifier:** `akhilanmart`  
**Target Environment:** Java 17+, Apache Tomcat 9, H2 Database (HikariCP Connection Pool), jBCrypt Security

---

## 1. Project Overview

**Akhilan Mart** solves the challenges faced by independent sellers and online shoppers by delivering a centralized, robust multi-seller e-commerce marketplace platform. Independent sellers can list products, manage inventory stock, and track orders containing their products. Buyers can discover items across five categories (Accessories, Books, Clothing, Electronics, Home), filter by search terms or categories, add items to a shopping cart, and place orders through an ACID-compliant transactional checkout.

---

## 2. Technical Stack

- **Frontend:** JSP, JSTL, HTML5, CSS3 ("Midnight Marketplace" dark theme), Vanilla JavaScript, Fetch API / AJAX.
- **Backend:** Java 17, Java Servlets (`javax.servlet`), JDBC with HikariCP connection pooling, layered architecture (Servlet → Service → DAO → JDBC → H2).
- **Database:** Embedded H2 Database (Auto-created and auto-seeded on first run).
- **Security:** jBCrypt password hashing (`BCrypt.hashpw`), PreparedStatements (SQL Injection protection), `AuthFilter` (Session & Role authorization).
- **Build System:** Maven WAR Packaging.

---

## 3. Demo Credentials

The database is auto-seeded on first startup with demo accounts and realistic marketplace products:

| Role | Email | Password | Access Rights |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@akhilanmart.com` | `Admin@123` | Full governance, User management, Listing moderation, Global orders |
| **Buyer** | `buyer@akhilanmart.com` | `Buyer@123` | Product browsing, Shopping cart, ACID checkout, Reviews |
| **Seller** | `seller@akhilanmart.com` | `Seller@123` | Seller dashboard, Create/Edit/Delete listings, Order status updates |

---

## 4. Local Execution & Build Instructions

### Prerequisites
- Java JDK 17 or higher
- Apache Maven 3.8+
- Apache Tomcat 9.0+ or Docker

### Build Command
Run the following command inside `akhilanmart/` directory:
```bash
mvn clean verify
```
This compiles source files, runs all JUnit 5 test suites, and builds the deployable `target/akhilanmart.war` package.

---

## 5. Docker Deployment

To build and run using Docker Compose:
```bash
docker compose up --build
```
Access the application in your web browser at:
`http://localhost:8080`

---

## 6. Render Cloud Deployment Instructions

1. Log into your Render dashboard.
2. Create a new **Web Service** and connect your GitHub repository containing the `akhilanmart` source.
3. Select **Docker** environment runtime.
4. Set Build Context to `./` and Dockerfile Path to `Dockerfile`.
5. Deploy Web Service. Render will build the multi-stage Docker image and launch Tomcat on port `8080`.
