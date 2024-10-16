<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="context-path" content="${pageContext.servletContext.contextPath}">
    <title>Restore Train</title>
    <link rel="icon" href="${pageContext.servletContext.contextPath}/resources/images/favicon.png" sizes="32x32"
          type="image/png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <link href="${pageContext.servletContext.contextPath}/resources/css/train/trains.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            crossorigin="anonymous"></script>
    <script src="${pageContext.servletContext.contextPath}/resources/js/train/train-restore.js" type="module"
            defer></script>
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/main/header.jsp"/>
<div class="container mt-4">
    <div class="card">
        <div class="card-header">
            <h5 class="card-title">Restore Train</h5>
        </div>
        <div class="card-body">
            <div id="formError" class="alert alert-danger d-none"></div>
            <div class="mb-3">
                <label for="trainNumber" class="form-label">Train Number</label>
                <input type="text" id="trainNumber" class="form-control" placeholder="112ІС" minlength="1" maxlength="6"
                       required>
            </div>
            <button id="restoreByNumberBtn" class="btn btn-info mt-2 w-100">Restore by Number</button>
        </div>
    </div>
    <div class="card mt-4">
        <div class="card-header">
            <h5 class="card-title">Deleted Trains</h5>
        </div>
        <div class="card-body">
            <table class="table table-bordered table-hover">
                <thead class="table-success">
                <tr>
                    <th>Number</th>
                    <th>Departure Station</th>
                    <th>Arrival Station</th>
                    <th>Movement Type</th>
                    <th>Departure Time</th>
                    <th>Arrival Time</th>
                    <th>Duration</th>
                </tr>
                </thead>
                <tbody lang="uk" id="deletedTrainTableBody">
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/main/footer.jsp"/>
</body>
</html>