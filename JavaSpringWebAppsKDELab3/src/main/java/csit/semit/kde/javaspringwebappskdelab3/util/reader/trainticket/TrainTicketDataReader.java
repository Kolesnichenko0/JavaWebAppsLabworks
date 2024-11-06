package csit.semit.kde.javaspringwebappskdelab3.util.reader.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading train ticket data from a CSV file and converting it into a list of {@link TrainTicketDTO} objects.
 * <p>
 * This class provides a method to read train ticket data from a specified CSV file path. The CSV file is expected to have
 * specific columns in a predefined order. The data is parsed and converted into {@link TrainTicketDTO} objects.
 * </p>
 * <p>
 * The main functionalities provided by this class include:
 * <ul>
 *   <li>Reading train ticket data from a CSV file.</li>
 *   <li>Parsing the CSV data into {@link TrainTicketDTO} objects.</li>
 *   <li>Handling potential I/O exceptions during the file reading process.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * List<TrainTicketDTO> trainTicketData = TrainTicketDataReader.readTrainTicketDataFromCsv("path/to/train_ticket_data.csv");
 * for (TrainTicketDTO trainTicket : trainTicketData) {
 *     // Process the train ticket data
 * }
 * }
 * </pre>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see TrainTicketDTO
 * @since 1.0.0
 */
public class TrainTicketDataReader {
    public static List<TrainTicketDTO> readTrainTicketDataFromCsv(String csvFilePath) {
        List<TrainTicketDTO> trainTicketDTOs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 6) {
                    Long trainId = Long.parseLong(fields[0]);
                    String passengerSurname = fields[1];
                    String passportNumber = fields[2];
                    Integer carriageNumber = Integer.parseInt(fields[3]);
                    Integer seatNumber = Integer.parseInt(fields[4]);
                    LocalDate departureDate = LocalDate.parse(fields[5]);

                    TrainTicketDTO trainTicketDTO = new TrainTicketDTO(null, null, null, null, trainId, passengerSurname, passportNumber, carriageNumber, seatNumber, departureDate);
                    trainTicketDTOs.add(trainTicketDTO);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading train ticket data from CSV file: " + e.getMessage());
        }

        return trainTicketDTOs;
    }
}