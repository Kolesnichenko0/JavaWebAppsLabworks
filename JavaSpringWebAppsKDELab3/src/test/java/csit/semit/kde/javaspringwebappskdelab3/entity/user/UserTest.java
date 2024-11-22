package csit.semit.kde.javaspringwebappskdelab3.entity.user;

import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.user.UserFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link User} entity.
 * <p>
 * This class contains unit tests for the {@code User} entity, focusing on validation of usernames, emails, names, and passwords,
 * as well as the creation of {@code User} objects. It uses JUnit 5 for testing and includes nested test classes for
 * better organization of test cases.
 * </p>
 * <p>
 * The `UserTest` class includes:
 * <ul>
 *   <li>Validation of usernames</li>
 *   <li>Validation of emails</li>
 *   <li>Validation of names</li>
 *   <li>Validation of passwords</li>
 *   <li>Creation and validation of {@code User} objects</li>
 * </ul>
 * </p>
 *
 * @see User
 * @see Role
 * @see UserFieldName
 * @since 1.0.0
 * @author Kolesnychenko Denys Yevhenovych
 */
class UserTest {

    @Nested
    @DisplayName("Username Validation")
    class UsernameValidation {

        @ParameterizedTest(name = "Valid username = {0}")
        @DisplayName("Should pass for valid usernames")
        @ValueSource(strings = {"user_123", "UserName", "userName_1", "user123"})
        void testValidUsernames(String username) {
            assertDoesNotThrow(() -> User.validateUsername(username),
                    "Username should be valid.");
        }

        @ParameterizedTest(name = "Invalid username = {0}")
        @DisplayName("Should throw exception for invalid usernames")
        @ValueSource(strings = {"us", "user name", "user@name", "user-name", "user.name", "user#name"})
        void testInvalidUsernames(String username) {
            Exception exception = assertThrows(FieldValidationException.class,
                    () -> User.validateUsername(username));
            assertEquals("Username must be 3-20 characters long and can only contain letters, digits, and underscores.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Email Validation")
    class EmailValidation {

        @ParameterizedTest(name = "Valid email = {0}")
        @DisplayName("Should pass for valid emails")
        @ValueSource(strings = {"user@gmail.com", "user.name@gmail.com", "user_name@gmail.com", "user-name@gmail.com"})
        void testValidEmails(String email) {
            assertDoesNotThrow(() -> User.validateEmail(email),
                    "Email should be valid.");
        }

        @ParameterizedTest(name = "Invalid email = {0}")
        @DisplayName("Should throw exception for invalid emails")
        @ValueSource(strings = {"userexample.com", "user@.com", "user@example.com", "user@com.", "user@com.c", "user@com..com"})
        void testInvalidEmails(String email) {
            Exception exception = assertThrows(FieldValidationException.class,
                    () -> User.validateEmail(email));
            assertEquals("Email should be valid", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Name Validation")
    class NameValidation {

        @ParameterizedTest(name = "Valid name = {0}")
        @DisplayName("Should pass for valid names")
        @ValueSource(strings = {"Іван", "Олександр", "Марія", "Андрій", "Олена", "Іван-Петро"})
        void testValidNames(String name) {
            assertDoesNotThrow(() -> User.validateName(name),
                    "Name should be valid.");
        }

        @ParameterizedTest(name = "Invalid name = {0}")
        @DisplayName("Should throw exception for invalid names")
        @ValueSource(strings = {"ivan", "Олександр1", "Марія@", "Андрій!", "Олена#", "Іван-Петро1"})
        void testInvalidNames(String name) {
            Exception exception = assertThrows(FieldValidationException.class,
                    () -> User.validateName(name));
            assertEquals("Name must start with a Ukrainian capital letter and can include lowercase letters, apostrophes, and hyphens.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Password Validation")
    class PasswordValidation {

        @ParameterizedTest(name = "Valid password = {0}")
        @DisplayName("Should pass for valid passwords")
        @ValueSource(strings = {"Password1!", "ValidPass123@", "Another$Pass2", "StrongPass#4"})
        void testValidPasswords(String password) {
            assertDoesNotThrow(() -> User.validatePassword(password),
                    "Password should be valid.");
        }

        @ParameterizedTest(name = "Invalid password = {0}")
        @DisplayName("Should throw exception for invalid passwords")
        @ValueSource(strings = {"short1!", "NoSpecialChar1", "nouppercase1!", "NOLOWERCASE1!", "NoDigit!", "toolongpasswordtoolongpassword1!"})
        void testInvalidPasswords(String password) {
            Exception exception = assertThrows(FieldValidationException.class,
                    () -> User.validatePassword(password));
            assertTrue(exception.getMessage().contains("Password"), "Password should be invalid.");
        }
    }

    @Nested
    @DisplayName("User Object Creation")
    class UserObjectCreation {

        @Test
        @DisplayName("Should create a User object and validate its fields")
        void testUserObjectCreation() {
            String username = "user_123";
            String email = "user@gmail.com";
            String password = "Password1!";
            String name = "Іван";
            Role role = Role.USER;

            User user = new User(username, email, password, name, role);
            assertEquals("user_123", user.getUsername());
            assertEquals("user@gmail.com", user.getEmail());
            assertEquals("Password1!", user.getPassword());
            assertEquals("Іван", user.getName());
            assertEquals(Role.USER, user.getRole());
        }
    }
}