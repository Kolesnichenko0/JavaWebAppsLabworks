<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="context-path" content="${pageContext.servletContext.contextPath}">
    <title>Create Train</title>
    <link rel="icon" href="${pageContext.servletContext.contextPath}/resources/images/favicon.png" sizes="32x32"
          type="image/png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="${pageContext.servletContext.contextPath}/resources/css/train/trains.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            crossorigin="anonymous"></script>
    <script src="${pageContext.servletContext.contextPath}/resources/js/train/train-create.js" type="module" defer></script>
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/main/header.jsp"/>
<div class="container mt-4">
    <div class="card">
        <div class="card-header">
            <h5 class="card-title">Create Train</h5>
        </div>
        <div class="card-body">
            <form id="createTrainForm">
                <div id="formError" class="alert alert-danger d-none"></div>
                <div class="mb-3">
                    <label for="trainNumber" class="form-label">Train Number</label>
                    <input type="text" id="trainNumber" class="form-control" placeholder="112ІС" minlength="1"
                           maxlength="6" required>
                </div>
                <div class="mb-3">
                    <label for="departureStation" class="form-label">Departure Station</label>
                    <input type="text" id="departureStation" class="form-control" placeholder="Полтава-Київська"
                           minlength="1" maxlength="100" required>
                </div>
                <div class="mb-3">
                    <label for="arrivalStation" class="form-label">Arrival Station</label>
                    <input type="text" id="arrivalStation" class="form-control" placeholder="Харків-Пасажирський"
                           minlength="1" maxlength="100" required>
                </div>
                <div class="mb-3">
                    <label for="movementType" class="form-label">Movement Type</label>
                    <select id="movementType" class="form-select" required>
                        <c:forEach var="movement" items="${MovementType.getMovementTypes()}">
                            <option value="${movement}">${fn:toUpperCase(fn:substring(movement, 0, 1))}${fn:toLowerCase(fn:substring(movement, 1, fn:length(movement)))}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="departureTime" class="form-label">Departure Time</label>
                    <input type="time" id="departureTime" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label for="durationHours" class="form-label">Duration (hours and minutes)</label>
                    <div class="d-flex">
                        <input type="number" id="durationHours" class="form-control me-2" min="0" max="24"
                               placeholder="Hours" required>
                        <input type="number" id="durationMinutes" class="form-control" min="0" max="59"
                               placeholder="Minutes" required>
                    </div>
                </div>
                <div class="d-flex justify-content-between">
                    <button type="submit" class="btn btn-success">Create</button>
                    <button type="button" id="cancelBtn" class="btn btn-danger">Cancel</button>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/main/footer.jsp"/>
</body>
</html>