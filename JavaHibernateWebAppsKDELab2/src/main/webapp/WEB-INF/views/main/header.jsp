<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<header class="bg-success text-white py-3">
    <div class="container d-flex justify-content-between align-items-center">
        <a href="${pageContext.servletContext.contextPath}/main"
           class="d-flex align-items-center text-white text-decoration-none">
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