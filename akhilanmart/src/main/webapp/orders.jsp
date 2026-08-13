<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/" class="nav-link">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/orders" class="nav-link active">My Orders</a></li>
            </ul>
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline btn-sm">🛒 Cart</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
            </div>
        </div>
    </nav>

    <!-- MAIN ORDERS CONTAINER -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <h1 class="section-title" style="margin-bottom: 0.5rem;">My Order History</h1>
        <p style="color: var(--text-secondary); margin-bottom: 2rem;">Track and review all purchases placed on Akhilan Mart</p>

        <c:choose>
            <c:when test="${not empty orders}">
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Order ID</th>
                                <th>Date & Time</th>
                                <th>Items</th>
                                <th>Total Amount</th>
                                <th>Status</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orders}" var="ord">
                                <tr>
                                    <td><strong>#${ord.id}</strong></td>
                                    <td><fmt:formatDate value="${ord.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></td>
                                    <td>${ord.items.size()} item(s)</td>
                                    <td style="font-weight: 700; color: var(--accent);">
                                        ₹<fmt:formatNumber value="${ord.totalAmount}" pattern="#,##0.00"/>
                                    </td>
                                    <td>
                                        <span class="status-badge status-${ord.status}">${ord.status}</span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/order-details?id=${ord.id}" class="btn btn-outline btn-sm">View Details</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border: 1px dashed var(--border); border-radius: var(--radius-lg);">
                    <div style="font-size: 3.5rem; margin-bottom: 1rem;">📦</div>
                    <h2>No orders found</h2>
                    <p style="color: var(--text-secondary); margin-bottom: 1.5rem;">You haven't placed any orders on Akhilan Mart yet.</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Marketplace</a>
                </div>
            </c:otherwise>
        </c:choose>
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
