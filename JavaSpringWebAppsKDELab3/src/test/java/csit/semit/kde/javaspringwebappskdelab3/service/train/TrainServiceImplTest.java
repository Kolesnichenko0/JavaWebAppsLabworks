package csit.semit.kde.javaspringwebappskdelab3.service.train;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import csit.semit.kde.javaspringwebappskdelab3.service.trainticket.TrainTicketService;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.train.TrainQueryParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the TrainService implementation.
 * <p>
 * This class contains unit tests for the TrainService implementation, ensuring that the service methods
 * function correctly. The tests cover various scenarios including creating, updating, deleting, and
 * retrieving train entities, as well as handling duplicate entries and validation errors.
 * </p>
 * <p>
 * The main functionalities tested in this class include:
 * <ul>
 *   <li>Creating a new train entity.</li>
 *   <li>Handling duplicate train entries.</li>
 *   <li>Finding a train by its ID.</li>
 *   <li>Updating an existing train entity.</li>
 *   <li>Deleting a train entity.</li>
 *   <li>Retrieving all train entities.</li>
 *   <li>Finding trains by various criteria such as number, arrival station, and departure station.</li>
 *   <li>Filtering and sorting trains based on specific criteria.</li>
 *   <li>Retrieving tickets associated with a train.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
@SpringBootTest
public class TrainServiceImplTest {

    @Autowired
    private TrainService trainService;

    @Autowired
    private TrainTicketService trainTicketService;

    @BeforeEach
    public void setUp() {
        ServiceResult<Void> result = trainService.deleteAll();
        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
    }

    @Test
    public void testCreateTrain() {
        TrainDTO trainDTO = new TrainDTO(null, "123ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> result = trainService.save(trainDTO);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("123ІС", result.getEntity().getNumber());
    }

    @Test
    public void testCreateTrainDuplicate() {
        TrainDTO trainDTO = new TrainDTO(null, "123ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        trainService.save(trainDTO);
        ServiceResult<TrainDTO> result = trainService.save(trainDTO);

        assertEquals(ServiceStatus.DUPLICATE_ENTRY, result.getStatus());
    }

    @Test
    public void testFindById() {
        TrainDTO trainDTO = new TrainDTO(null, "124ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> createResult = trainService.save(trainDTO);
        Long id = createResult.getEntity().getId();

        ServiceResult<TrainDTO> result = trainService.findById(id);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("124ІС", result.getEntity().getNumber());
    }

    @Test
    public void testFindByIdInvalid() {
        ServiceResult<TrainDTO> result = trainService.findById(-1L);

        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testUpdateTrain() {
        TrainDTO trainDTO = new TrainDTO(null, "125ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> createResult = trainService.save(trainDTO);
        Long id = createResult.getEntity().getId();

        TrainDTO updatedTrainDTO = new TrainDTO(null, "125ІС", "Львів-2", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> result = trainService.update(id, updatedTrainDTO);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("125ІС", result.getEntity().getNumber());
        assertEquals("Львів-2", result.getEntity().getDepartureStation());
    }

    @Test
    public void testUpdateTrainDuplicate() {
        TrainDTO trainDTO1 = new TrainDTO(null, "126ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        TrainDTO trainDTO2 = new TrainDTO(null, "127ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        trainService.save(trainDTO1);
        ServiceResult<TrainDTO> createResult = trainService.save(trainDTO2);
        Long id = createResult.getEntity().getId();

        TrainDTO updatedTrainDTO = new TrainDTO(null, "126ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> result = trainService.update(id, updatedTrainDTO);

        assertEquals(ServiceStatus.DUPLICATE_ENTRY, result.getStatus());
    }

    @Test
    public void testDeleteTrain() {
        TrainDTO trainDTO = new TrainDTO(null, "120ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> createResult = trainService.save(trainDTO);
        Long id = createResult.getEntity().getId();

        ServiceResult<Void> result = trainService.delete(id);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
    }

    @Test
    public void testDeleteTrainInvalid() {
        ServiceResult<Void> result = trainService.delete(-1L);

        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testGetAllList() {
        trainService.save(new TrainDTO(null, "128ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.save(new TrainDTO(null, "129ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findTrains(null);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFindByNumber() {
        TrainDTO trainDTO = new TrainDTO(null, "135ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        trainService.save(trainDTO);

        ServiceResult<TrainDTO> result = trainService.findByNumber("135ІС");

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("135ІС", result.getEntity().getNumber());
    }

    @Test
    public void testFindByArrivalStation() {
        trainService.save(new TrainDTO(null, "131ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.save(new TrainDTO(null, "132ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findTrains(
                new TrainQueryParams.Builder().searchingArrivalStation("Одеса-Головна").build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFindByDepartureStation() {
        trainService.save(new TrainDTO(null, "133ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.save(new TrainDTO(null, "134ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findTrains(
                new TrainQueryParams.Builder().searchingDepartureStation("Львів").build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFindByArrivalAndDeparture() {
        trainService.save(new TrainDTO(null, "139ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.save(new TrainDTO(null, "136ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findTrains(
                new TrainQueryParams.Builder().searchingArrivalStation("Одеса-Головна").searchingDepartureStation("Львів").build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFilterAndSortByCriteria() {
        trainService.save(new TrainDTO(null, "137ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.save(new TrainDTO(null, "138ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findTrains(
                new TrainQueryParams.Builder()
                        .filteringMovementTypes(Set.of(MovementType.DAILY))
                        .filteringFrom(LocalTime.of(9, 0))
                        .filteringTo(LocalTime.of(11, 0))
                        .filteringMinDuration(Duration.ofHours(4))
                        .filteringMaxDuration(Duration.ofHours(6))
                        .sortedByTrainNumberAsc(true)
                        .build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testGetTrainTickets() {
        TrainDTO trainDTO = new TrainDTO(null, "140ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> trainResult = trainService.save(trainDTO);
        assertEquals(ServiceStatus.SUCCESS, trainResult.getStatus());
        Long trainId = trainResult.getEntity().getId();

        TrainTicketDTO ticket1 = new TrainTicketDTO(null, null, null, null, trainId, "Іванов", "ВА123456", 1, 1, LocalDate.now().plusDays(1));
        TrainTicketDTO ticket2 = new TrainTicketDTO(null, null, null, null, trainId, "Петров", "МН789012", 2, 1, LocalDate.now().plusDays(1));

        ServiceResult<TrainTicketDTO> saveResult1 = trainTicketService.save(ticket1);
        ServiceResult<TrainTicketDTO> saveResult2 = trainTicketService.save(ticket2);

        assertEquals(ServiceStatus.SUCCESS, saveResult1.getStatus());
        assertEquals(ServiceStatus.SUCCESS, saveResult2.getStatus());

        ServiceResult<TrainTicketDTO> result = trainService.getTrainTickets(trainId);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertEquals(2, result.getEntityList().size());
    }
}