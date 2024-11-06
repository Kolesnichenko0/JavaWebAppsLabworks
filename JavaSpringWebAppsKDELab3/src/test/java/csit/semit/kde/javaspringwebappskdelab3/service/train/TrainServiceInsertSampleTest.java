package csit.semit.kde.javaspringwebappskdelab3.service.train;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;
import csit.semit.kde.javaspringwebappskdelab3.util.reader.train.TrainDataReader;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for inserting sample train data into the TrainService.
 * <p>
 * This class contains unit tests for the TrainService implementation, focusing on the insertion of sample train data
 * from a CSV file. The tests ensure that the service methods function correctly when saving multiple train entities.
 * </p>
 * <p>
 * The main functionalities tested in this class include:
 * <ul>
 *   <li>Deleting all existing train entities before each test.</li>
 *   <li>Reading train data from a CSV file.</li>
 *   <li>Saving multiple train entities to the service.</li>
 *   <li>Verifying the successful insertion of train entities.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
@SpringBootTest
public class TrainServiceInsertSampleTest {
    private static final String CSV_FILE_PATH = "./storage/train/train_data.csv";
    @Autowired
    private TrainService trainService;

    @BeforeEach
    public void setUp() {
        trainService.deleteAll();
    }

    @Test
    public void testSaveAll() {
        List<TrainDTO> trainDTOs = TrainDataReader.readTrainDataFromCsv(CSV_FILE_PATH);

        ServiceResult<TrainDTO> result = trainService.saveAll(trainDTOs);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertEquals(trainDTOs.size(), result.getEntityList().size());
    }
}
