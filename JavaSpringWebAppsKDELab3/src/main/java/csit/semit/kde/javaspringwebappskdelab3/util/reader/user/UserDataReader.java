package csit.semit.kde.javaspringwebappskdelab3.util.reader.user;

import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserCreateDTO;
import csit.semit.kde.javaspringwebappskdelab3.entity.user.User;
import csit.semit.kde.javaspringwebappskdelab3.enums.user.Role;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading user data from a CSV file and converting it into a list of {@link User} objects.
 * <p>
 * This class provides a method to read user data from a specified CSV file path. The CSV file is expected to have
 * specific columns in a predefined order. The data is parsed and converted into {@link User} objects.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Reading user data from a CSV file.</li>
 *   <li>Parsing the CSV data into {@link User} objects.</li>
 *   <li>Handling potential I/O exceptions during the file reading process.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * List<User> userData = UserDataReader.readUserDataFromCsv("path/to/user_data.csv");
 * for (User user : userData) {
 *     // Process the user data
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @since 1.0.0
 */
public class UserDataReader {
    public static List<UserCreateDTO> readUserDataFromCsv(String csvFilePath) {
        List<UserCreateDTO> userCreateDTOs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 5) {
                    String username = fields[0];
                    String email = fields[1];
                    String name = fields[2];
                    String password = fields[3];
                    Role role = Role.valueOf(fields[4].toUpperCase());

                    UserCreateDTO userCreateDTO = new UserCreateDTO(username, email, password, name, role);
                    userCreateDTOs.add(userCreateDTO);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading user data from CSV file: " + e.getMessage());
        }

        return userCreateDTOs;
    }
}