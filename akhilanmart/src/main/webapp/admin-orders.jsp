<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Marketplace Orders - Admin Panel - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/admin/products" class="nav-link">Product Listings</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/orders" class="nav-link active">Marketplace Orders</a></li>
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
                <h1 class="dashboard-title">Global Marketplace Orders</h1>
                <p style="color: var(--text-secondary);">Monitor transactions, order items, and override status updates</p>
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
                        <th>Order ID</th>
                        <th>Buyer Name</th>
                        <th>Date & Time</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Update Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${orders}" var="ord">
                        <tr>
                            <td>
                                <a href="${pageContext.request.contextPath}/order-details?id=${ord.id}">
                                    <strong>#${ord.id}</strong>
                                </a>
                            </td>
                            <td>${ord.buyerName} <br><small style="color: var(--text-secondary);">${ord.buyerEmail}</small></td>
                            <td><fmt:formatDate value="${ord.createdAt}" pattern="dd MMM yyyy, hh:mm a"/></td>
                            <td style="font-weight: 700; color: var(--accent);">₹<fmt:formatNumber value="${ord.totalAmount}" pattern="#,##0.00"/></td>
                            <td><span class="status-badge status-${ord.status}">${ord.status}</span></td>
                            <td>
                                <form action="${pageContext.request.contextPath}/admin/order/update-status" method="post" style="display: flex; gap: 0.5rem; margin:0;">
                                    <input type="hidden" name="orderId" value="${ord.id}">
                                    <select name="status" class="form-control" style="padding: 0.3rem 0.6rem; font-size: 0.8rem; width: 130px;">
                                        <option value="PENDING" ${ord.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                                        <option value="CONFIRMED" ${ord.status == 'CONFIRMED' ? 'selected' : ''}>CONFIRMED</option>
                                        <option value="SHIPPED" ${ord.status == 'SHIPPED' ? 'selected' : ''}>SHIPPED</option>
                                        <option value="DELIVERED" ${ord.status == 'DELIVERED' ? 'selected' : ''}>DELIVERED</option>
                                        <option value="CANCELLED" ${ord.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                                    </select>
                                    <button type="submit" class="btn btn-outline btn-sm">Save</button>
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
</body>
</html>
