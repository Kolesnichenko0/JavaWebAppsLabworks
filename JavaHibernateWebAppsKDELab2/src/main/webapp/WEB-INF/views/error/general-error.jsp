<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%--<%@ page buffer="none" %>--%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageContext.errorData.statusCode} Error</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <link rel="icon" href="${pageContext.servletContext.contextPath}/resources/images/favicon.png" sizes="32x32" type="image/png">
</head>
<body class="d-flex flex-column min-vh-100">
<header class="bg-success text-white py-3">
    <div class="container d-flex justify-content-between align-items-center">
        <a href="${pageContext.servletContext.contextPath}/main" class="d-flex align-items-center text-white text-decoration-none">
            <img src="${pageContext.servletContext.contextPath}/resources/images/logo.png" alt="Logo" class="me-2"/>
            <h1 class="mb-0">Train Management System</h1>
        </a>

        <nav>
            <ul class="nav">
                <li class="nav-item">
                    <a href="${pageContext.servletContext.contextPath}/project-description" class="nav-link text-white">
                        Project Description
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.servletContext.contextPath}/trains" class="nav-link text-white">
                        Manage Trains
                    </a>
                </li>
                <li class="nav-item">
                    <a href="${pageContext.servletContext.contextPath}/main" class="nav-link text-white">
                        Go to Home
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</header>
<main class="container my-5">
    <div class="text-center">
        <h1 class="mb-4">${pageContext.errorData.statusCode} -
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    Page Not Found
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 500}">
                    Internal Server Error
                </c:when>
                <c:otherwise>
                    Error
                </c:otherwise>
            </c:choose>
        </h1>
        <p class="lead mb-4">
            <c:choose>
                <c:when test="${pageContext.errorData.statusCode == 404}">
                    The page you are looking for might have been removed, had its name changed, or is temporarily unavailable.
                </c:when>
                <c:when test="${pageContext.errorData.statusCode == 500}">
                    There was a problem on the server while processing your request. We are doing our best to resolve it as soon as possible.
                </c:when>
                <c:otherwise>
                    An unexpected error occurred.
                </c:otherwise>
            </c:choose>
        </p>
        <div class="text-center">
            <a href="${pageContext.servletContext.contextPath}/main" class="btn btn-success btn-lg"
               style="background-color: #90c695; border-color: #7ba57b;">Go to Home</a>
        </div>
    </div>
</main>
<footer class="bg-light text-center py-3 mt-auto" style="color: #333;">
    <div class="container">
        <p class="mb-0" style="font-size: 14px; color: #7ba57b;">
            Copyright Denys KOLESNYCHENKO, CS-222a, SEMIT, CSIT, NTU "KhPI", 2024.
        </p>
    </div>
</footer>
</body>
</html>