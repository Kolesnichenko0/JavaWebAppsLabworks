package csit.semit.kde.javaspringwebappskdelab3.service.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketExportRequestDTO;
import csit.semit.kde.javaspringwebappskdelab3.enums.export.ExportFormat;
import csit.semit.kde.javaspringwebappskdelab3.service.export.ExportService;
import csit.semit.kde.javaspringwebappskdelab3.service.mail.EmailContentService;
import csit.semit.kde.javaspringwebappskdelab3.service.mail.MailService;
import csit.semit.kde.javaspringwebappskdelab3.service.storage.FileStorageService;
import csit.semit.kde.javaspringwebappskdelab3.service.user.UserService;
import csit.semit.kde.javaspringwebappskdelab3.dto.user.UserDTO;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of TrainTicketExportService for exporting train tickets.
 * <p>
 * This service provides functionality to export train ticket data based on the provided request parameters and send the exported data via email.
 * The service supports different export formats and handles the entire process from data retrieval to email sending.
 * </p>
 * <p>
 * The `TrainTicketExportServiceImpl` class includes:
 * <ul>
 *   <li>Dependency injection of various services such as {@link TrainTicketService}, {@link ExportService}, {@link MailService}, {@link UserService}, {@link EmailContentService}, and {@link FileStorageService}.</li>
 *   <li>Method to export train ticket data and send it via email, specifying the request parameters, train ID, and the username of the requester.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private TrainTicketExportService trainTicketExportService;
 *
 * public void someMethod() {
 *     TrainTicketExportRequestDTO request = new TrainTicketExportRequestDTO();
 *     // Set request parameters
 *     ServiceResult<TrainTicketDTO> result = trainTicketExportService.exportAndSendTrainTickets(request, 123L, "username");
 *     // Handle the result
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainTicketService}</li>
 *   <li>{@link ExportService}</li>
 *   <li>{@link MailService}</li>
 *   <li>{@link UserService}</li>
 *   <li>{@link EmailContentService}</li>
 *   <li>{@link FileStorageService}</li>
 *   <li>{@link TrainTicketExportRequestDTO}</li>
 *   <li>{@link TrainTicketDTO}</li>
 *   <li>{@link ServiceResult}</li>
 *   <li>{@link ServiceStatus}</li>
 *   <li>{@link ExportFormat}</li>
 *   <li>{@link LocalDate}</li>
 *   <li>{@link DateTimeFormatter}</li>
 * </ul>
 * </p>
 * <p>
 * This class is annotated with {@link Service} to indicate that it's a Spring service component.
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych CS-222a
 * @see TrainTicketService
 * @see ExportService
 * @see MailService
 * @see UserService
 * @see EmailContentService
 * @see FileStorageService
 * @see TrainTicketExportRequestDTO
 * @see TrainTicketDTO
 * @see ServiceResult
 * @see ServiceStatus
 * @see ExportFormat
 * @see LocalDate
 * @see DateTimeFormatter
 * @since 1.0.0
 */
@Service
public class TrainTicketExportServiceImpl implements TrainTicketExportService {

    private final TrainTicketService trainTicketService;
    private final ExportService exportService;
    private final MailService mailService;
    private final UserService userService;
    private final EmailContentService emailContentService;
    private final FileStorageService fileStorageService;

    @Autowired
    public TrainTicketExportServiceImpl(TrainTicketService trainTicketService,
                                        ExportService exportService,
                                        MailService mailService,
                                        UserService userService,
                                        EmailContentService emailContentService,
                                        FileStorageService fileStorageService) {
        this.trainTicketService = trainTicketService;
        this.exportService = exportService;
        this.mailService = mailService;
        this.userService = userService;
        this.emailContentService = emailContentService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ServiceResult<TrainTicketDTO> exportAndSendTrainTickets(TrainTicketExportRequestDTO request, Long trainId, String username) {
        ServiceResult<UserDTO> userResult = userService.findByUsername(username);
        if (userResult.getStatus() != ServiceStatus.SUCCESS) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND, "User not found or has no email");
        }
        UserDTO user = userResult.getEntity();
        String email = user.getEmail();
        if (email == null || email.isEmpty()) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, "User email is not set");
        }

        ServiceResult<TrainTicketDTO> ticketResult = trainTicketService.findTicketTrains(request.getQueryParams());
        if (ticketResult.getStatus() != ServiceStatus.SUCCESS) {
            return new ServiceResult<>(ticketResult.getStatus(), "No tickets found for the given parameters");
        }
        List<TrainTicketDTO> tickets = ticketResult.getEntityList();

        if (tickets.isEmpty()) {
            return new ServiceResult<>(ServiceStatus.ENTITIES_NOT_FOUND, "No ticket data available for export");
        }

        String trainInfo = "";
        if (trainId != null) {
            Optional<TrainTicketDTO> firstTicketOpt = tickets.stream().findFirst();
            if (firstTicketOpt.isPresent()) {
                TrainTicketDTO firstTicket = firstTicketOpt.get();
                trainInfo = "Exported for Train " + firstTicket.getTrainNumber();
            }
        }

        byte[] fileBytes;
        try {
            ExportFormat format = request.getFormat();
            fileBytes = exportService.exportData(tickets, format);
            if (fileBytes.length == 0) {
                return new ServiceResult<>(ServiceStatus.UNKNOWN_ERROR, "Failed to generate " + format + " file");
            }
        } catch (UnsupportedOperationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getMessage());
        } catch (Exception e) {
            return new ServiceResult<>(ServiceStatus.UNKNOWN_ERROR, "Error during file generation: " + e.getMessage());
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", user.getName());
        variables.put("dataType", "train ticket data");
        variables.put("format", request.getFormat().name());
        if (!trainInfo.isEmpty()) {
            variables.put("trainInfo", trainInfo);
        }

        String htmlContent = emailContentService.generateEmailContent("email/export", variables);

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String attachmentName = "train_tickets_" + date + "." + request.getFormat().getExtension();

        try {
            String subject = "Train Ticket Data Export (" + request.getFormat() + ")";
            mailService.sendEmailWithAttachment(email, subject, htmlContent, true, attachmentName, fileBytes);
        } catch (Exception e) {
            return new ServiceResult<>(ServiceStatus.UNKNOWN_ERROR, "Error during email sending: " + e.getMessage());
        }

        try {
            fileStorageService.saveFile(username, request.getFormat().name(), attachmentName, fileBytes);
        } catch (Exception e) {
            System.err.println("Error during local file saving: " + e.getMessage());
        }

        return new ServiceResult<>(ServiceStatus.SUCCESS);
    }
}
