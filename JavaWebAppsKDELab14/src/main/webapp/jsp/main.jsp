<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Main page</title>
    <link rel="icon" href="<%= request.getContextPath() %>/png/favicon.png" sizes="32x32" type="image/png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<jsp:include page="/WEB-INF/jsp/greeting.jsp"/>
<main class="container my-5">
    <div class="text-center">
        <h1 class="mb-4">JavaWebAppsKDELab14</h1>
        <p class="lead mb-4">Developer: Kolesnychenko Denys Yevhenovych</p>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/menu.jsp"/>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>