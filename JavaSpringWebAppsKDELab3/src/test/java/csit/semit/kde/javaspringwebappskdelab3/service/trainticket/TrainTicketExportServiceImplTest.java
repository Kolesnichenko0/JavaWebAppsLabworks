package csit.semit.kde.javaspringwebappskdelab3.service.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketExportRequestDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.export.ExportFormat;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket.TrainTicketQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the TrainTicketExportServiceImpl.
 * <p>
 * This class contains unit tests for the TrainTicketExportServiceImpl implementation, focusing on various train ticket export operations.
 * The tests ensure that the service methods function correctly when exporting and sending train ticket data.
 * </p>
 * <p>
 * The main functionalities tested in this class include:
 * <ul>
 *   <li>Exporting and sending train ticket data in different formats (CSV, EXCEL) and verifying the successful operation.</li>
 *   <li>Handling cases where the user is not found during the export operation.</li>
 * </ul>
 * </p>
 * <p>
 * The tests utilize the following components:
 * <ul>
 *   <li>{@link TrainTicketExportServiceImpl} - The service implementation being tested.</li>
 *   <li>{@link TrainTicketQueryParams} - The query parameters for filtering and sorting train ticket data.</li>
 *   <li>{@link TrainTicketExportRequestDTO} - The data transfer object for train ticket export requests.</li>
 * </ul>
 * </p>
 * <p>
 * The tests are executed within a Spring Boot test context to leverage dependency injection and other Spring features.
 * </p>
 * <p>
 * Each test method is annotated with {@link Test} to indicate that it is a test case. The {@link ParameterizedTest} annotation
 * is used for parameterized tests, and {@link EnumSource} is used to provide different export formats.
 * </p>
 * <p>
 * The test methods use assertions from the {@link org.junit.jupiter.api.Assertions} class to verify the expected outcomes.
 * </p>
 * <p>
 * The tests cover scenarios such as successful export and send operations, and handling of non-existent users.
 * </p>
 * <p>
 * The test class uses a constant {@code TEST_USERNAME} to represent a valid username for testing purposes.
 * </p>
 * <p>
 * The {@code testExportAndSendTrainTickets_Success} method tests the successful export and send operation for different formats.
 * The {@code testExportAndSendTrainTickets_UserNotFound} method tests the scenario where the user is not found.
 * </p>
 * <p>
 * The {@code trainTicketExportService} is autowired to leverage Spring's dependency injection.
 * </p>
 * <p>
 * The {@code TrainTicketQueryParams} and {@code TrainTicketExportRequestDTO} are used to create request objects for the service methods.
 * </p>
 * <p>
 * The {@code ServiceResult} and {@code ServiceStatus} classes are used to handle and verify the results of the service methods.
 * </p>
 * <p>
 * The test methods use assertions to verify that the service methods return the expected results.
 * </p>
 * <p>
 * The test class is annotated with {@link SpringBootTest} to indicate that it is a Spring Boot test.
 * </p>
 * <p>
 * The test methods are designed to be self-contained and independent of each other.
 * </p>
 * <p>
 * The test class is intended to ensure the correctness and reliability of the TrainTicketExportServiceImpl implementation.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TrainTicketExportServiceImpl
 * @see TrainTicketQueryParams
 * @see TrainTicketExportRequestDTO
 * @see org.junit.jupiter.api.Assertions
 * @see org.junit.jupiter.api.Test
 * @see org.junit.jupiter.params.ParameterizedTest
 * @see org.junit.jupiter.params.provider.EnumSource
 * @see org.springframework.boot.test.context.SpringBootTest
 * @since 1.0.0
 */
@SpringBootTest
public class TrainTicketExportServiceImplTest {

    private static final String TEST_USERNAME = "denys";

    @Autowired
    private TrainTicketExportServiceImpl trainTicketExportService;

    @ParameterizedTest
    @EnumSource(value = ExportFormat.class, names = {"CSV", "EXCEL"})
    public void testExportAndSendTrainTickets_Success(ExportFormat format) {
        TrainTicketQueryParams queryParams = new TrainTicketQueryParams.Builder()
                .searchingTrainId(1L)
                .build();

        TrainTicketExportRequestDTO request = new TrainTicketExportRequestDTO(format, queryParams);

//        ServiceResult<TrainTicketDTO> result = trainTicketExportService.exportAndSendTrainTickets(request, 1L, TEST_USERNAME);
        ServiceResult<TrainTicketDTO> result = trainTicketExportService.exportAndSendTrainTickets(request, null, TEST_USERNAME);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
    }

    @Test
    public void testExportAndSendTrainTickets_UserNotFound() {
        TrainTicketExportRequestDTO request = new TrainTicketExportRequestDTO();

        ServiceResult<TrainTicketDTO> result = trainTicketExportService.exportAndSendTrainTickets(request, 1L, "nonExistentUser");

        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, result.getStatus());
    }
}