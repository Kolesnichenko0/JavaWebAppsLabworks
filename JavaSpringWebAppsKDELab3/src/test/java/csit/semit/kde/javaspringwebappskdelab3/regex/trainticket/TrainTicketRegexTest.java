package csit.semit.kde.javaspringwebappskdelab3.regex.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for validating train ticket fields using regular expressions.
 * <p>
 * This class contains unit tests for the {@code TrainTicket} entity, focusing on the validation of passenger surnames
 * and passport numbers using regular expressions. It uses JUnit 5 for testing and includes nested test classes for better
 * organization of test cases.
 * </p>
 * <p>
 * The `TrainTicketRegexTest` class includes:
 * <ul>
 *   <li>Validation of passenger surnames using regular expressions</li>
 *   <li>Validation of passport numbers using regular expressions</li>
 * </ul>
 * </p>
 */
class TrainTicketRegexTest {

    @Nested
    @DisplayName("Passenger Surname Validation")
    class PassengerSurnameValidation {

        @ParameterizedTest(name = "Valid passenger surname = {0}")
        @DisplayName("Should pass for valid passenger surnames")
        @ValueSource(strings = {"Коваленко", "Іваненко-Олексійчук", "Петренко", "Сидоренко"})
        void testValidPassengerSurnames(String surname) {
            assertTrue(surname.matches(TrainTicket.PASSENGER_SURNAME_REGEX), "Passenger surname should be valid.");
        }

        @ParameterizedTest(name = "Invalid passenger surname = {0}")
        @DisplayName("Should fail for invalid passenger surnames")
        @ValueSource(strings = {"", "123", "Коваленко1", "Іваненко@", "Петренко!"})
        void testInvalidPassengerSurnames(String surname) {
            assertFalse(surname.matches(TrainTicket.PASSENGER_SURNAME_REGEX), "Passenger surname should be invalid.");
        }
    }

    @Nested
    @DisplayName("Passport Number Validation")
    class PassportNumberValidation {

        @ParameterizedTest(name = "Valid passport number = {0}")
        @DisplayName("Should pass for valid passport numbers")
        @ValueSource(strings = {"АА123456", "ВВ654321", "123456789"})
        void testValidPassportNumbers(String passportNumber) {
            assertTrue(passportNumber.matches(TrainTicket.PASSPORT_NUMBER_REGEX), "Passport number should be valid.");
        }

        @ParameterizedTest(name = "Invalid passport number = {0}")
        @DisplayName("Should fail for invalid passport numbers")
        @ValueSource(strings = {"", "AA123456", "12345", "АА12345@", "12345678!"})
        void testInvalidPassportNumbers(String passportNumber) {
            assertFalse(passportNumber.matches(TrainTicket.PASSPORT_NUMBER_REGEX), "Passport number should be invalid.");
        }
    }
}