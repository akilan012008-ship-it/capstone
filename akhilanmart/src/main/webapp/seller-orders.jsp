<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Orders - Seller Panel - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/seller/dashboard" class="nav-link">Dashboard</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/products" class="nav-link">My Products</a></li>
                <li><a href="${pageContext.request.contextPath}/seller/orders" class="nav-link active">Customer Orders</a></li>
            </ul>
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
            </div>
        </div>
    </nav>

    <!-- ORDERS CONTAINER -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <div class="dashboard-header">
            <div>
                <h1 class="dashboard-title">Orders Containing My Products</h1>
                <p style="color: var(--text-secondary);">Track item fulfillments and update delivery statuses</p>
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

        <c:choose>
            <c:when test="${not empty orderItems}">
                <div class="table-wrapper">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Order Ref</th>
                                <th>Product</th>
                                <th>Qty</th>
                                <th>Total Revenue</th>
                                <th>Status</th>
                                <th>Update Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${orderItems}" var="item">
                                <tr>
                                    <td><strong>#${item.orderId}</strong></td>
                                    <td>
                                        <div style="display: flex; align-items: center; gap: 0.75rem;">
                                            <img src="${item.productImageUrl}" alt="${item.productName}" style="width: 40px; height: 40px; object-fit: cover; border-radius: var(--radius-sm);">
                                            <span>${item.productName}</span>
                                        </div>
                                    </td>
                                    <td style="font-weight: 700;">${item.quantity}</td>
                                    <td style="font-weight: 700; color: var(--accent);">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                    <td>
                                        <span class="status-badge status-${item.orderStatus}">${item.orderStatus}</span>
                                    </td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/seller/order/update-status" method="post" style="display: flex; gap: 0.5rem; margin:0;">
                                            <input type="hidden" name="orderId" value="${item.orderId}">
                                            <select name="status" class="form-control" style="padding: 0.3rem 0.6rem; font-size: 0.8rem; width: 130px;">
                                                <option value="PENDING">PENDING</option>
                                                <option value="CONFIRMED">CONFIRMED</option>
                                                <option value="SHIPPED">SHIPPED</option>
                                                <option value="DELIVERED">DELIVERED</option>
                                                <option value="CANCELLED">CANCELLED</option>
                                            </select>
                                            <button type="submit" class="btn btn-outline btn-sm">Update</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 4rem 1rem; background: var(--surface); border: 1px dashed var(--border); border-radius: var(--radius-lg);">
                    <div style="font-size: 3.5rem; margin-bottom: 1rem;">📋</div>
                    <h2>No orders received yet</h2>
                    <p style="color: var(--text-secondary);">When buyers order your products, they will show up here.</p>
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
