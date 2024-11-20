package csit.semit.kde.javaspringwebappskdelab3.util.fieldname.user;

/**
 * Enum representing the various field names used in the User class.
 * <p>
 * This enum is used to define the different field names that can be used in the User class. Each field name has a
 * corresponding real name that is used for display purposes. The enum provides methods to get the real name of a field.
 * </p>
 * <p>
 * The `UserFieldName` enum includes:
 * <ul>
 *   <li>{@code USERNAME} - Represents the username field.</li>
 *   <li>{@code EMAIL} - Represents the email field.</li>
 *   <li>{@code PASSWORD} - Represents the password field.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * UserFieldName fieldName = UserFieldName.USERNAME;
 * String realName = fieldName.getRealName();
 * }
 * </pre>
 * </p>
 * <p>
 * Author: Kolesnychenko Denys Yevhenovych CS-222a
 * </p>
 *
 * @since 1.0.0
 */
public enum UserFieldName {
    ID("id"),
    USERNAME("username"),
    EMAIL("email"),
    NAME("name"),
    ROLE("role"),
    PASSWORD("password");

    private final String realName;

    UserFieldName(String realName) {
        this.realName = realName;
    }

    public String getRealName() {
        return realName;
    }
}