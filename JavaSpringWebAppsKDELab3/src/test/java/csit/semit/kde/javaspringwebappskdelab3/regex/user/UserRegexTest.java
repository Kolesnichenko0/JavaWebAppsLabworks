package csit.semit.kde.javaspringwebappskdelab3.regex.user;

import csit.semit.kde.javaspringwebappskdelab3.entity.user.User;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for validating user fields using regular expressions.
 * <p>
 * This class contains unit tests for the {@code User} entity, focusing on the validation of usernames, emails, and names
 * using regular expressions. It uses JUnit 5 for testing and includes nested test classes for better organization of test cases.
 * </p>
 * <p>
 * The `UserRegexTest` class includes:
 * <ul>
 *   <li>Validation of usernames using regular expressions</li>
 *   <li>Validation of emails using regular expressions</li>
 *   <li>Validation of names using regular expressions</li>
 *   <li>Validation of passwords using static validation method</li>
 * </ul>
 * </p>
 *
 * @see User
 * @since 1.0.0
 */
class UserRegexTest {

    @Nested
    @DisplayName("Username Validation")
    class UsernameValidation {

        @ParameterizedTest(name = "Valid username = {0}")
        @DisplayName("Should pass for valid usernames")
        @ValueSource(strings = {"user_123", "UserName", "userName_1", "user123"})
        void testValidUsernames(String username) {
            assertTrue(username.matches(User.USERNAME_REGEX), "Username should be valid.");
        }

        @ParameterizedTest(name = "Invalid username = {0}")
        @DisplayName("Should fail for invalid usernames")
        @ValueSource(strings = {"us", "user name", "user@name", "user-name", "user.name", "user#name"})
        void testInvalidUsernames(String username) {
            assertFalse(username.matches(User.USERNAME_REGEX), "Username should be invalid.");
        }
    }

    @Nested
    @DisplayName("Email Validation")
    class EmailValidation {

        @ParameterizedTest(name = "Valid email = {0}")
        @DisplayName("Should pass for valid emails")
        @ValueSource(strings = {"user@example.com", "user.name@example.com", "user_name@example.com", "user-name@example.com"})
        void testValidEmails(String email) {
            assertTrue(email.matches(User.EMAIL_REGEX), "Email should be valid.");
        }

        @ParameterizedTest(name = "Invalid email = {0}")
        @DisplayName("Should fail for invalid emails")
        @ValueSource(strings = {"userexample.com", "user@"})
        void testInvalidEmails(String email) {
            assertFalse(email.matches(User.EMAIL_REGEX), "Email should be invalid.");
        }
    }

    @Nested
    @DisplayName("Name Validation")
    class NameValidation {

        @ParameterizedTest(name = "Valid name = {0}")
        @DisplayName("Should pass for valid names")
        @ValueSource(strings = {"Іван", "Олександр", "Марія", "Андрій", "Олена", "Іван-Петро"})
        void testValidNames(String name) {
            assertTrue(name.matches(User.NAME_REGEX), "Name should be valid.");
        }

        @ParameterizedTest(name = "Invalid name = {0}")
        @DisplayName("Should fail for invalid names")
        @ValueSource(strings = {"ivan", "Олександр1", "Марія@", "Андрій!", "Олена#", "Іван-Петро1"})
        void testInvalidNames(String name) {
            assertFalse(name.matches(User.NAME_REGEX), "Name should be invalid.");
        }
    }

    @Nested
    @DisplayName("Password Validation")
    class PasswordValidation {

        @ParameterizedTest(name = "Valid password = {0}")
        @DisplayName("Should pass for valid passwords")
        @ValueSource(strings = {"Password1!", "ValidPass123@", "Another$Pass2", "StrongPass#4"})
        void testValidPasswords(String password) {
            assertTrue(User.validatePassword(password).equals(password), "Password should be valid.");
        }

        @ParameterizedTest(name = "Invalid password = {0}")
        @DisplayName("Should fail for invalid passwords")
        @ValueSource(strings = {"short1!", "NoSpecialChar1", "nouppercase1!", "NOLOWERCASE1!", "NoDigit!", "toolongpasswordtoolongpassword1!"})
        void testInvalidPasswords(String password) {
            assertThrows(FieldValidationException.class, () -> User.validatePassword(password), "Password should be invalid.");
        }
    }
}