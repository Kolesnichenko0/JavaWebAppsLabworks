package csit.semit.kde.javahibernatewebappskdelab2.service;

import csit.semit.kde.javahibernatewebappskdelab2.dao.TrainDAO;
import csit.semit.kde.javahibernatewebappskdelab2.dto.TrainDTO;
import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import csit.semit.kde.javahibernatewebappskdelab2.entity.Transport;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationStatus;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.service.ServiceStatus;
import csit.semit.kde.javahibernatewebappskdelab2.util.criteria.TrainQueryParams;
import csit.semit.kde.javahibernatewebappskdelab2.util.mapper.TrainMapper;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldValidationException;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationResult;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.service.ServiceResult;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public interface ITrainService {
    TrainDAO getTrainDAO();

    private ServiceStatus convertToServiceStatus(OperationStatus operationStatus) {
        return switch (operationStatus) {
            case VALIDATION_ERROR -> ServiceStatus.VALIDATION_ERROR;
            case DUPLICATE_ENTRY -> ServiceStatus.DUPLICATE_ENTRY;
            case DATABASE_ERROR -> ServiceStatus.DATABASE_ERROR;
            case ENTITY_NOT_FOUND -> ServiceStatus.ENTITY_NOT_FOUND;
            case ENTITIES_NOT_FOUND -> ServiceStatus.ENTITIES_NOT_FOUND;
            case UNKNOWN_ERROR -> ServiceStatus.UNKNOWN_ERROR;
            case FIELD_NOT_FOUND -> ServiceStatus.UNKNOWN_ERROR;
            case ENTITY_DELETED -> ServiceStatus.ENTITY_DELETED;
            case ENTITY_ALREADY_DELETED -> ServiceStatus.ENTITY_ALREADY_DELETED;
            case ENTITY_ALREADY_ACTIVE -> ServiceStatus.ENTITY_ALREADY_ACTIVE;
            default -> ServiceStatus.UNKNOWN_ERROR;
        };
    }

    private ServiceResult<TrainDTO> convertToServiceResultWithList(OperationResult<Train> operationResult) {
        if (operationResult.getStatus() == OperationStatus.SUCCESS) {
            List<TrainDTO> trainDTOList = operationResult.getEntityList().stream()
                    .map(TrainMapper::toDTO)
                    .collect(Collectors.toList());
            return new ServiceResult<>(ServiceStatus.SUCCESS, trainDTOList);
        } else {
            return new ServiceResult<>(convertToServiceStatus(operationResult.getStatus()));
        }
    }

    private ServiceResult<TrainDTO> convertToServiceResult(OperationResult<Train> operationResult) {
        if (operationResult.getStatus() == OperationStatus.SUCCESS) {
            return new ServiceResult<>(ServiceStatus.SUCCESS, TrainMapper.toDTO(operationResult.getEntity()));
        } else {
            return new ServiceResult<>(convertToServiceStatus(operationResult.getStatus()));
        }
    }

    private ServiceResult<TrainDTO> convertToServiceResultForCreateUpdate(OperationResult<Train> operationResult) {
        if (operationResult.getStatus() == OperationStatus.DUPLICATE_ENTRY) {
            return new ServiceResult<>(ServiceStatus.DUPLICATE_ENTRY,
                    TrainMapper.toDTO(operationResult.getEntity()), operationResult.getFoundFields());
        } else if (operationResult.getStatus() == OperationStatus.SUCCESS) {
            return new ServiceResult<>(ServiceStatus.SUCCESS, TrainMapper.toDTO(operationResult.getEntity()));
        } else {
            return new ServiceResult<>(convertToServiceStatus(operationResult.getStatus()));
        }
    }

    default ServiceResult<TrainDTO> getAllList() {
        return convertToServiceResultWithList(getTrainDAO().getAllList(false));
    }

    default ServiceResult<TrainDTO> create(@NonNull TrainDTO trainDTO) {
        Train train;
        try {
            train = TrainMapper.toEntity(trainDTO);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }

        OperationResult<Train> operationResult = getTrainDAO().insert(train);

        return convertToServiceResultForCreateUpdate(operationResult);
    }

    default ServiceResult<TrainDTO> update(@NonNull Long id, @NonNull TrainDTO trainDTO) {
        Train train;
        try {
            Transport.validateId(id);
            train = TrainMapper.toEntity(trainDTO);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }

        OperationResult<Train> operationResult = getTrainDAO().update(id, train);

        return convertToServiceResultForCreateUpdate(operationResult);
    }

    default ServiceResult<TrainDTO> delete(@NonNull Long id) {
        try {
            Transport.validateId(id);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }

        OperationResult<Train> operationResult = getTrainDAO().delete(id);
        return convertToServiceResult(operationResult);
    }

    default ServiceResult<TrainDTO> findDeletedEntities() {
        OperationResult<Train> operationResult = getTrainDAO().findDeletedEntities();
        return convertToServiceResultWithList(operationResult);
    }

    default ServiceResult<TrainDTO> permanentDelete(@NonNull Long id) {
        try {
            Transport.validateId(id);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }

        OperationResult<Train> operationResult = getTrainDAO().permanentDelete(id);
        return convertToServiceResult(operationResult);
    }

    default ServiceResult<TrainDTO> restore(@NonNull Long id) {
        try {
            Transport.validateId(id);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }
        OperationResult<Train> operationResult = getTrainDAO().restoreById(id);
        return convertToServiceResult(operationResult);
    }

    default ServiceResult<TrainDTO> restore(@NonNull String number) {
        try {
            number = Train.validateNumber(number);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        OperationResult<Train> operationResult = getTrainDAO().restoreByNumber(number);
        return convertToServiceResult(operationResult);
    }

    default ServiceResult<TrainDTO> findById(@NonNull Long id) {
        try {
            Transport.validateId(id);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.ENTITY_NOT_FOUND);
        }
        OperationResult<Train> operationResult = getTrainDAO().findById(id);
        return convertToServiceResult(operationResult);
    }

    default ServiceResult<TrainDTO> findByNumber(@NonNull String number) {
        try {
            number = Train.validateNumber(number);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        OperationResult<Train> operationResult = getTrainDAO().findByNumber(number, false);
        return convertToServiceResult(operationResult);
    }

    default ServiceResult<TrainDTO> findByNumberInDeleted(@NonNull String number) {
        try {
            number = Train.validateNumber(number);
        } catch (FieldValidationException e) {
            return new ServiceResult<>(ServiceStatus.VALIDATION_ERROR, e.getFieldName());
        }
        OperationResult<Train> operationResult = getTrainDAO().findByNumberInDeleted(number);
        return convertToServiceResult(operationResult);
    }

    default ServiceResult<TrainDTO> findAndFilterAndSortByQueryParams(TrainQueryParams queryParams) {
        TrainDAO trainDAO = getTrainDAO();
        OperationResult<Train> operationResult = trainDAO.findAndFilterAndSortByQueryParams(queryParams);

        return convertToServiceResultWithList(operationResult);
    }
}
