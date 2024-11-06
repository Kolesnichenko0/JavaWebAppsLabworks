package csit.semit.kde.javaspringwebappskdelab3.service.train;

import csit.semit.kde.javaspringwebappskdelab3.dto.train.TrainDTO;
import csit.semit.kde.javaspringwebappskdelab3.dto.trainticket.TrainTicketDTO;
import csit.semit.kde.javaspringwebappskdelab3.entity.train.Train;
import csit.semit.kde.javaspringwebappskdelab3.repository.train.TrainRepository;
import csit.semit.kde.javaspringwebappskdelab3.repository.train.TrainSpecification;
import csit.semit.kde.javaspringwebappskdelab3.util.criteria.train.TrainQueryParams;
import csit.semit.kde.javaspringwebappskdelab3.util.mapper.train.TrainMapper;
import csit.semit.kde.javaspringwebappskdelab3.util.mapper.trainticket.TrainTicketMapper;
import csit.semit.kde.javaspringwebappskdelab3.util.result.entity.FieldValidationException;
import csit.semit.kde.javaspringwebappskdelab3.util.fieldname.train.TrainFieldName;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceResult;
import csit.semit.kde.javaspringwebappskdelab3.util.result.service.ServiceStatus;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service implementation for managing train-related operations.
 * <p>
 * This class provides methods for performing CRUD operations and other business logic
 * related to {@link TrainDTO} entities. It also includes methods for querying train tickets
 * associated with a specific train.
 * </p>
 * <p>
 * The main functionalities provided by this service include:
 * <ul>
 *   <li>Finding a train by its ID, key set, or number.</li>
 *   <li>Querying trains based on various parameters.</li>
 *   <li>Saving and updating train information.</li>
 *   <li>Deleting trains individually or in bulk.</li>
 *   <li>Retrieving train tickets associated with a specific train.</li>
 * </ul>
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @Autowired
 * private TrainService trainService;
 *
 * public void someMethod() {
 *     ServiceResult<TrainDTO> result = trainService.findById(1L);
 *     if (result.isSuccess()) {
 *         TrainDTO train = result.getData();
 *         // Process the train data
 *     }
 * }
 * }
 * </pre>
 * </p>
 * <p>
 * Dependencies:
 * <ul>
 *   <li>{@link TrainDTO}</li>
 *   <li>{@link TrainTicketDTO}</li>
 *   <li>{@link TrainQueryParams}</li>
 *   <li>{@link ServiceResult}</li>
 * </ul>
 * </p>
 *
 * @author Kolesnychenko Denys Yevhenovych
 * @see TrainDTO
 * @see TrainTicketDTO
 * @see TrainQueryParams
 * @see ServiceResult
 * @since 1.0.0
 */
@Service
public class TrainServiceImpl implements TrainService {
    private final TrainRepository trainRepository;

    @Autowired
    public TrainServiceImpl(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public ServiceResult<TrainDTO> findById(@NonNull Long id) {
        Optional<Train> train = trainRepository.findById(id);
        return train.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, TrainMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    @Override
    public ServiceResult<TrainDTO> findByKeySet(String number) {
        try {
            number = Train.validateNumber(number);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        Optional<Train> train = trainRepository.findByNumber(number);
        return train.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, TrainMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    @Override
    public ServiceResult<TrainDTO> findByNumber(String number) {
        try {
            number = Train.validateNumber(number);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        Optional<Train> train = trainRepository.findByNumber(number);
        return train.map(value -> new ServiceResult<>(ServiceStatus.SUCCESS, TrainMapper.toDTO(value)))
                .orElseGet(() -> new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND));
    }

    @Override
    public ServiceResult<TrainDTO> findTrains(TrainQueryParams queryParams) {
        List<Train> trains;

        if (queryParams == null || queryParams.isEmpty()) {
            trains = trainRepository.findAll();
        } else {
            trains = trainRepository.findAll(TrainSpecification.withQueryParams(queryParams));
        }

        if (trains.isEmpty()) {
            return new ServiceResult<>(ServiceStatus.ENTITIES_NOT_FOUND);
        }

        List<TrainDTO> trainDTOs = trains.stream()
                .map(TrainMapper::toDTO)
                .collect(Collectors.toList());

        return new ServiceResult<>(ServiceStatus.SUCCESS, trainDTOs);
    }

    @Override
    @Transactional
    public ServiceResult<TrainTicketDTO> getTrainTickets(@NonNull Long id) {
        Optional<Train> train = trainRepository.findById(id);
        if (train.isEmpty()) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }

        List<TrainTicketDTO> trainTicketDTOs = train.get().getTrainTickets().stream()
                .map(TrainTicketMapper::toDTO)
                .collect(Collectors.toList());

        if (trainTicketDTOs.isEmpty()) {
            return new ServiceResult<>(ServiceStatus.ENTITIES_NOT_FOUND);
        }

        return new ServiceResult<>(ServiceStatus.SUCCESS, trainTicketDTOs);
    }

    private ServiceResult<Void> validateUniqueFields(Train train, Long excludeId) {
        ServiceResult<TrainDTO> existingTrainResult = findByKeySet(train.getNumber());

        if (existingTrainResult.getStatus() == ServiceStatus.SUCCESS) {
            if (excludeId != null && TrainMapper.toEntity(existingTrainResult.getEntity()).getId().equals(excludeId)) {
                return new ServiceResult<>(ServiceStatus.SUCCESS);
            }
            return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY, TrainFieldName.NUMBER.getRealName());
        } else if (existingTrainResult.getStatus() == ServiceStatus.VALIDATION_ERROR) {
            return new ServiceResult<>(existingTrainResult.getStatus(), existingTrainResult.getFieldName());
        }

        return new ServiceResult<>(ServiceStatus.SUCCESS);
    }

    @Override
    public ServiceResult<TrainDTO> save(@NonNull TrainDTO trainDTO) {
        try {
            Train train = TrainMapper.toEntity(trainDTO);

            ServiceResult<Void> validationResult = validateUniqueFields(train, null);
            if (validationResult.getStatus() != ServiceStatus.SUCCESS) {
                return new ServiceResult<>(validationResult.getStatus(),
                        validationResult.getFieldName());
            }

            Train savedTrain = trainRepository.save(train);
            return new ServiceResult<>(ServiceStatus.SUCCESS, TrainMapper.toDTO(savedTrain));
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        } catch (ConstraintViolationException e) {
            String fieldName = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, fieldName);
        }
    }

    @Override
    public ServiceResult<TrainDTO> saveAll(@NonNull List<TrainDTO> trainDTOs) {
        try {
            List<String> numbers = trainDTOs.stream()
                    .map(TrainDTO::getNumber)
                    .toList();
            Set<String> uniqueNumbers = new HashSet<>(numbers);
            if (uniqueNumbers.size() < numbers.size()) {
                return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY, TrainFieldName.NUMBER.getRealName());
            }

            List<Train> trains = trainDTOs.stream()
                    .map(TrainMapper::toEntity)
                    .collect(Collectors.toList());

            for (Train train : trains) {
                ServiceResult<Void> validationResult = validateUniqueFields(train, null);
                if (validationResult.getStatus() != ServiceStatus.SUCCESS) {
                    return new ServiceResult<>(validationResult.getStatus(), validationResult.getFieldName());
                }
            }

            List<Train> savedTrains = trainRepository.saveAll(trains);
            List<TrainDTO> savedTrainDTOs = savedTrains.stream()
                    .map(TrainMapper::toDTO)
                    .collect(Collectors.toList());

            return new ServiceResult<>(ServiceStatus.SUCCESS, savedTrainDTOs);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        } catch (ConstraintViolationException e) {
            String fieldName = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, fieldName);
        }
    }

    @Override
    public ServiceResult<TrainDTO> update(@NonNull Long id, @NonNull TrainDTO trainDTO) {
        if (!trainRepository.existsById(id)) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }
        try {
            Train train = TrainMapper.toEntity(trainDTO);
            train.setId(id);

            ServiceResult<Void> validationResult = validateUniqueFields(train, id);
            if (validationResult.getStatus() != ServiceStatus.SUCCESS) {
                return new ServiceResult<>(validationResult.getStatus(),
                        validationResult.getFieldName());
            }

            Train updatedTrain = trainRepository.save(train);
            return new ServiceResult<>(ServiceStatus.SUCCESS, TrainMapper.toDTO(updatedTrain));
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        } catch (ConstraintViolationException e) {
            String fieldName = e.getConstraintViolations().iterator().next().getPropertyPath().toString();
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, fieldName);
        }
    }

    @Override
    @Transactional
    public ServiceResult<Void> delete(@NonNull Long id) {
        Optional<Train> train = trainRepository.findById(id);
        if (train.isPresent()) {
            Train trainEntity = train.get();
            if (trainEntity.getTrainTickets().isEmpty()) {
                trainRepository.deleteById(id);
                return new ServiceResult<>(ServiceStatus.SUCCESS);
            } else {
                return new ServiceResult<>(ServiceStatus.DELETION_NOT_ALLOWED);
            }
        } else {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }
    }

    @Override
    public ServiceResult<Void> deleteAll() {
        trainRepository.deleteAll();
        return new ServiceResult<>(ServiceStatus.SUCCESS);
    }

}