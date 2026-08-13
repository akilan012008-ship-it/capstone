<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order #${order.id} Details - Akhilan Mart</title>
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
                <li><a href="${pageContext.request.contextPath}/orders" class="nav-link">← Back to Orders</a></li>
            </ul>
        </div>
    </nav>

    <!-- MAIN ORDER DETAILS -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem;">
        <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 2rem;">
            <div>
                <h1 class="section-title">Order #${order.id} Details</h1>
                <p style="color: var(--text-secondary);">Placed on <fmt:formatDate value="${order.createdAt}" pattern="dd MMMM yyyy, hh:mm a"/></p>
            </div>
            <span class="status-badge status-${order.status}" style="font-size: 0.9rem; padding: 6px 14px;">${order.status}</span>
        </div>

        <div style="display: grid; grid-template-columns: 2fr 1fr; gap: 2rem;">
            <!-- ITEMS TABLE -->
            <div class="table-wrapper">
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Category</th>
                            <th>Unit Price</th>
                            <th>Qty</th>
                            <th>Subtotal</th>
                            <th>Review</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${order.items}" var="item">
                            <tr>
                                <td>
                                    <div style="display: flex; align-items: center; gap: 0.75rem;">
                                        <img src="${item.productImageUrl}" alt="${item.productName}" style="width: 44px; height: 44px; object-fit: cover; border-radius: var(--radius-sm);">
                                        <div>
                                            <div style="font-weight: 700;">${item.productName}</div>
                                            <div style="font-size: 0.75rem; color: var(--text-secondary);">Seller: ${item.sellerName}</div>
                                        </div>
                                    </div>
                                </td>
                                <td><span class="category-badge" style="position:static;">${item.productCategory}</span></td>
                                <td>₹<fmt:formatNumber value="${item.unitPrice}" pattern="#,##0.00"/></td>
                                <td style="font-weight: 700;">${item.quantity}</td>
                                <td style="font-weight: 700; color: var(--accent);">₹<fmt:formatNumber value="${item.subtotal}" pattern="#,##0.00"/></td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/product-details?id=${item.productId}" class="btn btn-outline btn-sm">Rate & Review</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>

            <!-- CUSTOMER & BILLING SUMMARY -->
            <div class="card-panel" style="height: fit-content;">
                <h3 class="panel-title">Order Information</h3>
                
                <div style="margin-bottom: 1rem;">
                    <div style="font-size: 0.8rem; color: var(--text-secondary);">Buyer Name</div>
                    <strong>${order.buyerName}</strong>
                </div>

                <div style="margin-bottom: 1rem;">
                    <div style="font-size: 0.8rem; color: var(--text-secondary);">Buyer Email</div>
                    <strong>${order.buyerEmail}</strong>
                </div>

                <div style="border-top: 1px solid var(--border); padding-top: 1rem; margin-top: 1.5rem; display: flex; justify-content: space-between; align-items: center;">
                    <span style="font-weight: 700;">Total Amount</span>
                    <span style="font-family: var(--font-heading); font-size: 1.6rem; font-weight: 800; color: var(--accent);">
                        ₹<fmt:formatNumber value="${order.totalAmount}" pattern="#,##0.00"/>
                    </span>
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
