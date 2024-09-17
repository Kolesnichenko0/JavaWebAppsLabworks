<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<div class="bg-light text-dark text-center py-4 mt-3">
    <div class="container">
        <h2 class="mb-3">Welcome to Employee Portal!</h2>
        <p class="mb-0">Today: <%= java.time.LocalDate.now() %><!-- A scriptlet for inserting the current date -->
        </p>
    </div>
</div>