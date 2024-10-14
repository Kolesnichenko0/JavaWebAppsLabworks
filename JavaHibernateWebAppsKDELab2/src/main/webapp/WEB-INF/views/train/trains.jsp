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
    <title>Trains</title>
    <link rel="icon" href="${pageContext.servletContext.contextPath}/resources/images/favicon.png" sizes="32x32"
          type="image/png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
    <link href="${pageContext.servletContext.contextPath}/resources/css/train/trains.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            crossorigin="anonymous"></script>
    <script src="${pageContext.servletContext.contextPath}/resources/js/train/trains.js" type="module" defer></script>
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/views/main/header.jsp"/>
<div class="container mt-4">
    <div class="d-flex justify-content-between mb-3">
        <button id="createTrainBtn" class="btn btn-primary">Create Train</button>
        <button id="restoreTrainBtn" class="btn btn-secondary">Restore Train</button>
    </div>
    <div class="card mb-3">
        <div class="card-header">
            <h5 class="card-title">Search Trains</h5>
        </div>
        <div class="card-body">
            <div class="mb-3">
                <label for="searchNumber" class="form-label">Train Number</label>
                <input type="text" id="searchNumber" class="form-control" placeholder="112ІС" minlength="1"
                       maxlength="6">
            </div>
            <button id="searchByNumberBtn" class="btn btn-info mt-2 w-100">Search by Number</button>
            <div class="mb-3">
                <label for="searchDeparture" class="form-label">Departure Station</label>
                <input type="text" id="searchDeparture" class="form-control" placeholder="Полтава-Київська"
                       minlength="1" maxlength="100">
            </div>
            <div class="mb-3">
                <label for="searchArrival" class="form-label">Arrival Station</label>
                <input type="text" id="searchArrival" class="form-control" placeholder="Харків-Пасажирський"
                       minlength="1" maxlength="100">
            </div>
            <div class="d-flex justify-content-between">
                <button id="searchByDepartureBtn" class="btn btn-info w-100 me-2">Search by Departure</button>
                <button id="searchByArrivalBtn" class="btn btn-info w-100 me-2">Search by Arrival</button>
                <button id="searchByDepartureArrivalBtn" class="btn btn-info w-100">Search by Departure & Arrival
                </button>
            </div>
        </div>
    </div>

    <div class="card mb-3" id="currentStatus">
        <div class="card-header">
            <h5 class="card-title">Current Status</h5>
        </div>
        <div class="card-body">
            <div id="currentSearch" class="alert alert-info" role="alert">
                <strong>Search:</strong> Showing all results
            </div>
            <div id="currentSort" class="alert alert-info" role="alert">
                <strong>Sorting:</strong> No sorting applied
            </div>
        </div>
    </div>
    <div id="sortingSection" class="mb-3">
        <div class="d-flex justify-content-between align-items-center">
            <div class="dropdown">
                <button class="btn btn-light dropdown-toggle" type="button" id="sortDropdown" data-bs-toggle="dropdown"
                        aria-expanded="false">
                    Sort Options
                </button>
                <ul class="dropdown-menu" aria-labelledby="sortDropdown">
                    <li><button class="dropdown-item" id="sortByNumberAscBtn">Sort by Number Asc</button></li>
                    <li><button class="dropdown-item" id="sortByNumberDescBtn">Sort by Number Desc</button></li>
                    <li><button class="dropdown-item" id="sortByDurationAscBtn">Sort by Duration Asc</button></li>
                    <li><button class="dropdown-item" id="sortByDurationDescBtn">Sort by Duration Desc</button></li>
                    <li><button class="dropdown-item" id="sortByDepartureTimeAscBtn">Sort by Departure Time Asc</button></li>
                    <li><button class="dropdown-item" id="sortByDepartureTimeDescBtn">Sort by Departure Time Desc</button></li>
                </ul>
            </div>
            <button id="resetSortBtn" class="btn btn-danger">Reset Sort</button>
        </div>
    </div>
    <div class="row">
        <div class="col-md-3">
            <div class="card">
                <div class="card-body">
                    <h5>Filter Options</h5>
                    <c:forEach var="movement" items="${MovementType.values()}">
                        <div class="form-check">
                            <input class="form-check-input" type="checkbox" value="${movement.getDisplayName()}" id="filter${movement.name()}">
                            <label class="form-check-label" for="filter${movement.name()}">
                                    ${fn:toUpperCase(fn:substring(movement.getDisplayName(), 0, 1))}${fn:toLowerCase(fn:substring(movement.getDisplayName(), 1, fn:length(movement.displayName)))}
                            </label>
                        </div>
                    </c:forEach>
                    <div class="mt-3">
                        <label for="filterFromTime">Departure Time From:</label>
                        <input type="time" id="filterFromTime" class="form-control">
                        <label for="filterToTime" class="mt-2">Departure Time To:</label>
                        <input type="time" id="filterToTime" class="form-control">
                    </div>
                    <div class="mt-3">
                        <label for="minDuration">Min Duration:</label>
                        <input type="range" id="minDuration" class="form-range" min="0" max="1440" step="1" oninput="updateDurationLabels()">
                        <label for="maxDuration">Max Duration:</label>
                        <input type="range" id="maxDuration" class="form-range" min="0" max="1440" step="1" oninput="updateDurationLabels()">
                        <div class="d-flex justify-content-between">
                            <span>0h</span>
                            <span id="durationLabels">0h 0m - 24h 0m</span>
                            <span>24h</span>
                        </div>
                    </div>
                    <div class="d-flex justify-content-between mt-3">
                        <button id="applyFiltersBtn" class="btn btn-success">Apply Filters</button>
                        <button id="resetFiltersBtn" class="btn btn-danger">Reset Filters</button>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <table class="table table-bordered table-hover">
                <thead class="table-success">
                <tr>
                    <th>Number</th>
                    <th>Departure Station</th>
                    <th>Arrival Station</th>
                    <th>Movement Type</th>
                    <th>Departure Time</th>
                    <th>Duration</th>
                </tr>
                </thead>
                <tbody id="trainTableBody">
                </tbody>
            </table>
        </div>
    </div>
</div>
<jsp:include page="/WEB-INF/views/main/footer.jsp"/>
</body>
</html>