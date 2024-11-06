package csit.semit.kde.javaspringwebappskdelab3.util.reader.train;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.train.MovementType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading train data from a CSV file and converting it into a list of {@link TrainDTO} objects.
 * <p>
 * This class provides a method to read train data from a specified CSV file path. The CSV file is expected to have
 * specific columns in a predefined order. The data is parsed and converted into {@link TrainDTO} objects.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Reading train data from a CSV file.</li>
 *   <li>Parsing the CSV data into {@link TrainDTO} objects.</li>
 *   <li>Handling potential I/O exceptions during the file reading process.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * List<TrainDTO> trainData = TrainDataReader.readTrainDataFromCsv("path/to/train_data.csv");
 * for (TrainDTO train : trainData) {
 *     // Process the train data
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see TrainDTO
 * @see MovementType
 * @since 1.0.0
 */
public class TrainDataReader {
    public static List<TrainDTO> readTrainDataFromCsv(String csvFilePath) {
        List<TrainDTO> trainDTOs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 6) {
                    String trainNumber = fields[0];
                    String departureStation = fields[1];
                    String arrivalStation = fields[2];
                    MovementType movementType = MovementType.getByDisplayName(fields[3]);
                    LocalTime departureTime = LocalTime.parse(fields[4]);
                    Duration duration = Duration.ofSeconds(Long.parseLong(fields[5]));

                    TrainDTO trainDTO = new TrainDTO(null, trainNumber, departureStation, arrivalStation, movementType, departureTime, duration);
                    trainDTOs.add(trainDTO);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading train data from CSV file: " + e.getMessage());
        }

        return trainDTOs;
    }
}