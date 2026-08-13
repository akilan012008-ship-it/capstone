<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Server Error (500) - Akhilan Mart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/responsive.css">
</head>
<body>

    <!-- NAVBAR -->
    <nav class="navbar">
        <div class="container nav-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <span class="brand-akhilan">Akhilan</span><span class="brand-mart">Mart</span>
            </a>
        </div>
    </nav>

    <!-- ERROR CONTAINER -->
    <div class="container" style="margin-top: 4rem; margin-bottom: 4rem; text-align: center; max-width: 650px;">
        <div style="font-size: 5rem; margin-bottom: 1rem;">⚠️</div>
        <h1 style="font-family: var(--font-heading); font-size: 2.5rem; font-weight: 800; margin-bottom: 0.5rem;">500 - Internal Server Error</h1>
        <p style="color: var(--text-secondary); font-size: 1.1rem; margin-bottom: 1.5rem;">
            An unexpected error occurred while processing your request. Please try again or return to home.
        </p>

        <c:if test="${not empty errorMessage}">
            <div class="flash-message flash-error" style="text-align: left; margin-bottom: 2rem;">
                ${errorMessage}
            </div>
        </c:if>

        <a href="${pageContext.request.contextPath}/" class="btn btn-primary btn-lg">Return to Marketplace Home</a>
    </div>

    <!-- FOOTER -->
    <footer>
        <div class="container footer-bottom">
            &copy; 2026 Akhilan Mart. Capstone Project Identifier: <code>akhilanmart</code>.
        </div>
    </footer>
</body>
</html>
