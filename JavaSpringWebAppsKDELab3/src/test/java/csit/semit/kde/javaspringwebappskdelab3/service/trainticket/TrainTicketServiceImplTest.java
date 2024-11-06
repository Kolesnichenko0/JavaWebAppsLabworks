package csit.semit.kde.javaspringwebappskdelab3.service.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import csit.semit.kde.javaspringwebappskdelab3.service.train.TrainService;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket.TrainTicketQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the TrainTicketService implementation.
 * <p>
 * This class contains unit tests for the TrainTicketService implementation, focusing on various CRUD operations
 * and validation scenarios for train tickets. The tests ensure that the service methods function correctly
 * when creating, updating, deleting, and retrieving train tickets.
 * </p>
 * <p>
 * The main functionalities tested in this class include:
 * <ul>
 *   <li>Setting up the test environment by deleting all existing train and train ticket entities before each test.</li>
 *   <li>Creating valid and invalid train tickets and verifying the results.</li>
 *   <li>Handling duplicate entries and validation errors.</li>
 *   <li>Retrieving train tickets by various criteria such as ID, passenger surname, passport number, date range, and train ID.</li>
 *   <li>Updating train tickets partially and verifying the changes.</li>
 *   <li>Deleting train tickets individually and in bulk.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
@SpringBootTest
public class TrainTicketServiceImplTest {

    @Autowired
    private TrainTicketService trainTicketService;

    @Autowired
    private TrainService trainService;

    private Long trainId;

    @BeforeEach
    public void setUp() {
        trainTicketService.deleteAll();
        trainService.deleteAll();

        TrainDTO trainDTO = new TrainDTO(null, "123ІС", "Львів", "Одеса-Головна",
                MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> trainResult = trainService.save(trainDTO);
        trainId = trainResult.getEntity().getId();
    }

    private TrainTicketDTO createValidTicketDTO() {
        return new TrainTicketDTO(
                null,
                null,
                null,
                null,
                trainId,
                "Петренко",
                "123456789",
                1,
                1,
                LocalDate.now().plusDays(10)
        );
    }

    @Test
    public void testCreateTrainTicket() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();

        ServiceResult<TrainTicketDTO> result = trainTicketService.save(ticketDTO);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("Петренко", result.getEntity().getPassengerSurname());
    }

    @Test
    public void testCreateTrainTicketDuplicate() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        trainTicketService.save(ticketDTO);
        ServiceResult<TrainTicketDTO> result = trainTicketService.save(ticketDTO);

        assertEquals(ServiceStatus.DUPLICATE_ENTRY, result.getStatus());
    }

    @Test
    public void testCreateTrainTicketInvalidTrain() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ticketDTO.setTrainId(-1L);
        ServiceResult<TrainTicketDTO> result = trainTicketService.save(ticketDTO);

        assertEquals(ServiceStatus.VALIDATION_ERROR, result.getStatus());
    }

    @Test
    public void testCreateTrainTicketInvalidPassengerSurname() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ticketDTO.setPassengerSurname("123");
        ServiceResult<TrainTicketDTO> result = trainTicketService.save(ticketDTO);

        assertEquals(ServiceStatus.VALIDATION_ERROR, result.getStatus());
    }

    @Test
    public void testCreateTrainTicketInvalidPassportNumber() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ticketDTO.setPassportNumber("INVALID");
        ServiceResult<TrainTicketDTO> result = trainTicketService.save(ticketDTO);

        assertEquals(ServiceStatus.VALIDATION_ERROR, result.getStatus());
    }

    @Test
    public void testCreateTrainTicketInvalidSeatNumber() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ticketDTO.setSeatNumber(-1);
        ServiceResult<TrainTicketDTO> result = trainTicketService.save(ticketDTO);

        assertEquals(ServiceStatus.VALIDATION_ERROR, result.getStatus());
    }

    @Test
    public void testCreateTrainTicketInvalidCarriageNumber() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ticketDTO.setCarriageNumber(-1);
        ServiceResult<TrainTicketDTO> result = trainTicketService.save(ticketDTO);

        assertEquals(ServiceStatus.VALIDATION_ERROR, result.getStatus());
    }

    @Test
    public void testCreateTrainTicketPastDate() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ticketDTO.setDepartureDate(LocalDate.now().minusDays(1));
        ServiceResult<TrainTicketDTO> result = trainTicketService.save(ticketDTO);

        assertEquals(ServiceStatus.VALIDATION_ERROR, result.getStatus());
    }

    @Test
    public void testFindById() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ServiceResult<TrainTicketDTO> createResult = trainTicketService.save(ticketDTO);
        Long id = createResult.getEntity().getId();

        ServiceResult<TrainTicketDTO> result = trainTicketService.findById(id);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("Петренко", result.getEntity().getPassengerSurname());
    }

    @Test
    public void testFindByIdInvalid() {
        ServiceResult<TrainTicketDTO> result = trainTicketService.findById(-1L);
        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testFindByKeySet() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ServiceResult<TrainTicketDTO> createResult = trainTicketService.save(ticketDTO);

        ServiceResult<TrainTicketDTO> result = trainTicketService.findByKeySet(
                trainId,
                ticketDTO.getSeatNumber(),
                ticketDTO.getCarriageNumber(),
                ticketDTO.getDepartureDate()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
    }

    @Test
    public void testSaveAll() {
        List<TrainTicketDTO> tickets = new ArrayList<>();
        tickets.add(createValidTicketDTO());

        TrainTicketDTO ticket2 = createValidTicketDTO();
        ticket2.setSeatNumber(2);
        tickets.add(ticket2);

        ServiceResult<TrainTicketDTO> result = trainTicketService.saveAll(tickets);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertEquals(2, result.getEntityList().size());
    }

    @Test
    public void testSaveAllDuplicate() {
        List<TrainTicketDTO> tickets = new ArrayList<>();
        TrainTicketDTO ticket = createValidTicketDTO();
        tickets.add(ticket);
        tickets.add(ticket);

        ServiceResult<TrainTicketDTO> result = trainTicketService.saveAll(tickets);

        assertEquals(ServiceStatus.DUPLICATE_ENTRY, result.getStatus());
    }

    @Test
    public void testUpdatePartial() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ServiceResult<TrainTicketDTO> createResult = trainTicketService.save(ticketDTO);
        Long id = createResult.getEntity().getId();

        Map<String, Object> updates = new HashMap<>();
        updates.put("passengerSurname", "Іваненко");

        ServiceResult<TrainTicketDTO> result = trainTicketService.updatePartial(id, updates);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertEquals("Іваненко", result.getEntity().getPassengerSurname());
    }

    @Test
    public void testFindTicketsByPassengerSurname() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        trainTicketService.save(ticketDTO);

        ServiceResult<TrainTicketDTO> result = trainTicketService.findTicketTrains(
                new TrainTicketQueryParams.Builder()
                        .searchingPassengerSurname("Петренко")
                        .build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() == 1);
    }

    @Test
    public void testFindTicketsByPassportNumber() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        trainTicketService.save(ticketDTO);

        ServiceResult<TrainTicketDTO> result = trainTicketService.findTicketTrains(
                new TrainTicketQueryParams.Builder()
                        .searchingPassportNumber("123456789")
                        .build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() == 1);
    }

    @Test
    public void testFindTicketsByDateRange() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        trainTicketService.save(ticketDTO);

        ServiceResult<TrainTicketDTO> result = trainTicketService.findTicketTrains(
                new TrainTicketQueryParams.Builder()
                        .filteringDepartureDateFrom(LocalDate.now())
                        .filteringDepartureDateTo(LocalDate.now().plusDays(30))
                        .build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() == 1);
    }

    @Test
    public void testFindTicketsByTrainId() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        trainTicketService.save(ticketDTO);

        ServiceResult<TrainTicketDTO> result = trainTicketService.findTicketTrains(
                new TrainTicketQueryParams.Builder()
                        .searchingTrainId(trainId)
                        .build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() == 1);
    }

    @Test
    public void testDelete() {
        TrainTicketDTO ticketDTO = createValidTicketDTO();
        ServiceResult<TrainTicketDTO> createResult = trainTicketService.save(ticketDTO);
        Long id = createResult.getEntity().getId();

        ServiceResult<Void> result = trainTicketService.delete(id);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
    }

    @Test
    public void testDeleteInvalid() {
        ServiceResult<Void> result = trainTicketService.delete(-1L);
        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testDeleteAll() {
        trainTicketService.save(createValidTicketDTO());
        TrainTicketDTO ticket2 = createValidTicketDTO();
        ticket2.setSeatNumber(2);
        trainTicketService.save(ticket2);

        ServiceResult<Void> result = trainTicketService.deleteAll();
        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
    }
}