package csit.semit.kde.javawebappskdelab12.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
        this.birthday = LocalDate.of(0,01,01);
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

    public boolean isGender() {
        return gender;
    }

    public String getGenderAsString() {
        return gender ? "female" : "male";
    }

    public boolean isFemale() {
        return getGenderAsString().equalsIgnoreCase("female");
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getBirthdayAsString() {
        return birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public double getSalary() {
        return salary;
    }

    public String getSalaryAsCurrency() {
        return String.format(Locale.US, "%.2f UAH", salary);
    }

    public String getProgramLanguage() {
        return programLanguage.getDisplayName();
    }

    public int getImportance() {
        return importance;
    }


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
