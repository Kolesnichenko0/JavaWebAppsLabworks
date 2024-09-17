package csit.semit.kde.javawebappskdelab12.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * The {@code Employee} class represents an employee in a company.
 * Each employee has an id, name, birthday, gender, salary, programming language they specialize in, and an importance level.
 * The importance level is a value from 0 to 2 that represents the employee's level in the company.
 * <p>
 * The class provides methods to get the employee's details, such as their name, gender, birthday, salary, programming language, and importance level.
 * It also provides methods to get the gender, birthday, and salary as strings.
 * <p>
 * The class overrides the {@link Object#toString()} method to provide a string representation of the employee, and provides a method to get a HTML row representation of the employee.
 * <p>
 * The {@code Employee} class is used in the context of a web application that manages a list of employees.
 * <p>
 * Original code provided by: Dvukhhlavov Dmytro Eduardovych
 * Changes made by: Your Name
 *
 * @author Dvukhhlavov Dmytro Eduardovych SEMIT department,
 * Kolesnychenko Denys Yevhenovych CS-222a
 * @see ProgramLanguages
 * @see EmployeeList
 * @since 1.0.0
 */
public class Employee {
    private static Long employeeNum = 0L;
    private Long id;
    private String name;
    private LocalDate birthday;
    private boolean gender;
    private double salary;
    private ProgramLanguages programLanguage;
    //Level value for company - from 0 to 2
    private int importance;


    public Employee(String name, String birthday, String gender, double salary, String programLanguage, int importance) {
        employeeNum++;
        id = employeeNum;
        this.name = name;
        this.birthday = LocalDate.parse(birthday, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        this.gender = "male".equals(gender);
        this.salary = salary;
        this.programLanguage = ProgramLanguages.values()[ProgramLanguages.getEnumIndex(programLanguage)];
        this.importance = importance;
    }

    public Employee(String name) {
        this.name = name;
        this.birthday = LocalDate.of(0, 01, 01);
        this.gender = true;
        this.salary = 0.;
        this.programLanguage = ProgramLanguages.java;
        this.importance = 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * Checks if the employee is male or female.
     *
     * @return {@code true} if the employee is male, {@code false} otherwise.
     */
    public boolean isGender() {
        return gender;
    }

    /**
     * Returns the gender of the employee as a string.
     *
     * @return A string representing the gender of the employee. "female" if the employee is female, "male" otherwise.
     */
    public String getGenderAsString() {
        return gender ? "female" : "male";
    }

    /**
     * Checks if the employee is female.
     *
     * @return {@code true} if the employee is female, {@code false} otherwise.
     */
    public boolean isFemale() {
        return getGenderAsString().equalsIgnoreCase("female");
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Returns the birthday of the employee as a string.
     *
     * @return A string representing the birthday of the employee in the format "dd.MM.yyyy".
     */
    public String getBirthdayAsString() {
        return birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public double getSalary() {
        return salary;
    }

    /**
     * Returns the salary of the employee as a string in currency format.
     *
     * @return A string representing the salary of the employee in the format "xx.xx UAH".
     */
    public String getSalaryAsCurrency() {
        return String.format(Locale.US, "%.2f UAH", salary);
    }

    public String getProgramLanguage() {
        return programLanguage.getDisplayName();
    }

    public int getImportance() {
        return importance;
    }

    /**
     * Returns a string representation of the employee.
     *
     * @return A string representing the employee, including their id, name, birthday, gender, salary, programming language, and importance level.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employee{");
        sb.append("employeeNum=").append(employeeNum);
        sb.append(", id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", birthday=").append(birthday);
        sb.append(", gender=").append(gender);
        sb.append(", salary=").append(salary);
        sb.append(", programLanguage=").append(programLanguage);
        sb.append(", importance=").append(importance);
        sb.append('}');
        return sb.toString();
    }

    /**
     * Returns a HTML row representation of the employee.
     *
     * @return A string representing a HTML row of the employee's details, including their name, birthday, gender, salary, programming language, and importance level.
     */
    public String toHtmlRow() {
        StringBuilder sb = new StringBuilder("<tr>");
        sb.append("<td>").append(getName()).append("</td>");
        sb.append("<td>").append(getBirthdayAsString()).append("</td>");
        sb.append("<td>").append(getGenderAsString()).append("</td>");
        sb.append("<td>").append(getSalaryAsCurrency()).append("</td>");
        sb.append("<td>").append(getProgramLanguage()).append("</td>");
        sb.append("<td>").append(getImportance()).append("</td>");
        sb.append("</tr>");
        return sb.toString();
    }


}
