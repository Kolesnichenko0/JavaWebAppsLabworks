package csit.semit.kde.javahibernatewebappskdelab2.regrex;

import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for validating train numbers and station names using regular expressions.
 * <p>
 * This class contains unit tests for the {@code Train} entity, focusing on the validation of train numbers and station names
 * using regular expressions. It uses JUnit 5 for testing and includes nested test classes for better organization of test cases.
 * </p>
 * <p>
 * The `TrainNumberRegexTest` class includes:
 * <ul>
 *   <li>Validation of train numbers using regular expressions</li>
 *   <li>Validation of station names using regular expressions</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see Train
 * @since 1.0.0
 */
class TrainNumberRegexTest {

    @Nested
    @DisplayName("Train Number Validation")
    class TrainNumberValidation {

        @ParameterizedTest(name = "Valid train number = {0}")
        @DisplayName("Should pass for valid train numbers")
        @ValueSource(strings = {"001ІС", "150ІС", "298РЕ", "750НШ", "788НП", "099ІС", "001ІС", "001ІС"})
        void testValidTrainNumbers(String number) {
            assertTrue(number.matches(Train.NUMBER_REGEX), "Train number should be valid.");
        }

        @ParameterizedTest(name = "Invalid train number = {0}")
        @DisplayName("Should fail for invalid train numbers")
        @ValueSource(strings = {"000ІС", "299", "751X", "1234", "12", "AAA", "000"})
        void testInvalidTrainNumbers(String number) {
            assertFalse(number.matches(Train.NUMBER_REGEX), "Train number should be invalid.");
        }
    }

    @Nested
    @DisplayName("Train Station Validation")
    class TrainStationValidation {

        @ParameterizedTest(name = "Valid station name = {0}")
        @DisplayName("Should pass for valid station names")
        @ValueSource(strings = {"Київ-Пас", "КИЇВ", "Київ пас.", "КИЇВ 123", "Київ-Пас 1", "Львів", "Харків"})
        void testValidStationNames(String station) {
            assertTrue(station.matches(Train.STATION_REGEX), "Station name should be valid.");
        }

        @ParameterizedTest(name = "Invalid station name = {0}")
        @DisplayName("Should fail for invalid station names")
        @ValueSource(strings = {"", "123", "Київ1", "Львів@", "Харків!"})
        void testInvalidStationNames(String station) {
            assertFalse(station.matches(Train.STATION_REGEX), "Station name should be invalid.");
        }
    }
}
