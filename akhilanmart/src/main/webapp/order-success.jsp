<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Placed Successfully - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/products" class="nav-link">Products</a></li>
                <li><a href="${pageContext.request.contextPath}/orders" class="nav-link">My Orders</a></li>
            </ul>
        </div>
    </nav>

    <!-- SUCCESS CONTAINER -->
    <div class="container" style="margin-top: 3.5rem; margin-bottom: 4rem; max-width: 750px;">
        <div class="card-panel" style="text-align: center;">
            <div style="font-size: 4rem; margin-bottom: 1rem;">🎉</div>
            <h1 class="panel-title" style="font-size: 2rem; color: var(--success); margin-bottom: 0.5rem;">
                Thank You! Your Order Has Been Placed.
            </h1>
            <p style="color: var(--text-secondary); margin-bottom: 2rem;">
                Order Reference Number: <strong>#${order.id}</strong>
            </p>

            <div style="background: var(--surface-secondary); border: 1px solid var(--border); border-radius: var(--radius-md); padding: 1.5rem; text-align: left; margin-bottom: 2rem;">
                <div style="display: flex; justify-content: space-between; margin-bottom: 1rem; border-bottom: 1px solid var(--border); padding-bottom: 0.75rem;">
                    <div>
                        <div style="font-size: 0.8rem; color: var(--text-secondary);">Order Date</div>
                        <strong><fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></strong>
                    </div>
                    <div>
                        <div style="font-size: 0.8rem; color: var(--text-secondary);">Status</div>
                        <span class="status-badge status-${order.status}">${order.status}</span>
                    </div>
                    <div>
                        <div style="font-size: 0.8rem; color: var(--text-secondary);">Total Paid</div>
                        <strong style="color: var(--accent);">₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/></strong>
                    </div>
                </div>

                <h4 style="margin-bottom: 0.75rem;">Purchased Items</h4>
                <div style="display: flex; flex-direction: column; gap: 0.75rem;">
                    <c:forEach items="${order.items}" var="item">
                        <div style="display: flex; justify-content: space-between; font-size: 0.9rem;">
                            <span>${item.productName} (Qty: ${item.quantity})</span>
                            <strong>₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></strong>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div style="display: flex; gap: 1rem; justify-content: center;">
                <a href="${pageContext.request.contextPath}/orders" class="btn btn-primary">View My Orders</a>
                <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">Continue Shopping</a>
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
