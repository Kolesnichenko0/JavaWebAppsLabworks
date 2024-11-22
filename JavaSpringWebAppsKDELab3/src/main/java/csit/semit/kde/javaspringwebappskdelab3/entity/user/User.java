package csit.semit.kde.javaspringwebappskdelab3.entity.user;

import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.user.UserFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.Check;

import java.io.Serial;
import java.io.Serializable;

/**
 * Entity class representing a User.
 * <p>
 * This class is mapped to the "users" table in the database and includes various fields such as username, email, password, name, and role.
 * It also includes validation constraints for the fields.
 * </p>
 * <p>
 * The `User` class includes:
 * <ul>
 *   <li>Field validation using regular expressions and custom validation methods</li>
 *   <li>Annotations for JPA entity mapping and Lombok for boilerplate code generation</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_username", columnList = "username"),
        @Index(name = "idx_user_email", columnList = "email")
})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 12345678987654321L;

    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    public static final String NAME_REGEX = "^([А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ[\\']]+(\\-[А-ЗЙ-ЩЮЯЇІЄ][а-щьюяїієґ[\\']]+)?)$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@gmail\\.com$";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = USERNAME_REGEX, message = "Username must be 3-20 characters long and can only contain letters, digits, and underscores.")
    @Check(constraints = "REGEXP_LIKE(username, '" + USERNAME_REGEX + "', 'c')")
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @NotNull
    @Email(regexp = EMAIL_REGEX, message = "Email should be valid")
    @Check(constraints = "REGEXP_LIKE(email, '" + EMAIL_REGEX + "', 'c')")
    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull
    @Pattern(regexp = NAME_REGEX, message = "Name must start with a Ukrainian capital letter and can include lowercase letters and apostrophes.")
    @Check(constraints = "REGEXP_LIKE(name, '" + NAME_REGEX + "', 'c')")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public User(@NonNull String username, @NonNull String email, @NonNull String password, @NonNull String name, @NonNull Role role) {
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setName(name);
        setRole(role);
    }

    public static String validateName(@NonNull String name) {
        if (name.isEmpty()) {
            throw new FieldValidationException("Name cannot be empty", "name");
        }

        name = name.trim();
        String[] parts = name.split("-");
        StringBuilder validatedName = new StringBuilder();

        for (String part : parts) {
            part = part.trim();
            if (Character.isLowerCase(part.charAt(0))) {
                part = Character.toUpperCase(part.charAt(0)) + part.substring(1);
            }
            validatedName.append(part).append("-");
        }

        validatedName.setLength(validatedName.length() - 1);

        if (!validatedName.toString().matches(NAME_REGEX)) {
            throw new FieldValidationException("Name must start with a Ukrainian capital letter and can include lowercase letters, apostrophes, and hyphens.", UserFieldName.NAME.getRealName());
        }

        return validatedName.toString();
    }

    public static String validateUsername(@NonNull String username) {
        if (!username.matches(USERNAME_REGEX)) {
            throw new FieldValidationException("Username must be 3-20 characters long and can only contain letters, digits, and underscores.", UserFieldName.USERNAME.getRealName());
        }
        return username;
    }

    public static String validateEmail(@NonNull String email) {
        if (!email.matches(EMAIL_REGEX)) {
            throw new FieldValidationException("Email should be valid", UserFieldName.EMAIL.getRealName());
        }
        return email;
    }

    public static String validatePassword(@NonNull String password) {
        if (password.length() < 8 || password.length() > 30) {
            throw new FieldValidationException("Password must be between 8 and 30 characters long", UserFieldName.PASSWORD.getRealName());
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new FieldValidationException("Password must contain at least one uppercase letter", UserFieldName.PASSWORD.getRealName());
        }
        if (!password.matches(".*[a-z].*")) {
            throw new FieldValidationException("Password must contain at least one lowercase letter", UserFieldName.PASSWORD.getRealName());
        }
        if (!password.matches(".*\\d.*")) {
            throw new FieldValidationException("Password must contain at least one digit", UserFieldName.PASSWORD.getRealName());
        }
        if (!password.matches(".*[!@#$%^&*(),.?:{}|<>].*")) {
            throw new FieldValidationException("Password must contain at least one special character", UserFieldName.PASSWORD.getRealName());
        }
        return password;
    }

    public void setUsername(@NonNull String username) {
        this.username = validateUsername(username);
    }

    public void setEmail(@NonNull String email) {
        this.email = validateEmail(email);
    }

    public void setName(@NonNull String name) {
        this.name = validateName(name);
    }
}