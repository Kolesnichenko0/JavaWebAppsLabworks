<%@ page import="csit.semit.kde.javawebappskdelab14.entity.Employee" %>
<%@ page import="csit.semit.kde.javawebappskdelab14.entity.EmployeeList" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Table</title>
    <link rel="icon" href="<%= request.getContextPath() %>/png/favicon.png" sizes="32x32" type="image/png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
          crossorigin="anonymous">
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<div class="container">
    <h1 class="text-center mt-4 mb-4">Employee List</h1>
    <p class="text-center mb-4">This table displays the list of employees as of <%= java.time.LocalDate.now() %>.</p>
    <table class="table table-bordered table-hover">
        <thead class="table-success">
        <tr>
            <th>Name</th>
            <th>Birthday</th>
            <th>Gender</th>
            <th>Salary</th>
            <th>Programming Language</th>
            <th>Importance</th>
        </tr>
        </thead>
        <tbody>
        <%
            EmployeeList employeeList = (EmployeeList) request.getAttribute("employeeList");
            int index = 0;
            for (Employee empl : employeeList) {
                String rowClass = (index++ % 2 == 0) ? "table-light" : "table-success";
        %>
        <tr class="<%= rowClass %>">
            <td><%= empl.getName() %>
            </td>
            <td><%= empl.getBirthdayAsString() %>
            </td>
            <td><%= empl.getGenderAsString() %>
            </td>
            <td><%= empl.getSalaryAsCurrency() %>
            </td>
            <td><%= empl.getProgramLanguage() %>
            </td>
            <td><%= empl.getImportance() %>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <div class="text-center mt-4 mb-4">
        <a href="<%= request.getContextPath() %>/main" class="btn btn-success btn-lg"
           style="background-color: #90c695; border-color: #7ba57b;">Go
            to Home</a>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>