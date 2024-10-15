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
    <link rel="icon" href="${pageContext.servletContext.contextPath}/resources/images/favicon.png" sizes="32x32"
          type="image/png">
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/main/header.jsp"/>
<jsp:include page="/WEB-INF/views/main/greeting.jsp"/>
<main class="container my-5">
    <div class="text-center">
        <h1 class="mb-4">JavaHibernateWebAppsKDELab2</h1>
        <p class="lead mb-4">Developer: Kolesnychenko Denys Yevhenovych</p>
    </div>
</main>
<jsp:include page="/WEB-INF/views/main/menu.jsp"/>
<div class="text-center mt-4">
    <p class="lead">"The journey of a thousand miles begins with a single step."</p>
</div>
<jsp:include page="/WEB-INF/views/main/footer.jsp"/>
</body>
</html>