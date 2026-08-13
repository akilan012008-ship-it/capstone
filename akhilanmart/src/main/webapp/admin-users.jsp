<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Accounts Management - Admin Panel - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link active">User Accounts</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products" class="nav-link">Product Listings</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/orders" class="nav-link">Marketplace Orders</a></li>
            </ul>
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
            </div>
        </div>
    </nav>

    <!-- MAIN CONTAINER -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <div class="dashboard-header">
            <div>
                <h1 class="dashboard-title">User Accounts Governance</h1>
                <p style="color: var(--text-secondary);">View and manage buyer, seller, and administrator accounts</p>
            </div>
            
            <div style="display: flex; gap: 0.5rem;">
                <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-outline btn-sm ${selectedRole == 'ALL' ? 'active' : ''}">All Users</a>
                <a href="${pageContext.request.contextPath}/admin/users?role=BUYER" class="btn btn-outline btn-sm ${selectedRole == 'BUYER' ? 'active' : ''}">Buyers</a>
                <a href="${pageContext.request.contextPath}/admin/users?role=SELLER" class="btn btn-outline btn-sm ${selectedRole == 'SELLER' ? 'active' : ''}">Sellers</a>
            </div>
        </div>

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="flash-message flash-success">
                ✅ ${sessionScope.flashSuccess}
            </div>
            <c:remove var="flashSuccess" scope="session" />
        </c:if>
        <c:if test="${not empty sessionScope.flashError}">
            <div class="flash-message flash-error">
                ⚠️ ${sessionScope.flashError}
            </div>
            <c:remove var="flashError" scope="session" />
        </c:if>

        <div class="table-wrapper">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>User ID</th>
                        <th>Name</th>
                        <th>Email Address</th>
                        <th>Role</th>
                        <th>Joined Date</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${users}" var="u">
                        <tr>
                            <td><strong>#${u.id}</strong></td>
                            <td>${u.name}</td>
                            <td>${u.email}</td>
                            <td>
                                <span class="status-badge ${u.role == 'ADMIN' ? 'status-DELIVERED' : (u.role == 'SELLER' ? 'status-CONFIRMED' : 'status-PENDING')}">
                                    ${u.role}
                                </span>
                            </td>
                            <td><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy"/></td>
                            <td>
                                <c:choose>
                                    <c:when test="${u.role != 'ADMIN'}">
                                        <form action="${pageContext.request.contextPath}/admin/user/delete" method="post" style="margin:0;">
                                            <input type="hidden" name="id" value="${u.id}">
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirmDelete('Remove user ${u.name}? All associated listings/orders will be affected.')">Remove User</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <span style="font-size: 0.8rem; color: var(--text-secondary); italic;">System Admin</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- FOOTER -->
    <footer>
        <div class="container footer-bottom">
            &copy; 2026 Akhilan Mart. Capstone Project Identifier: <code>akhilanmart</code>.
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
