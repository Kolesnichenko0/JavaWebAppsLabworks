package csit.semit.kde.javaspringwebappskdelab3.service.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserCreateDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.repository.user.UserRepository;
import csit.semit.kde.javaspringwebappskdelab3.util.reader.user.UserDataReader;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for inserting sample user data into the UserService.
 * <p>
 * This class contains unit tests for the UserService implementation, focusing on the insertion of sample user data
 * from a CSV file. The tests ensure that the service methods function correctly when saving multiple user entities.
 * </p>
 * <p>
 * The main functionalities tested in this class include:
 * <ul>
 *   <li>Deleting all existing user entities before each test.</li>
 *   <li>Reading user data from a CSV file.</li>
 *   <li>Saving multiple user entities to the service.</li>
 *   <li>Verifying the successful insertion of user entities.</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
@SpringBootTest
public class UserServiceInsertSampleTest {
    private static final String CSV_FILE_PATH = "./storage/user/user_data.csv";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testSaveAll() {
        List<UserCreateDTO> users = UserDataReader.readUserDataFromCsv(CSV_FILE_PATH);

        ServiceResult<UserDTO> result = userService.saveAll(users);

        assertEquals(ServiceStatus.SUCCESS, result.getStatus());
        assertEquals(users.size(), result.getEntityList().size());
    }
}