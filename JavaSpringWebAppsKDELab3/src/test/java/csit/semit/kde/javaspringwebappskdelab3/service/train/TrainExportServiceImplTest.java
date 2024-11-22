package csit.semit.kde.javaspringwebappskdelab3.service.train;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainExportRequestDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.export.ExportFormat;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.train.TrainQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for the TrainExportServiceImpl.
 * <p>
 * This class contains unit tests for the TrainExportServiceImpl implementation, focusing on various train export operations.
 * The tests ensure that the service methods function correctly when exporting and sending train data.
 * </p>
 * <p>
 * The main functionalities tested in this class include:
 * <ul>
 *   <li>Exporting and sending train data in different formats (CSV, EXCEL) and verifying the successful operation.</li>
 *   <li>Handling cases where the user is not found during the export operation.</li>
 * </ul>
 * </p>
 * <p>
 * The tests utilize the following components:
 * <ul>
 *   <li>{@link TrainExportServiceImpl} - The service implementation being tested.</li>
 *   <li>{@link TrainQueryParams} - The query parameters for filtering and sorting train data.</li>
 *   <li>{@link TrainExportRequestDTO} - The data transfer object for train export requests.</li>
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
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TrainExportServiceImpl
 * @see TrainQueryParams
 * @see TrainExportRequestDTO
 * @see org.junit.jupiter.api.Assertions
 * @see org.junit.jupiter.api.Test
 * @see org.junit.jupiter.params.ParameterizedTest
 * @see org.junit.jupiter.params.provider.EnumSource
 * @see org.springframework.boot.test.context.SpringBootTest
 * @since 1.0.0
 */
@SpringBootTest
public class TrainExportServiceImplTest {

    private static final String TEST_USERNAME = "denys";

    @Autowired
    private TrainExportServiceImpl trainExportService;

    @ParameterizedTest
    @EnumSource(value = ExportFormat.class, names = {"CSV", "EXCEL"})
    public void testExportAndSendTrains_Success(ExportFormat format) {
        TrainQueryParams queryParams = new TrainQueryParams.Builder()
                .filteringMovementTypes(Set.of(MovementType.DAILY))
                .sortedByTrainNumberAsc(true)
                .build();

        TrainExportRequestDTO request = new TrainExportRequestDTO(format, queryParams);

        ServiceResult<TrainDTO> result = trainExportService.exportAndSendTrains(request, TEST_USERNAME);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
    }


    @Test
    public void testExportAndSendTrains_UserNotFound() {
        TrainExportRequestDTO request = new TrainExportRequestDTO();

        ServiceResult<TrainDTO> result = trainExportService.exportAndSendTrains(request, "nonExistentUser");

        assertEquals(ServiceStatus.ENTITY_NOT_FOUND, result.getStatus());
    }
}