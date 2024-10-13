<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<jsp:include page="/WEB-INF/views/main/header.jsp"/>
<%--<c:import url=="/WEB-INF/views/main/header.jsp"/>--%>
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
<jsp:include page="/WEB-INF/views/main/footer.jsp"/>
</body>
</html>