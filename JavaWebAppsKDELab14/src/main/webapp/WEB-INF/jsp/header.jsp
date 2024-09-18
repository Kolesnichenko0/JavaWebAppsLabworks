<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<header class="bg-success text-white py-3">
    <div class="container d-flex justify-content-between align-items-center">
        <a href="<%= request.getContextPath() %>/main" class="d-flex align-items-center text-white text-decoration-none">
            <img src="<%= request.getContextPath() %>/png/logo.png" alt="Logo" class="me-2"/>
            <h1 class="mb-0">Employee Portal</h1>
        </a>

        <nav>
            <ul class="nav">
                <li class="nav-item">
                    <a href="<%= request.getContextPath() %>/project-description" class="nav-link text-white">
                        Project Description
                    </a>
                </li>
                <li class="nav-item">
                    <a href="<%= request.getContextPath() %>/show-table" class="nav-link text-white">
                        Show Table (JSP)
                    </a>
                </li>
                <li class="nav-item">
                    <a href="<%= request.getContextPath() %>/main" class="nav-link text-white">
                        Go to Home
                    </a>
                </li>
            </ul>
        </nav>
    </div>
</header>