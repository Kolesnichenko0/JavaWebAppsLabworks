<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Main Page</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <link rel="icon" href="${pageContext.servletContext.contextPath}/resources/images/favicon.png" sizes="32x32" type="image/png">
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/main/header.jsp"/>

<main class="container my-5 flex-grow-1 d-flex flex-column justify-content-center">
    <div class="text-center">
        <h1 class="mb-4">Welcome to Train Management System</h1>
        <p class="lead mb-4">We're glad to see you on our website.</p>
        <div class="mt-4">
            <a href="${pageContext.servletContext.contextPath}/login" class="btn btn-success btn-lg"
               style="background-color: #90c695; border-color: #7ba57b;">Login</a>
        </div>
    </div>
</main>

<div class="text-center mt-4">
    <p class="lead">"The journey of a thousand miles begins with a single step."</p>
</div>

<jsp:include page="/WEB-INF/views/main/footer.jsp"/>
</body>
</html>