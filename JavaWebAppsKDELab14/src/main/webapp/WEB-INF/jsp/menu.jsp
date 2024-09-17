<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String projectDescriptionUrl = request.getContextPath() + "/project-description";
    String showTableUrl = request.getContextPath() + "/show-table";
%>

<div class="text-center mt-4">
    <a href="<%= projectDescriptionUrl %>" class="btn btn-success btn-lg"
       style="background-color: #90c695; border-color: #7ba57b;">Project Description</a>
    <a href="<%= showTableUrl %>" class="btn btn-success btn-lg ms-3"
       style="background-color: #90c695; border-color: #7ba57b;">Show Table (JSP)</a>
</div>