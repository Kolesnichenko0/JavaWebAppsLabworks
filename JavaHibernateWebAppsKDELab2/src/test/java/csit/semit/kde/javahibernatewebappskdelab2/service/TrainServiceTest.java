package csit.semit.kde.javahibernatewebappskdelab2.service;

import csit.semit.kde.javahibernatewebappskdelab2.dao.DAOManager;
import csit.semit.kde.javahibernatewebappskdelab2.dto.TrainDTO;
import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.service.ServiceStatus;
import csit.semit.kde.javahibernatewebappskdelab2.util.criteria.TrainQueryParams;
import csit.semit.kde.javahibernatewebappskdelab2.util.hibernate.HibernateUtil;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.service.ServiceResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TrainServiceTest {

    private TrainService trainService;

    @BeforeEach
    public void setUp() {
        DAOManager daoManager = new DAOManager(HibernateUtil.getSessionFactory(), HibernateUtil.getValidator());
        trainService = new TrainService(daoManager);
        daoManager.getTrainDAO().truncateTable();
    }

    @Test
    public void testCreateTrain() {
        TrainDTO trainDTO = new TrainDTO(null, "123ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> result = trainService.create(trainDTO);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("123ІС", result.getEntity().getNumber());
    }

    @Test
    public void testCreateTrainDuplicate() {
        TrainDTO trainDTO = new TrainDTO(null, "130ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        trainService.create(trainDTO);
        ServiceResult<TrainDTO> result = trainService.create(trainDTO);

        assertEquals(ServiceStatus.DUPLICATE_ENTRY, result.getStatus());
    }

    @Test
    public void testFindById() {
        TrainDTO trainDTO = new TrainDTO(null, "124ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> createResult = trainService.create(trainDTO);
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
        ServiceResult<TrainDTO> createResult = trainService.create(trainDTO);
        Long id = createResult.getEntity().getId();

        TrainDTO updatedTrainDTO = new TrainDTO(id, "125ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> result = trainService.update(id, updatedTrainDTO);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("125ІС", result.getEntity().getNumber());
    }

    @Test
    public void testUpdateTrainDuplicate() {
        TrainDTO trainDTO1 = new TrainDTO(null, "126ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        TrainDTO trainDTO2 = new TrainDTO(null, "127ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        trainService.create(trainDTO1);
        ServiceResult<TrainDTO> createResult = trainService.create(trainDTO2);
        Long id = createResult.getEntity().getId();

        TrainDTO updatedTrainDTO = new TrainDTO(id, "126ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> result = trainService.update(id, updatedTrainDTO);

        assertEquals(ServiceStatus.DUPLICATE_ENTRY, result.getStatus());
    }

    @Test
    public void testDeleteTrain() {
        TrainDTO trainDTO = new TrainDTO(null, "120ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> createResult = trainService.create(trainDTO);
        Long id = createResult.getEntity().getId();

        ServiceResult<TrainDTO> result = trainService.delete(id);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("120ІС", result.getEntity().getNumber());
    }

    @Test
    public void testDeleteTrainInvalid() {
        ServiceResult<TrainDTO> result = trainService.delete(-1L);

        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testRestoreTrain() {
        TrainDTO trainDTO = new TrainDTO(null, "119ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> createResult = trainService.create(trainDTO);
        Long id = createResult.getEntity().getId();

        trainService.delete(id);
        ServiceResult<TrainDTO> result = trainService.restore(id);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("119ІС", result.getEntity().getNumber());
    }

    @Test
    public void testRestoreTrainInvalid() {
        ServiceResult<TrainDTO> result = trainService.restore(-1L);

        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testRestoreTrainByNumber() {
        TrainDTO trainDTO = new TrainDTO(null, "118ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> serviceResult = trainService.create(trainDTO);
        trainService.delete(serviceResult.getEntity().getId());

        ServiceResult<TrainDTO> result = trainService.restore("118ІС");

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("118ІС", result.getEntity().getNumber());
    }

    @Test
    public void testGetAllList() {
        trainService.create(new TrainDTO(null, "128ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.create(new TrainDTO(null, "129ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.getAllList();

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFindByNumber() {
        TrainDTO trainDTO = new TrainDTO(null, "135ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        trainService.create(trainDTO);

        ServiceResult<TrainDTO> result = trainService.findByNumber("135ІС");

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("135ІС", result.getEntity().getNumber());
    }

    @Test
    public void testFindByArrivalStation() {
        trainService.create(new TrainDTO(null, "131ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.create(new TrainDTO(null, "132ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findAndFilterAndSortByQueryParams(
                new TrainQueryParams.Builder().searchingArrivalStation("Одеса-Головна").build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFindByDepartureStation() {
        trainService.create(new TrainDTO(null, "133ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.create(new TrainDTO(null, "134ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findAndFilterAndSortByQueryParams(
                new TrainQueryParams.Builder().searchingDepartureStation("Львів").build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFindByArrivalAndDeparture() {
        trainService.create(new TrainDTO(null, "139ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.create(new TrainDTO(null, "136ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findAndFilterAndSortByQueryParams(
                new TrainQueryParams.Builder().searchingArrivalStation("Одеса-Головна").searchingDepartureStation("Львів").build()
        );

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFilterAndSortByCriteria() {
        trainService.create(new TrainDTO(null, "137ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));
        trainService.create(new TrainDTO(null, "138ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5)));

        ServiceResult<TrainDTO> result = trainService.findAndFilterAndSortByQueryParams(
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
    public void testPermanentDeleteTrain() {
        TrainDTO trainDTO = new TrainDTO(null, "199ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> createResult = trainService.create(trainDTO);
        Long id = createResult.getEntity().getId();

        ServiceResult<TrainDTO> result = trainService.permanentDelete(id);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());

        ServiceResult<TrainDTO> findResult = trainService.findById(id);
        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, findResult.getStatus());
    }

    @Test
    public void testFindDeletedEntities() {
        TrainDTO trainDTO = new TrainDTO(null, "189ІС", "Львів", "Одеса-Головна", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        ServiceResult<TrainDTO> createResult = trainService.create(trainDTO);
        Long id = createResult.getEntity().getId();

        trainService.delete(id);

        ServiceResult<TrainDTO> result = trainService.findDeletedEntities();

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertTrue(result.getEntityList().stream().anyMatch(train -> train.getId().equals(id)));
    }
}
