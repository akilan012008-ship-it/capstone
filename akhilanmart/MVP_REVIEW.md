# Akhilan Mart — Capstone MVP Architecture & Viva Review Guide

**Project Identifier:** `akhilanmart`  
**Display Name:** Akhilan Mart  

---

## 1. Architectural Architecture Flow

```
Browser (JSP / Vanilla JS)
   ↓
Servlet / Controller (AuthFilter, Route Handlers)
   ↓
Service Layer (AuthService, ProductService, CartService, OrderService, ReviewService, UserService)
   ↓
DAO Layer (UserDAO, ProductDAO, CartDAO, OrderDAO, ReviewDAO)
   ↓
HikariCP Connection Pool
   ↓
H2 Database (In-Memory / File Auto-Created & Seeded)
```

---

## 2. Key Technical Highlights for Viva Defense

1. **Layered Architecture & Separation of Concerns:**
   - Controllers only handle HTTP requests, session state, and JSP forwarding.
   - Business rules, input validations, and stock boundary checks reside strictly inside the Service layer.
   - Database operations use PreparedStatements inside the DAO layer.

2. **Transactional Order Checkout (ACID Compliance):**
   - Implemented in `OrderService.placeOrder(buyerId)`.
   - Disables `autoCommit` on JDBC connection, inserts order, inserts order items, reduces product stock, clears user cart, and commits.
   - If stock is insufficient or database insertion fails, executes immediate `rollback()`.

3. **jBCrypt Security:**
   - Passwords are never stored in plain text.
   - Uses `BCrypt.hashpw(password, BCrypt.gensalt(10))` during registration and seed initialization.
   - Verified during login via `BCrypt.checkpw()`.

4. **Zero Manual Setup Database Seeding:**
   - `AppServletContextListener` initializes HikariCP and executes `DatabaseInitializer` during application startup.
   - Auto-creates database schema and seeds Admin (`admin@akhilanmart.com`), Buyers, Sellers, and 20 products across 5 categories.

5. **Security & Ownership Validation:**
   - Public registration for `ADMIN` role is strictly disabled.
   - Sellers can only edit or delete their own product listings (`sellerId` verification in `ProductService`).
   - Only verified buyers who have purchased a product can post product reviews.
