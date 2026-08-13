<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Administrator Dashboard - Akhilan Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
</head>
<body>

    <!-- NAVBAR -->
    <nav class="navbar">
        <div class="container nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <span class="brand-akhilan">Akhilan</span><span class="brand-mart">Mart</span>
            </a>
            <ul class="nav-links">
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link active">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link">User Accounts</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products" class="nav-link">Product Listings</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/orders" class="nav-link">Marketplace Orders</a></li>
            </ul>
            <div class="nav-actions">
                <span class="user-greeting">Admin: <strong>${sessionScope.user.name}</strong></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
            </div>
        </div>
    </nav>

    <!-- MAIN DASHBOARD -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <div class="dashboard-header">
            <div>
                <h1 class="dashboard-title">Marketplace Administration</h1>
                <p style="color: var(--text-secondary);">Central governance, metrics analytics, and management tools</p>
            </div>
        </div>

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="flash-message flash-success">
                ✅ ${sessionScope.flashSuccess}
            </div>
            <c:remove var="flashSuccess" scope="session" />
        </c:if>

        <!-- SUMMARY CARDS -->
        <div class="metrics-grid">
            <div class="metric-card">
                <div class="metric-info">
                    <p>Total Registered Users</p>
                    <div class="metric-number">${totalUsers}</div>
                </div>
                <div class="metric-icon">👥</div>
            </div>

            <div class="metric-card">
                <div class="metric-info">
                    <p>Buyer Accounts</p>
                    <div class="metric-number">${totalBuyers}</div>
                </div>
                <div class="metric-icon">🛍️</div>
            </div>

            <div class="metric-card">
                <div class="metric-info">
                    <p>Seller Accounts</p>
                    <div class="metric-number">${totalSellers}</div>
                </div>
                <div class="metric-icon">🏪</div>
            </div>

            <div class="metric-card">
                <div class="metric-info">
                    <p>Marketplace Products</p>
                    <div class="metric-number">${totalProducts}</div>
                </div>
                <div class="metric-icon">📦</div>
            </div>

            <div class="metric-card">
                <div class="metric-info">
                    <p>Total Marketplace Orders</p>
                    <div class="metric-number">${totalOrders}</div>
                </div>
                <div class="metric-icon">🛒</div>
            </div>

            <div class="metric-card">
                <div class="metric-info">
                    <p>Marketplace GMV Volume</p>
                    <div class="metric-number" style="color: var(--accent);">₹<fmt:formatNumber value="${totalMarketplaceRevenue}" pattern="#,##0"/></div>
                </div>
                <div class="metric-icon">💳</div>
            </div>
        </div>

        <!-- RECENT ACTIVITY GRIDS -->
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; margin-top: 2rem;">
            <!-- RECENT USERS -->
            <div>
                <div class="section-header">
                    <h3>Recent User Registrations</h3>
                    <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline btn-sm">Manage Users →</a>
                </div>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Name</th>
                                <th>Email</th>
                                <th>Role</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentUsers}" var="u">
                                <tr>
                                    <td><strong>${u.name}</strong></td>
                                    <td>${u.email}</td>
                                    <td>
                                        <span class="status-badge ${u.role == 'ADMIN' ? 'status-DELIVERED' : (u.role == 'SELLER' ? 'status-CONFIRMED' : 'status-PENDING')}">
                                            ${u.role}
                                        </span>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- RECENT ORDERS -->
            <div>
                <div class="section-header">
                    <h3>Recent Orders</h3>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline btn-sm">Manage Orders →</a>
                </div>
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Order Ref</th>
                                <th>Total</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${recentOrders}" var="o">
                                <tr>
                                    <td><strong>#${o.id}</strong></td>
                                    <td style="color: var(--accent); font-weight: 700;">₹<fmt:formatNumber value="${o.totalAmount}" pattern="#,##0.00"/></td>
                                    <td><span class="status-badge status-${o.status}">${o.status}</span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <footer>
        <div class="container footer-bottom">
            &copy; 2026 Akhilan Mart. Capstone Project Identifier: <code>akhilanmart</code>.
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
</body>
</html>
