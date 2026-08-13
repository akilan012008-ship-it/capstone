<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Listings Management - Admin Panel - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/admin/users" class="nav-link">User Accounts</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/products" class="nav-link active">Product Listings</a></li>
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
                <h1 class="dashboard-title">All Marketplace Products</h1>
                <p style="color: var(--text-secondary);">Moderate product catalog and remove non-compliant listings</p>
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
                        <th>ID</th>
                        <th>Product Details</th>
                        <th>Seller</th>
                        <th>Category</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${products}" var="prod">
                        <tr>
                            <td><strong>#${prod.id}</strong></td>
                            <td>
                                <div style="display: flex; align-items: center; gap: 0.75rem;">
                                    <img src="${prod.imageUrl}" alt="${prod.name}" style="width: 40px; height: 40px; object-fit: cover; border-radius: var(--radius-sm);">
                                    <span>${prod.name}</span>
                                </div>
                            </td>
                            <td>${prod.sellerName}</td>
                            <td><span class="category-badge" style="position:static;">${prod.category}</span></td>
                            <td style="font-weight: 700; color: var(--accent);">₹<fmt:formatNumber value="${prod.price}" pattern="#,##0.00"/></td>
                            <td>${prod.stock} units</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/product/delete" method="post" style="margin:0;">
                                    <input type="hidden" name="id" value="${prod.id}">
                                    <button type="submit" class="btn btn-danger btn-sm" onclick="return confirmDelete('Remove product #${prod.id} from marketplace?')">Remove Listing</button>
                                </form>
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
