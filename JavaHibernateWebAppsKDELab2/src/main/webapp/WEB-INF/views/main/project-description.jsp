<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Project Description</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <link rel="icon" href="${pageContext.servletContext.contextPath}/resources/images/favicon.png" sizes="32x32"
          type="image/png">
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/main/header.jsp"/>
<main class="container my-5">
    <h1 class="text-center mb-4">Project Description</h1>

    <p class="lead text-center mb-4">
        Welcome to the Employee Portal project. This web application is designed to manage and view information
        about trains.
        Currently, it provides a functionality to view a list of trains. Additionally, it allows users to add new
        trains, update existing train information, and delete trains from the system.
    </p>

    <div class="text-center mb-4">
        <h2 class="mb-3">Project Details</h2>
        <p>
            <strong>Developer:</strong> Kolesnychenko Denys Yevhenovych<br>
            <strong>Course:</strong> CS-222a, SEMIT department<br>
            <strong>Group List Number:</strong> 11<br>
        </p>
    </div>

    <div class="text-center">
        <a href="${pageContext.servletContext.contextPath}/main" class="btn btn-success btn-lg"
           style="background-color: #90c695; border-color: #7ba57b;">Go
            to Home</a>
    </div>
</main>

<jsp:include page="/WEB-INF/views/main/footer.jsp"/>
</body>
</html>