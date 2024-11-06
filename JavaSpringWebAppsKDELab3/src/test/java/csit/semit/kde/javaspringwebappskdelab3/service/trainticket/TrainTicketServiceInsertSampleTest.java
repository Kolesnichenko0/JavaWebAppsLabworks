package csit.semit.kde.javaspringwebappskdelab3.service.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.util.reader.trainticket.TrainTicketDataReader;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for inserting sample train ticket data into the TrainTicketService.
 * <p>
 * This class contains unit tests for the TrainTicketService implementation, focusing on the insertion of sample train ticket data
 * from a CSV file. The tests ensure that the service methods function correctly when saving multiple train ticket entities.
 * </p>
 * <p>
 * The main functionalities tested in this class include:
 * <ul>
 *   <li>Deleting all existing train ticket entities before each test.</li>
 *   <li>Reading train ticket data from a CSV file.</li>
 *   <li>Saving multiple train ticket entities to the service.</li>
 *   <li>Verifying the successful insertion of train ticket entities.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @since 1.0.0
 */
@SpringBootTest
public class TrainTicketServiceInsertSampleTest {
    private static final String CSV_FILE_PATH = "./storage/trainticket/traintickets_data.csv";
    @Autowired
    private TrainTicketService trainTicketService;

    @BeforeEach
    public void setUp() {
        trainTicketService.deleteAll();
    }

    @Test
    public void testSaveAll() {
        List<TrainTicketDTO> trainTicketDTOs = TrainTicketDataReader.readTrainTicketDataFromCsv(CSV_FILE_PATH);

        ServiceResult<TrainTicketDTO> result = trainTicketService.saveAll(trainTicketDTOs);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertEquals(trainTicketDTOs.size(), result.getEntityList().size());
    }
}