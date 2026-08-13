<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product != null ? 'Edit Product' : 'Create Product'} - Akhilan Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
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
                <li><a href="${pageContext.request.contextPath}/seller/products" class="nav-link">← Back to My Products</a></li>
            </ul>
        </div>
    </nav>

    <!-- FORM CONTAINER -->
    <div class="container" style="margin-top: 2.5rem; margin-bottom: 4rem; max-width: 750px;">
        <div class="card-panel">
            <h1 class="panel-title">${product != null ? 'Edit Product Listing' : 'Publish New Product'}</h1>
            
            <c:if test="${not empty sessionScope.flashError}">
                <div class="flash-message flash-error">
                    ⚠️ ${sessionScope.flashError}
                </div>
                <c:remove var="flashError" scope="session" />
            </c:if>

            <form action="${pageContext.request.contextPath}/seller/product/${product != null ? 'edit' : 'new'}" method="post" onsubmit="return validateProductForm();">
                <c:if test="${product != null}">
                    <input type="hidden" name="id" value="${product.id}">
                </c:if>

                <div class="form-group">
                    <label class="form-label" for="prodName">Product Name *</label>
                    <input type="text" id="prodName" name="name" class="form-control" value="${product != null ? product.name : ''}" placeholder="e.g. Ergonomic Office Chair" required>
                </div>

                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem;">
                    <div class="form-group">
                        <label class="form-label" for="prodPrice">Price (₹) *</label>
                        <input type="number" step="0.01" id="prodPrice" name="price" class="form-control" value="${product != null ? product.price : ''}" placeholder="1499.00" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="prodStock">Initial Stock Quantity *</label>
                        <input type="number" id="prodStock" name="stock" class="form-control" value="${product != null ? product.stock : '10'}" min="0" required>
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="prodCategory">Category *</label>
                    <select id="prodCategory" name="category" class="form-control" required>
                        <option value="">-- Select Category --</option>
                        <c:forEach items="${categories}" var="cat">
                            <option value="${cat}" ${product != null && product.category == cat ? 'selected' : ''}>${cat}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label" for="prodImageUrl">Image URL</label>
                    <input type="url" id="prodImageUrl" name="imageUrl" class="form-control" value="${product != null ? product.imageUrl : ''}" placeholder="https://images.unsplash.com/photo-...">
                    <small style="color: var(--text-secondary); font-size: 0.75rem;">Leave empty to use high quality category default image.</small>
                </div>

                <div class="form-group">
                    <label class="form-label" for="prodDesc">Product Description</label>
                    <textarea id="prodDesc" name="description" rows="4" class="form-control" placeholder="Provide detailed specs, features, and condition...">${product != null ? product.description : ''}</textarea>
                </div>

                <div style="display: flex; gap: 1rem; margin-top: 1.5rem;">
                    <button type="submit" class="btn btn-primary btn-lg" style="flex: 1;">${product != null ? 'Save Changes' : 'Publish Product'}</button>
                    <a href="${pageContext.request.contextPath}/seller/products" class="btn btn-outline btn-lg">Cancel</a>
                </div>
            </form>
        </div>
    </div>

    <!-- FOOTER -->
    <footer>
        <div class="container footer-bottom">
            &copy; 2026 Akhilan Mart. Capstone Project Identifier: <code>akhilanmart</code>.
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>
