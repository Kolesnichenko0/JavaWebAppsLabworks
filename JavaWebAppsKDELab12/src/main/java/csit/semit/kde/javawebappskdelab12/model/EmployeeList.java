package csit.semit.kde.javawebappskdelab12.model;

import java.util.ArrayList;

/**
 * The {@code EmployeeList} class represents a list of employees in a company.
 * It extends {@link java.util.ArrayList} and is used to manage a list of {@link Employee} objects.
 * <p>
 * The class is a singleton, meaning there can only be one instance of it in the application.
 * The instance can be accessed using the {@link #getInstance()} method.
 * <p>
 * The class provides a method to get a HTML table representation of the list of employees.
 * <p>
 * Original code provided by: Dvukhhlavov Dmytro Eduardovych
 * Changes made by: Your Name
 *
 * @author Dvukhhlavov Dmytro Eduardovych SEMIT department,
 * Kolesnychenko Denys Yevhenovych CS-222a
 * @see ProgramLanguages
 * @see Employee
 * @since 1.0.0
 */
public class EmployeeList extends ArrayList<Employee> {
    private static final long serialVersionUID = 1L;
    private static EmployeeList instance;

    private EmployeeList() {
    }

    /**
     * Returns the singleton instance of the {@code EmployeeList} class.
     * If the instance does not exist, it is created and populated with a default list of employees.
     *
     * @return The singleton instance of the {@code EmployeeList} class.
     */
    public static EmployeeList getInstance() {
        if (instance == null) {
            instance = new EmployeeList();
            instance.add(new Employee("John Doe", "24.01.1985", "male", 45000, "Java", 1));
            instance.add(new Employee("Jane Smith", "12.02.1990", "female", 50000, ".NET", 0));
            instance.add(new Employee("Robert Johnson", "14.08.1975", "male", 60000, "Python", 2));
            instance.add(new Employee("Emily Davis", "30.04.1992", "female", 55000, "PHP", 1));
            instance.add(new Employee("Michael Brown", "08.03.1980", "male", 65000, "Java", 1));
            instance.add(new Employee("Sarah Miller", "11.05.1988", "female", 60000, ".NET", 0));
            instance.add(new Employee("James Wilson", "07.07.1990", "male", 70000, "Java", 2));
            instance.add(new Employee("Jessica Moore", "15.08.1995", "female", 65000, "Java", 1));
            instance.add(new Employee("William Taylor", "20.09.1982", "male", 75000, "Java", 2));
            instance.add(new Employee("Elizabeth Anderson", "25.10.1987", "female", 70000, "Java", 0));
            instance.add(new Employee("David Thomas", "26.12.1980", "male", 80000, "PHP", 1));
            instance.add(new Employee("Emma Jackson", "10.11.1993", "female", 75000, "Java", 2));
        }
        return instance;
    }

    /**
     * Returns a HTML table representation of the list of employees.
     *
     * @return A string representing a HTML table of the employees' details, including their name, birthday, gender, salary, programming language, and importance level.
     */
    public String toHtmlTable() {
        StringBuilder sb = new StringBuilder("<table style=\"border: 1px solid black; border-collapse: collapse;\">");

        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th>Name</th>");
        sb.append("<th>Birthday</th>");
        sb.append("<th>Gender</th>");
        sb.append("<th>Salary</th>");
        sb.append("<th>Programming Language</th>");
        sb.append("<th>Importance</th>");
        sb.append("</tr>");
        sb.append("<thead>");

        sb.append("<tbody>");
        for (Employee employee : this) {
            sb.append(employee.toHtmlRow());
        }
        sb.append("</tbody>");

        sb.append("</table>");

        return sb.toString();
    }
}
