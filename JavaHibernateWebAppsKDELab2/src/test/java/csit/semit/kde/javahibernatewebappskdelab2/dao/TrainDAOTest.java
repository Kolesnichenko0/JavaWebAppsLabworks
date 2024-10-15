package csit.semit.kde.javahibernatewebappskdelab2.dao;

import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationStatus;
import csit.semit.kde.javahibernatewebappskdelab2.util.criteria.TrainQueryParams;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationResult;
import csit.semit.kde.javahibernatewebappskdelab2.util.hibernate.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the {@link TrainDAO} class.
 * <p>
 * This class contains unit tests for the {@code TrainDAO} class, specifically for the methods that handle CRUD operations
 * and other database interactions related to the {@link Train} entity. It uses JUnit 5 for testing and includes setup
 * and teardown methods to initialize and clean up resources.
 * </p>
 * <p>
 * The `TrainDAOTest` class includes:
 * <ul>
 *   <li>Setup method to initialize the {@code TrainDAO} instance and truncate the train table</li>
 *   <li>Teardown method to shut down Hibernate</li>
 *   <li>Test methods to verify various CRUD operations and other database interactions</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TrainDAO
 * @see OperationStatus
 * @see OperationResult
 * @see HibernateUtil
 * @since 1.0.0
 */
public class TrainDAOTest {

    private static TrainDAO trainDAO;

    @BeforeAll
    public static void setUp() {
        DAOManager daoManager = new DAOManager(HibernateUtil.getSessionFactory(), HibernateUtil.getValidator());
        assertNotNull(daoManager);
        trainDAO = daoManager.getTrainDAO();
        trainDAO.truncateTable();
    }

    @AfterAll
    public static void tearDown() {
        HibernateUtil.shutdown();
    }


    @Test
    public void testInsert_Success() {
        Train train = new Train("123ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        OperationResult<Train> result = trainDAO.insert(train);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntity());
        assertEquals("123ІС", result.getEntity().getNumber());
    }

    @Test
    public void testInsert_DuplicateEntry() {
        Train train = new Train("123ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        trainDAO.insert(train);

        OperationResult<Train> result = trainDAO.insert(train);
        assertEquals(OperationStatus.DUPLICATE_ENTRY, result.getStatus());
        assertNotNull(result.getFoundFields());
    }

    @Test
    public void testInsert_ValidationError() {
        try {
            Train train = new Train("123ІС", "Київ-2a", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));

            trainDAO.insert(train);

            OperationResult<Train> result = trainDAO.insert(train);
            assertEquals(OperationStatus.VALIDATION_ERROR, result.getStatus());
            assertEquals("arrivalStation", result.getFieldName());
        } catch (IllegalArgumentException e) {
            // Ignore
        }
    }

    @Test
    public void testUpdate_Success() {
        Train train = new Train("124ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.now(), Duration.ofHours(3));
        OperationResult<Train> insertResult = trainDAO.insert(train);
        Long id = insertResult.getEntity().getId();

        train.setDepartureStation("Одеса Головна");
        OperationResult<Train> updateResult = trainDAO.update(id, train);
        assertEquals(OperationStatus.SUCCESS, updateResult.getStatus());
        assertEquals("Одеса Головна", updateResult.getEntity().getDepartureStation());
    }

    @Test
    public void testUpdate_NonExistingEntity() {
        Train train = new Train("567ІС", "Миколаїв", "Київ", MovementType.DAILY, LocalTime.now(), Duration.ofHours(4));
        OperationResult<Train> result = trainDAO.update(999L, train);
        assertEquals(OperationStatus.ENTITY_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testDelete_Success() {
        Train train = new Train("125ІС", "Київ", "Полтава", MovementType.DAILY, LocalTime.now(), Duration.ofHours(2));
        OperationResult<Train> insertResult = trainDAO.insert(train);
        Long id = insertResult.getEntity().getId();

        OperationResult<Train> deleteResult = trainDAO.delete(id);
        assertEquals(OperationStatus.SUCCESS, deleteResult.getStatus());
    }

    @Test
    public void testDelete_NonExistingEntity() {
        OperationResult<Train> result = trainDAO.delete(999L);
        assertEquals(OperationStatus.ENTITY_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testRestoreById_Success() {
        Train train = new Train("126ІС", "Полтава", "Тернопіль", MovementType.DAILY, LocalTime.now(), Duration.ofHours(6));
        OperationResult<Train> insertResult = trainDAO.insert(train);
        Long id = insertResult.getEntity().getId();

        trainDAO.delete(id);
        OperationResult<Train> restoreResult = trainDAO.restoreById(id);
        assertEquals(OperationStatus.SUCCESS, restoreResult.getStatus());

        Train restoredTrain = trainDAO.findById(id).getEntity();
        assertFalse(restoredTrain.getIsDeleted());
    }

    @Test
    public void testRestoreById_AlreadyActiveEntity() {
        Train train = new Train("127ІС", "Рівне", "Полтава", MovementType.DAILY, LocalTime.now(), Duration.ofHours(2));
        OperationResult<Train> insertResult = trainDAO.insert(train);
        Long id = insertResult.getEntity().getId();

        OperationResult<Train> restoreResult = trainDAO.restoreById(id);
        assertEquals(OperationStatus.ENTITY_ALREADY_ACTIVE, restoreResult.getStatus());
    }

    @Test
    public void testRestoreByNumber_Success() {
        Train train = new Train("207ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        OperationResult<Train> insertResult = trainDAO.insert(train);
        Long id = insertResult.getEntity().getId();
        trainDAO.delete(id);

        OperationResult<Train> restoreResult = trainDAO.restoreByNumber("207ІС");
        assertEquals(OperationStatus.SUCCESS, restoreResult.getStatus());

        Train restoredTrain = trainDAO.findById(id).getEntity();
        assertFalse(restoredTrain.getIsDeleted());
    }

    @Test
    public void testRestoreTrainByNumber_EntityNotFound() {
        OperationResult<Train> restoreResult = trainDAO.restoreByNumber("991ІС");
        assertEquals(OperationStatus.ENTITY_NOT_FOUND, restoreResult.getStatus());
    }

    @Test
    public void testFindByNumber_IncludeDeletedFalse() {
        Train train = new Train("128ІС", "Біла церква", "Київ", MovementType.ODD_DAYS, LocalTime.now(), Duration.ofHours(4));
        trainDAO.insert(train);
        Long id = train.getId();

        trainDAO.delete(id);

        OperationResult<Train> result = trainDAO.findByNumber("128ІС", false);
        assertNull(result.getEntity());
    }

    @Test
    public void testFindByNumber_IncludeDeletedTrue() {
        Train train = new Train("129ІС", "Івано-Франківськ", "Рівне", MovementType.EVEN_DAYS, LocalTime.now(), Duration.ofHours(3));
        trainDAO.insert(train);
        Long id = train.getId();

        trainDAO.delete(id);

        OperationResult<Train> result = trainDAO.findByNumber("129ІС", true);
        assertNotNull(result.getEntity());
    }

    @Test
    public void testFindByKeySet_ValidTemplate() {
        Train train = new Train("130ІС", "Київ", "Харків", MovementType.DAILY, LocalTime.now(), Duration.ofHours(2));
        trainDAO.insert(train);

        Train template = new Train();
        template.setNumber("130ІС");

        OperationResult<Train> result = trainDAO.findByKeySet(template, false);
        Train resultEntity = result.getEntity();

        assertEquals("130ІС", resultEntity.getNumber());
    }

    @Test
    public void testFindById_Success() {
        Train train = new Train("131ІС", "Одеса", "Суми", MovementType.DAILY, LocalTime.now(), Duration.ofHours(2));
        OperationResult<Train> insertResult = trainDAO.insert(train);
        Long id = insertResult.getEntity().getId();

        OperationResult<Train> findResult = trainDAO.findById(id);
        assertEquals(OperationStatus.SUCCESS, findResult.getStatus());
        assertNotNull(findResult.getEntity());
        assertEquals("131ІС", findResult.getEntity().getNumber());
    }

    @Test
    public void testFindById_NonExistingEntity() {
        OperationResult<Train> findResult = trainDAO.findById(999L);
        assertEquals(OperationStatus.ENTITY_NOT_FOUND, findResult.getStatus());
    }

    @Test
    public void testGetAllList() {
        Train train1 = new Train("132ІС", "Полтава", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        trainDAO.insert(train1);

        Train train2 = new Train("133ІС", "Тернопіль", "Полтава", MovementType.EVEN_DAYS, LocalTime.now(), Duration.ofHours(2));
        trainDAO.insert(train2);

        OperationResult<Train> result = trainDAO.getAllList(false);
        assertTrue(result.getEntityList().size() >= 2);
    }

    @Test
    public void testFindByArrivalStation_Success() {
        Train train = new Train("134ІС", "Кииїв-Волинський", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        trainDAO.insert(train);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .searchingArrivalStation("Кииїв-В")
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals("Кииїв-Волинський", result.getEntityList().get(0).getArrivalStation());
    }

    @Test
    public void testFindByArrivalStation_NotFound() {
        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .searchingArrivalStation("Неіснуюча станція")
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.ENTITIES_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testFindByDepartureStation_Success() {
        Train train = new Train("135ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        trainDAO.insert(train);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .searchingDepartureStation("Львів")
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals("Львів", result.getEntityList().get(0).getDepartureStation());
    }

    @Test
    public void testFindByDepartureStation_NotFound() {
        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .searchingDepartureStation("Неіснуюча станція")
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.ENTITIES_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testFindByArrivalAndDeparture_Success() {
        Train train = new Train("245ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        trainDAO.insert(train);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .searchingArrivalStation("Київ")
                .searchingDepartureStation("Львів")
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals("Київ", result.getEntityList().get(0).getArrivalStation());
        assertEquals("Львів", result.getEntityList().get(0).getDepartureStation());
    }

    @Test
    public void testFindByArrivalAndDeparture_NotFound() {
        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .searchingArrivalStation("Київ")
                .searchingDepartureStation("Неіснуюча станція")
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.ENTITIES_NOT_FOUND, result.getStatus());
    }

    @Test
    public void testFilterByMovementType() {
        Train train1 = new Train("137ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        Train train2 = new Train("138ІС", "Одеса", "Харків", MovementType.EVEN_DAYS, LocalTime.now(), Duration.ofHours(6));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        Set<MovementType> movementTypes = Set.of(MovementType.DAILY);
        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .filteringMovementTypes(movementTypes)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertTrue(result.getEntityList().size() >= 1);
    }

    @Test
    public void testFilterByDepartureTimeRange() {
        Train train1 = new Train("139ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.of(7, 21), Duration.ofHours(5));
        Train train2 = new Train("140ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.of(7, 26), Duration.ofHours(6));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        LocalTime from = LocalTime.of(7, 21);
        LocalTime to = LocalTime.of(7, 25);
        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .filteringFrom(from)
                .filteringTo(to)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals(1, result.getEntityList().size());
        assertEquals("139ІС", result.getEntityList().get(0).getNumber());
    }

    @Test
    public void testFilterByDurationRange() {
        Train train1 = new Train("141ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(21));
        Train train2 = new Train("142ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.now(), Duration.ofHours(23));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        Duration minDuration = Duration.ofHours(21);
        Duration maxDuration = Duration.ofHours(22);
        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .filteringMinDuration(minDuration)
                .filteringMaxDuration(maxDuration)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
    }

    @Test
    public void testSortByTrainNumberAsc() {
        Train train1 = new Train("001ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        Train train2 = new Train("144ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.now(), Duration.ofHours(6));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .sortedByTrainNumberAsc(true)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals("001ІС", result.getEntityList().get(0).getNumber());
    }

    @Test
    public void testSortByTrainNumberDesc() {
        Train train1 = new Train("145ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        Train train2 = new Train("298ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.now(), Duration.ofHours(6));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .sortedByTrainNumberAsc(false)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals("298ІС", result.getEntityList().get(0).getNumber());
    }

    @Test
    public void testSortByDurationAsc() {
        Train train1 = new Train("147ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(1));
        Train train2 = new Train("148ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.now(), Duration.ofHours(6));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .sortedByDurationAsc(true)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals("147ІС", result.getEntityList().get(0).getNumber());
    }

    @Test
    public void testSortByDurationDesc() {
        Train train1 = new Train("149ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.now(), Duration.ofHours(5));
        Train train2 = new Train("150ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.now(), Duration.ofHours(24));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .sortedByDurationAsc(false)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
    }

    @Test
    public void testSortByDepartureTimeAsc() {
        Train train1 = new Train("151ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.of(0, 0), Duration.ofHours(5));
        Train train2 = new Train("152ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.of(12, 0), Duration.ofHours(6));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .sortedByDepartureTimeAsc(true)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals("151ІС", result.getEntityList().get(0).getNumber());
    }

    @Test
    public void testSortByDepartureTimeDesc() {
        Train train1 = new Train("153ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.of(10, 0), Duration.ofHours(5));
        Train train2 = new Train("154ІС", "Одеса", "Харків", MovementType.DAILY, LocalTime.of(23, 59), Duration.ofHours(6));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .sortedByDepartureTimeAsc(false)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
        assertEquals("154ІС", result.getEntityList().get(0).getNumber());
    }

    @Test
    public void testFilterAndSortByMultipleQueryParams() {
        Train train1 = new Train("155ІС", "Київ", "Львів", MovementType.DAILY, LocalTime.of(22, 55), Duration.ofHours(21));
        Train train2 = new Train("156ІС", "Одеса", "Харків", MovementType.EVEN_DAYS, LocalTime.of(23, 0), Duration.ofHours(23));
        trainDAO.insert(train1);
        trainDAO.insert(train2);

        Set<MovementType> movementTypes = Set.of(MovementType.DAILY);
        LocalTime from = LocalTime.of(22, 54);
        LocalTime to = LocalTime.of(22, 56);
        Duration minDuration = Duration.ofHours(20);
        Duration maxDuration = Duration.ofHours(23);

        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .filteringMovementTypes(movementTypes)
                .filteringFrom(from)
                .filteringTo(to)
                .filteringMinDuration(minDuration)
                .filteringMaxDuration(maxDuration)
                .sortedByTrainNumberAsc(true)
                .build();

        OperationResult<Train> result = trainDAO.findAndFilterAndSortByQueryParams(queryParams);
        assertEquals(OperationStatus.SUCCESS, result.getStatus());
        assertNotNull(result.getEntityList());
        assertFalse(result.getEntityList().isEmpty());
    }
}
