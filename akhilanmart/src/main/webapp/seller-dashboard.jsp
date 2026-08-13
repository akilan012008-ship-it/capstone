<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Dashboard - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link active">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/products" class="nav-link">My Products</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/orders" class="nav-link">Customer Orders</a></li>
            </ul>
            <div class="nav-actions">
                <span class="user-greeting">Seller: <strong>${sessionScope.user.name}</strong></span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
            </div>
        </div>
    </nav>

    <!-- DASHBOARD CONTAINER -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <div class="dashboard-header">
            <div>
                <h1 class="dashboard-title">Seller Control Panel</h1>
                <p style="color: var(--text-secondary);">Manage products, monitor inventory stock, and process customer orders</p>
            </div>
            <a href="${pageContext.request.contextPath}/seller/product/new" class="btn btn-primary">➕ Add New Product</a>
        </div>

        <c:if test="${not empty sessionScope.flashSuccess}">
            <div class="flash-message flash-success">
                ✅ ${sessionScope.flashSuccess}
            </div>
            <c:remove var="flashSuccess" scope="session" />
        </c:if>

        <!-- METRICS CARDS -->
        <div class="metrics-grid">
            <div class="metric-card">
                <div class="metric-info">
                    <p>Total Products</p>
                    <div class="metric-number">${totalProducts}</div>
                </div>
                <div class="metric-icon">📦</div>
            </div>

            <div class="metric-card">
                <div class="metric-info">
                    <p>Total Units Stock</p>
                    <div class="metric-number">${totalStock}</div>
                </div>
                <div class="metric-icon">📊</div>
            </div>

            <div class="metric-card">
                <div class="metric-info">
                    <p>Customer Orders</p>
                    <div class="metric-number">${totalOrdersCount}</div>
                </div>
                <div class="metric-icon">🛒</div>
            </div>

            <div class="metric-card">
                <div class="metric-info">
                    <p>Seller Sales Volume</p>
                    <div class="metric-number" style="color: var(--accent);">₹<fmt:formatNumber value="${totalRevenue}" pattern="#,##0"/></div>
                </div>
                <div class="metric-icon">💰</div>
            </div>
        </div>

        <!-- RECENT PRODUCTS -->
        <div style="margin-bottom: 3rem;">
            <div class="section-header">
                <h3>My Active Listings</h3>
                <a href="${pageContext.request.contextPath}/seller/products" class="btn btn-outline btn-sm">View All Products →</a>
            </div>

            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Product Name</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${recentProducts}" var="prod">
                            <tr>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 0.75rem;">
                                        <img src="${prod.imageUrl}" alt="${prod.name}" style="width: 36px; height: 36px; object-fit: cover; border-radius: var(--radius-sm);">
                                        <strong>${prod.name}</strong>
                                    </div>
                                </td>
                                <td><span class="category-badge" style="position:static;">${prod.category}</span></td>
                                <td style="font-weight: 700; color: var(--accent);">₹<fmt:formatNumber value="${prod.price}" pattern="#,##0.00"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${prod.stock > 0}">
                                            <span class="stock-tag in-stock">${prod.stock} units</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="stock-tag out-stock">Out of stock</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/seller/product/edit?id=${prod.id}" class="btn btn-outline btn-sm">Edit</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
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
