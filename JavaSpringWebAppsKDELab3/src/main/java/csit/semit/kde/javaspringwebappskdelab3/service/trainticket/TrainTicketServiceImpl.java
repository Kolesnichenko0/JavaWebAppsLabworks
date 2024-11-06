package csit.semit.kde.javaspringwebappskdelab3.service.trainticket;

import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.entity.train.Train;
import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.Ticket;
import csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket;
import csit.semit.kde.javaspringwebappskdelab3.repository.train.TrainRepository;
import csit.semit.kde.javaspringwebappskdelab3.repository.trainticket.TrainTicketRepository;
import csit.semit.kde.javaspringwebappskdelab3.repository.trainticket.TrainTicketSpecification;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.trainticket.TrainTicketQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TicketFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.trainticket.TrainTicketFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.mapper.trainticket.TrainTicketMapper;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service implementation for managing train tickets.
 * <p>
 * This service provides methods to handle CRUD operations for train tickets,
 * including finding tickets by various criteria, saving, updating, and deleting tickets.
 * It also includes validation to ensure unique fields and proper data integrity.
 * </p>
 * <p>
 * The service interacts with the `TrainTicketRepository` and `TrainRepository` to perform database operations.
 * It uses `TrainTicketMapper` to convert between entity and DTO objects.
 * </p>
 * <p>
 * The service methods return `ServiceResult` objects which encapsulate the status of the operation
 * and any relevant data or error messages.
 * </p>
 *
 * @autor Kolesnychenko Denys Yevhenovych CS-222a
 * @see csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO
 * @see csit.semit.kde.javaspringwebappskdelab3.entity.trainticket.TrainTicket
 * @see csit.semit.kde.javaspringwebappskdelab3.repository.trainticket.TrainTicketRepository
 * @see csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult
 * @see csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus
 * @since 1.0.0
 */
@Service
public class TrainTicketServiceImpl implements TrainTicketService {
    private final TrainTicketRepository trainTicketRepository;

    private final TrainRepository trainRepository;

    @Autowired
    public TrainTicketServiceImpl(TrainTicketRepository trainTicketRepository, TrainRepository trainRepository) {
        this.trainTicketRepository = trainTicketRepository;
        this.trainRepository = trainRepository;
    }

    @Override
    public ServiceResult<TrainTicketDTO> findById(@NonNull Long id) {
        Optional<TrainTicket> trainTicket = trainTicketRepository.findById(id);
        return trainTicket.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, TrainTicketMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    @Override
    public ServiceResult<TrainTicketDTO> findByKeySet(Long trainId, Integer seatNumber, Integer carriageNumber, LocalDate departureDate) {
        try {
            Ticket.validateSeatNumber(seatNumber);
            TrainTicket.validateCarriageNumber(carriageNumber);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        Optional<TrainTicket> trainTicket = trainTicketRepository.findByTrainIdAndSeatNumberAndCarriageNumberAndDepartureDate(trainId, seatNumber, carriageNumber, departureDate);
        return trainTicket.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, TrainTicketMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    @Override
    public ServiceResult<TrainTicketDTO> findByTrainIdAndSeatNumberAndCarriageNumberAndDepartureDate(Long trainId, Integer seatNumber, Integer carriageNumber, LocalDate departureDate) {
        try {
            Ticket.validateSeatNumber(seatNumber);
            TrainTicket.validateCarriageNumber(carriageNumber);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        Optional<TrainTicket> trainTicket = trainTicketRepository.findByTrainIdAndSeatNumberAndCarriageNumberAndDepartureDate(trainId, seatNumber, carriageNumber, departureDate);
        return trainTicket.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, TrainTicketMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    @Override
    public ServiceResult<TrainTicketDTO> findTicketTrains(TrainTicketQueryParams queryParams) {
        List<TrainTicket> trainTickets;

        if (queryParams == null || queryParams.isEmpty()) {
            trainTickets = trainTicketRepository.findAllWithDefaultSort();
        } else {
            trainTickets = trainTicketRepository.findAllWithDefaultSort(TrainTicketSpecification.withQueryParams(queryParams));
        }

        if (trainTickets.isEmpty()) {
            return new ServiceResult<>(ServiceStatus.ENTITIES_NOT_FOUND);
        }

        List<TrainTicketDTO> trainTicketDTOs = trainTickets.stream()
                .map(TrainTicketMapper::toDTO)
                .collect(Collectors.toList());

        return new ServiceResult<>(ServiceStatus.SUCCESS, trainTicketDTOs);
    }

    private ServiceResult<Void> validateUniqueFields(TrainTicket trainTicket, Long excludeId) {
        ServiceResult<TrainTicketDTO> existingTrainTicketResult = findByKeySet(trainTicket.getTrain().getId(), trainTicket.getSeatNumber(), trainTicket.getCarriageNumber(), trainTicket.getDepartureDate());

        if (existingTrainTicketResult.getStatus() == ServiceStatus.SUCCESS) {
            if (excludeId != null && existingTrainTicketResult.getEntity().getId().equals(excludeId)) {
                return new ServiceResult<>(ServiceStatus.SUCCESS);
            }
            return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY, TicketFieldName.SEAT_NUMBER.getRealName());
        } else if (existingTrainTicketResult.getStatus() == ServiceStatus.VALIDATION_ERROR) {
            return new ServiceResult<>(existingTrainTicketResult.getStatus(), existingTrainTicketResult.getFieldName());
        }

        return new ServiceResult<>(ServiceStatus.SUCCESS);
    }

    @Override
    public ServiceResult<TrainTicketDTO> save(@NonNull TrainTicketDTO trainTicketDTO) {
        try {
            Optional<Train> optionalTrain = trainRepository.findById(trainTicketDTO.getTrainId());
            if (optionalTrain.isEmpty()) {
                return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, TrainTicketFieldName.TRAIN.getRealName());
            }

            Train train = optionalTrain.get();
            TrainTicket trainTicket = TrainTicketMapper.toEntity(trainTicketDTO, train);
            ServiceResult<Void> validationResult = validateUniqueFields(trainTicket, null);
            if (validationResult.getStatus() != ServiceStatus.SUCCESS) {
                return new ServiceResult<>(validationResult.getStatus(), validationResult.getFieldName());
            }

            TrainTicket savedTrainTicket = trainTicketRepository.save(trainTicket);
            return new ServiceResult<>(ServiceStatus.SUCCESS, TrainTicketMapper.toDTO(savedTrainTicket));
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        } catch (ConstraintViolationException e) {
            String fieldName = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, fieldName);
        }
    }

    @Override
    public ServiceResult<TrainTicketDTO> saveAll(@NonNull List<TrainTicketDTO> trainTicketDTOs) {
        try {
            List<Long> trainIds = trainTicketDTOs.stream()
                    .map(TrainTicketDTO::getTrainId)
                    .collect(Collectors.toList());
            List<Train> trains = trainRepository.findAllById(trainIds);

            Map<Long, Train> trainMap = trains.stream()
                    .collect(Collectors.toMap(Train::getId, train -> train));

            Set<String> uniqueKeys = new HashSet<>();
            List<TrainTicket> trainTickets = new ArrayList<>();
            for (TrainTicketDTO dto : trainTicketDTOs) {
                String uniqueKey = dto.getTrainId() + "-" + dto.getSeatNumber() + "-" + dto.getCarriageNumber() + "-" + dto.getDepartureDate();
                if (!uniqueKeys.add(uniqueKey)) {
                    return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY, TicketFieldName.SEAT_NUMBER.getRealName());
                }

                Train train = trainMap.get(dto.getTrainId());
                if (train == null) {
                    return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, TrainTicketFieldName.TRAIN.getRealName());
                }
                TrainTicket trainTicket = TrainTicketMapper.toEntity(dto, train);

                ServiceResult<Void> validationResult = validateUniqueFields(trainTicket, null);
                if (validationResult.getStatus() != ServiceStatus.SUCCESS) {
                    return new ServiceResult<>(validationResult.getStatus(), validationResult.getFieldName());
                }

                trainTickets.add(trainTicket);
            }

            List<TrainTicket> savedTrainTickets = trainTicketRepository.saveAll(trainTickets);
            List<TrainTicketDTO> savedTrainTicketDTOs = savedTrainTickets.stream()
                    .map(TrainTicketMapper::toDTO)
                    .collect(Collectors.toList());

            return new ServiceResult<>(ServiceStatus.SUCCESS, savedTrainTicketDTOs);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        } catch (ConstraintViolationException e) {
            String fieldName = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, fieldName);
        }
    }


    public ServiceResult<TrainTicketDTO> updatePartial(Long id, Map<String, Object> updates) {
        Optional<TrainTicket> optionalTicket = trainTicketRepository.findById(id);
        if (optionalTicket.isEmpty()) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }

        try {
            TrainTicket trainTicket = optionalTicket.get();
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                try {
                    switch (key) {
                        case "passengerSurname":
                            trainTicket.setPassengerSurname((String) value);
                            break;
                        case "passportNumber":
                            trainTicket.setPassportNumber((String) value);
                            break;
                        case "seatNumber":
                            trainTicket.setSeatNumber(Integer.parseInt((String) value));
                            break;
                        case "carriageNumber":
                            trainTicket.setCarriageNumber(Integer.parseInt((String) value));
                            break;
                    }
                } catch (NumberFormatException e) {
                    return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, key);
                }
            }

            ServiceResult<Void> validationResult = validateUniqueFields(trainTicket, id);
            if (validationResult.getStatus() != ServiceStatus.SUCCESS) {
                return new ServiceResult<>(validationResult.getStatus(),
                        validationResult.getFieldName());
            }

            trainTicketRepository.save(trainTicket);
            return new ServiceResult<>(ServiceStatus.SUCCESS, TrainTicketMapper.toDTO(trainTicket));
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        } catch (ConstraintViolationException e) {
            String fieldName = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, fieldName);
        }

    }

    @Override
    public ServiceResult<Void> delete(@NonNull Long id) {
        if (!trainTicketRepository.existsById(id)) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }
        trainTicketRepository.deleteById(id);
        return new ServiceResult<>(ServiceStatus.SUCCESS);
    }

    @Override
    public ServiceResult<Void> deleteAll() {
        trainTicketRepository.deleteAll();
        return new ServiceResult<>(ServiceStatus.SUCCESS);
    }
}