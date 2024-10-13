package csit.semit.kde.javahibernatewebappskdelab2.entity;

import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TrainTest {

    @Nested
    @DisplayName("Train Number Validation")
    class TrainNumberValidation {


        @ParameterizedTest(name = "Valid train number = {0}")
        @DisplayName("Should pass for valid train numbers")
        @ValueSource(strings = {"122ІС", "322ІС", "563ІС", "759ІС"})
        void testValidTrainNumbers(String number) {
            assertDoesNotThrow(() -> Train.validateNumber(number),
                    "Train number should be valid.");
        }

        @ParameterizedTest(name = "Invalid train number = {0}")
        @DisplayName("Should throw exception for invalid train numbers")
        @ValueSource(strings = {"123X", "12X", "1234", "123", "12", "AAA", "000"})
        void testInvalidTrainNumbers(String number) {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> Train.validateNumber(number));
            assertEquals("The number should consist of 1 to 3 digits and {ІС+, ІС, РЕ, P, НЕ, НШ, НП}", exception.getMessage());
        }

        @ParameterizedTest(name = "Train number with auto-padding = {0}")
        @DisplayName("Should add leading zeros when necessary")
        @CsvSource({
                "12ІС, 012ІС",
                "1ІС, 001ІС"
        })
        void testTrainNumberWithPadding(String input, String expected) {
            String result = Train.validateNumber(input);
            assertEquals(expected, result, "The train number should be padded with leading zeros.");
        }
    }

    @Nested
    @DisplayName("Station Name Validation")
    class StationNameValidation {
        @ParameterizedTest(name = "Valid station name = {0}")
        @DisplayName("Should pass for valid station names")
        @ValueSource(strings = {"Караваєві Дачі", "Любінь-Великий", "Люботин-Західний", "Київ-Волинський", "Харків-Пасажирський"})
        void testValidStationNames(String station) {
            assertDoesNotThrow(() -> Train.validateStationName(station, FieldName.DEPARTURE_STATION),
                    "Station name should be valid.");
        }

        @ParameterizedTest(name = "Invalid station name = {0}")
        @DisplayName("Should throw exception for invalid station names")
        @ValueSource(strings = {"Київ@", "Львів123", "Одеса!", "Житомир#", "Тернопіль$"})
        void testInvalidStationNames(String station) {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> Train.validateStationName(station, FieldName.DEPARTURE_STATION));
            assertEquals("Station name must start with a Ukrainian capital letter and can include spaces, hyphens, numbers, and parentheses.", exception.getMessage());
        }

        @ParameterizedTest(name = "Station name with correction = {0}")
        @DisplayName("Should auto-correct station name's case")
        @CsvSource({
                "київ, Київ",
                "львів, Львів",
                "одеса, Одеса"
        })
        void testAutoCorrectStationNames(String input, String expected) {
            String result = Train.validateStationName(input, FieldName.DEPARTURE_STATION);
            assertEquals(expected, result, "The station name should be auto-corrected to start with an uppercase letter.");
        }
    }

    @Nested
    @DisplayName("Train Object Creation")
    class TrainObjectCreation {

        @Test
        @DisplayName("Should create a Train object and validate its fields")
        void testTrainObjectCreation() {
            String number = "123ІС";
            String departureStation = "Київ";
            String arrivalStation = "Львів";
            MovementType movementType = MovementType.DAILY;
            LocalTime departureTime = LocalTime.of(10, 30);
            Duration duration = Duration.ofHours(5);

            Train train = new Train(number, arrivalStation, departureStation, movementType, departureTime, duration);
            assertEquals("123ІС", train.getNumber());
            assertEquals("Київ", train.getDepartureStation());
            assertEquals("Львів", train.getArrivalStation());
            assertEquals(MovementType.DAILY, train.getMovementType());
            assertEquals(LocalTime.of(10, 30), train.getDepartureTime());
            assertEquals(Duration.ofHours(5), train.getDuration());
            assertFalse(train.getIsDeleted());
        }
    }
}
