package csit.semit.kde.javaspringwebappskdelab3.entity.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.entity.train.Train;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link TrainTicket} entity.
 * <p>
 * This class contains unit tests for the {@code TrainTicket} entity, focusing on validation of carriage numbers,
 * train departure dates, and the creation of {@code TrainTicket} objects. It uses JUnit 5 for testing and includes
 * nested test classes for better organization of test cases.
 * </p>
 * <p>
 * The `TrainTicketTest` class includes:
 * <ul>
 *   <li>Validation of carriage numbers</li>
 *   <li>Validation of train departure dates</li>
 *   <li>Creation and validation of {@code TrainTicket} objects</li>
 * </ul>
 * </p>
 */
class TrainTicketTest {

    @Nested
    @DisplayName("Carriage Number Validation")
    class CarriageNumberValidation {

        @ParameterizedTest(name = "Valid carriage number = {0}")
        @DisplayName("Should pass for valid carriage numbers")
        @ValueSource(ints = {1, 2, 10, 50})
        void testValidCarriageNumbers(int carriageNumber) {
            assertDoesNotThrow(() -> TrainTicket.validateCarriageNumber(carriageNumber),
                    "Carriage number should be valid.");
        }

        @ParameterizedTest(name = "Invalid carriage number = {0}")
        @DisplayName("Should throw exception for invalid carriage numbers")
        @ValueSource(ints = {0, -1, -10})
        void testInvalidCarriageNumbers(int carriageNumber) {
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> TrainTicket.validateCarriageNumber(carriageNumber));
            assertEquals("Carriage number must be greater than 0", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Train Departure Date Validation")
    class TrainDepartureDateValidation {

        @Test
        @DisplayName("Should pass for valid departure dates")
        void testValidDepartureDates() {
            Train train = new Train("123ІС", "Львів", "Київ", MovementType.DAILY, LocalTime.of(10, 30), java.time.Duration.ofHours(5));
            LocalDate departureDate = LocalDate.now().plusDays(1);
            assertDoesNotThrow(() -> TrainTicket.validateTrainDepartureDate(departureDate, train),
                    "Departure date should be valid.");
        }

        @Test
        @DisplayName("Should throw exception for past departure dates")
        void testPastDepartureDates() {
            Train train = new Train("123ІС", "Львів", "Київ", MovementType.DAILY, LocalTime.of(10, 30), java.time.Duration.ofHours(5));
            LocalDate departureDate = LocalDate.now().minusDays(1);
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> TrainTicket.validateTrainDepartureDate(departureDate, train));
            assertEquals("Departure date cannot be in the past", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for departure dates on even days for ODD_DAYS movement type")
        void testOddDaysMovementType() {
            Train train = new Train("123ІС", "Львів", "Київ", MovementType.ODD_DAYS, LocalTime.of(10, 30), java.time.Duration.ofHours(5));
            LocalDate departureDate = LocalDate.of(2025, 10, 2); // An even day
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> TrainTicket.validateTrainDepartureDate(departureDate, train));
            assertEquals("Cannot create ticket for a train that only runs on odd days", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for departure dates on odd days for EVEN_DAYS movement type")
        void testEvenDaysMovementType() {
            Train train = new Train("123ІС", "Львів", "Київ", MovementType.EVEN_DAYS, LocalTime.of(10, 30), java.time.Duration.ofHours(5));
            LocalDate departureDate = LocalDate.of(2025, 10, 1); // An odd day
            Exception exception = assertThrows(IllegalArgumentException.class,
                    () -> TrainTicket.validateTrainDepartureDate(departureDate, train));
            assertEquals("Cannot create ticket for a train that only runs on even days", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("TrainTicket Object Creation")
    class TrainTicketObjectCreation {

        @Test
        @DisplayName("Should create a TrainTicket object and validate its fields")
        void testTrainTicketObjectCreation() {
            Train train = new Train("123ІС", "Львів", "Київ", MovementType.DAILY, LocalTime.of(10, 30), java.time.Duration.ofHours(5));
            train.setId(1L);
            String passengerSurname = "Коваленко";
            String passportNumber = "АА123456";
            Integer carriageNumber = 1;
            Integer seatNumber = 10;
            LocalDate departureDate = LocalDate.now().plusDays(1);

            TrainTicket trainTicket = new TrainTicket(train, passengerSurname, passportNumber, carriageNumber, seatNumber, departureDate);
            assertEquals(train, trainTicket.getTrain());
            assertEquals("Коваленко", trainTicket.getPassengerSurname());
            assertEquals("АА123456", trainTicket.getPassportNumber());
            assertEquals(1, trainTicket.getCarriageNumber());
            assertEquals(10, trainTicket.getSeatNumber());
            assertEquals(departureDate, trainTicket.getDepartureDate());
        }
    }
}