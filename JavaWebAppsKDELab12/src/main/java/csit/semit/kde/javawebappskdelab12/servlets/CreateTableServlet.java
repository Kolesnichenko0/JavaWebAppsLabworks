package csit.semit.kde.javawebappskdelab12.servlets;

import java.io.*;

import csit.semit.kde.javawebappskdelab12.model.EmployeeList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet("/create-table")
public class CreateTableServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><head><title>KDE - Hello Table</title></head><body>");
        out.println("<h1>CREATED TABLE</h1>");
        out.println("<br><br>");
        EmployeeList employeeList = EmployeeList.getInstance();
        out.println(employeeList.toHtmlTable());
        out.println("<br><br>");
        out.println("<a href=\".\">To START</a>");
        out.println("</body></html>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}