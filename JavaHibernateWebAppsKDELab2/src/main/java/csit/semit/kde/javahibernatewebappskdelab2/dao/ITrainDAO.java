package csit.semit.kde.javahibernatewebappskdelab2.dao;

import csit.semit.kde.javahibernatewebappskdelab2.entity.SoftDeletable;
import csit.semit.kde.javahibernatewebappskdelab2.entity.Train;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationStatus;
import csit.semit.kde.javahibernatewebappskdelab2.util.criteria.TrainQueryParams;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationResult;
import jakarta.persistence.criteria.*;
import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.lang.reflect.Field;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public interface ITrainDAO extends EntityDAO<Train> {
    default OperationResult<Train> findByKeySet(@NonNull Train template, boolean includeDeleted) {
        try {
            Map<Field, Object> fieldValues = new HashMap<>();
            Field numberField = getEntityClass().getDeclaredField(FieldName.NUMBER.getRealName());
            fieldValues.put(numberField, template.getNumber());

            return findByKeys(fieldValues, includeDeleted);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.FIELD_NOT_FOUND);
        }
    }

    default OperationResult<Train> findByNumber(@NonNull String number, boolean includeDeleted) {
        try {
            Field numberField = getEntityClass().getDeclaredField(FieldName.NUMBER.getRealName());
            return findByKey(numberField, number, includeDeleted);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.FIELD_NOT_FOUND);
        }
    }

    default OperationResult<Train> findByNumberInDeleted(@NonNull String number) {
        try {
            Field numberField = getEntityClass().getDeclaredField(FieldName.NUMBER.getRealName());
            return findByKeyInDeleted(numberField, number);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.FIELD_NOT_FOUND);
        }
    }

    default OperationResult<Train> findAndFilterAndSortByQueryParams(TrainQueryParams queryParams) {

        try (Session session = getSessionFactory().openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Train> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
            Root<Train> root = criteriaQuery.from(getEntityClass());

            List<Predicate> predicates = new ArrayList<>();

            if (queryParams.getSearchingNumber() != null && !queryParams.getSearchingNumber().isEmpty()) {
                String number = queryParams.getSearchingNumber().trim();
                predicates.add(criteriaBuilder.like(root.get(FieldName.NUMBER.getRealName()), "%" + number + "%"));
            } else {
                if (queryParams.getSearchingDepartureStation() != null && !queryParams.getSearchingDepartureStation().isEmpty()) {
                    String departureStation = queryParams.getSearchingDepartureStation().trim();
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(FieldName.DEPARTURE_STATION.getRealName())), "%" + departureStation.toLowerCase() + "%"));
                }
                if (queryParams.getSearchingArrivalStation() != null && !queryParams.getSearchingArrivalStation().isEmpty()) {
                    String arrivalStation = queryParams.getSearchingArrivalStation().trim();
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(FieldName.ARRIVAL_STATION.getRealName())), "%" + arrivalStation.toLowerCase() + "%"));
                }
            }

            if (queryParams.getFilteringMovementTypes() != null && !queryParams.getFilteringMovementTypes().isEmpty()) {
                predicates.add(root.get(FieldName.MOVEMENT_TYPE.getRealName()).in(queryParams.getFilteringMovementTypes()));
            }
            if (queryParams.getFilteringFrom() != null && queryParams.getFilteringTo() != null) {
                predicates.add(criteriaBuilder.between(root.get(FieldName.DEPARTURE_TIME.getRealName()), queryParams.getFilteringFrom(), queryParams.getFilteringTo()));
            } else {
                if (queryParams.getFilteringFrom() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(FieldName.DEPARTURE_TIME.getRealName()), queryParams.getFilteringFrom()));
                }
                if (queryParams.getFilteringTo() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(FieldName.DEPARTURE_TIME.getRealName()), queryParams.getFilteringTo()));
                }
            }
            if (queryParams.getFilteringMinDuration() != null && queryParams.getFilteringMaxDuration() != null) {
                predicates.add(criteriaBuilder.between(root.get(FieldName.DURATION.getRealName()), queryParams.getFilteringMinDuration(), queryParams.getFilteringMaxDuration()));
            } else {
                if (queryParams.getFilteringMinDuration() != null) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(FieldName.DURATION.getRealName()), queryParams.getFilteringMinDuration()));
                }
                if (queryParams.getFilteringMaxDuration() != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(FieldName.DURATION.getRealName()), queryParams.getFilteringMaxDuration()));
                }
            }
            if (SoftDeletable.class.isAssignableFrom(getEntityClass())) {
                predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));
            }

            criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

            if (queryParams.getSortedByTrainNumberAsc() != null) {
                criteriaQuery.orderBy(queryParams.getSortedByTrainNumberAsc() ? criteriaBuilder.asc(root.get(FieldName.NUMBER.getRealName())) : criteriaBuilder.desc(root.get(FieldName.NUMBER.getRealName())));
            } else if (queryParams.getSortedByDurationAsc() != null) {
                criteriaQuery.orderBy(queryParams.getSortedByDurationAsc() ? criteriaBuilder.asc(root.get(FieldName.DURATION.getRealName())) : criteriaBuilder.desc(root.get(FieldName.DURATION.getRealName())));
            } else if (queryParams.getSortedByDepartureTimeAsc() != null) {
                criteriaQuery.orderBy(queryParams.getSortedByDepartureTimeAsc() ? criteriaBuilder.asc(root.get(FieldName.DEPARTURE_TIME.getRealName())) : criteriaBuilder.desc(root.get(FieldName.DEPARTURE_TIME.getRealName())));
            }

            List<Train> results = session.createQuery(criteriaQuery).getResultList();
            if (results.isEmpty()) {
                return new OperationResult<>(OperationStatus.ENTITIES_NOT_FOUND);
            }
            return new OperationResult<>(OperationStatus.SUCCESS, results);
        } catch (HibernateException e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<Train> insertSampleTrains() {
        List<Train> trains = Arrays.asList(
                new Train("102ІС", "Одеса-Головна", "Дніпро-Головний", MovementType.EVEN_DAYS, LocalTime.of(14, 30), Duration.ofSeconds(37800)),
                new Train("203ІС", "Львів", "Івано-Франківськ", MovementType.ODD_DAYS, LocalTime.of(10, 15), Duration.ofSeconds(10500)),
                new Train("101ІС+", "Київ-Пасажирський", "Львів", MovementType.DAILY, LocalTime.of(6, 45), Duration.ofSeconds(21420)),
                new Train("345НШ", "Дніпро-Головний", "Миколаїв", MovementType.EVEN_DAYS, LocalTime.of(6, 5), Duration.ofSeconds(25800)),
                new Train("304Р", "Харків-Пасажирський", "Кривий Ріг-Головний", MovementType.DAILY, LocalTime.of(22, 10), Duration.ofSeconds(29400)),
                new Train("112НЕ", "Полтава-Київська", "Чернівці", MovementType.EVEN_DAYS, LocalTime.of(9, 15), Duration.ofSeconds(46800)),
                new Train("405Р", "Запоріжжя-1", "Одеса-Головна", MovementType.EVEN_DAYS, LocalTime.of(3, 20), Duration.ofSeconds(40800)),
                new Train("506Р", "Київ-Пасажирський", "Вінниця", MovementType.ODD_DAYS, LocalTime.of(18, 55), Duration.ofSeconds(14400)),
                new Train("607РЕ", "Миколаїв", "Дніпро-Головний", MovementType.DAILY, LocalTime.of(8, 40), Duration.ofSeconds(27000)),
                new Train("709РЕ", "Чернівці", "Одеса-Головна", MovementType.EVEN_DAYS, LocalTime.of(7, 0), Duration.ofSeconds(55800)),
                new Train("710НЕ", "Хмельницький", "Київ-Пасажирський", MovementType.ODD_DAYS, LocalTime.of(19, 45), Duration.ofSeconds(25200)),
                new Train("102НЕ", "Івано-Франківськ", "Житомир", MovementType.DAILY, LocalTime.of(5, 55), Duration.ofSeconds(33000)),
                new Train("123НЕ", "Одеса-Головна", "Тернопіль", MovementType.ODD_DAYS, LocalTime.of(13, 35), Duration.ofSeconds(32400)),
                new Train("234НШ", "Кривий Ріг-Головний", "Запоріжжя-1", MovementType.DAILY, LocalTime.of(16, 50), Duration.ofSeconds(18600)),
                new Train("456НШ", "Львів", "Київ-Пасажирський", MovementType.DAILY, LocalTime.of(21, 30), Duration.ofSeconds(21600)),
                new Train("708РЕ", "Тернопіль", "Львів", MovementType.DAILY, LocalTime.of(11, 25), Duration.ofSeconds(7200)),
                new Train("567НП", "Житомир", "Львів", MovementType.ODD_DAYS, LocalTime.of(11, 55), Duration.ofSeconds(50400)),
                new Train("556ІС+", "Одеса-Головна", "Запоріжжя-1", MovementType.DAILY, LocalTime.of(7, 35), Duration.ofSeconds(39600)),
                new Train("678НП", "Тернопіль", "Хмельницький", MovementType.EVEN_DAYS, LocalTime.of(15, 25), Duration.ofSeconds(14400)),
                new Train("689НП", "Київ-Пасажирський", "Одеса-Головна", MovementType.DAILY, LocalTime.of(23, 10), Duration.ofSeconds(36000)),
                new Train("502НП", "Львів", "Одеса-Головна", MovementType.ODD_DAYS, LocalTime.of(18, 20), Duration.ofSeconds(45000)),
                new Train("112ІС", "Харків-Пасажирський", "Полтава-Київська", MovementType.EVEN_DAYS, LocalTime.of(12, 0), Duration.ofSeconds(10800)),
                new Train("223ІС", "Миколаїв", "Київ-Пасажирський", MovementType.DAILY, LocalTime.of(3, 45), Duration.ofSeconds(37800)),
                new Train("334ІС", "Тернопіль", "Дніпро-Головний", MovementType.EVEN_DAYS, LocalTime.of(14, 55), Duration.ofSeconds(46800)),
                new Train("590НП", "Івано-Франківськ", "Запоріжжя-1", MovementType.DAILY, LocalTime.of(9, 5), Duration.ofSeconds(72000)),
                new Train("445ІС+", "Чернівці", "Львів", MovementType.ODD_DAYS, LocalTime.of(22, 50), Duration.ofSeconds(12600)),
                new Train("667ІС+", "Кривий Ріг-Головний", "Івано-Франківськ", MovementType.EVEN_DAYS, LocalTime.of(17, 40), Duration.ofSeconds(64800)),
                new Train("578ІС", "Дніпро-Головний", "Миколаїв", MovementType.DAILY, LocalTime.of(10, 30), Duration.ofSeconds(28800)),
                new Train("589Р", "Хмельницький", "Київ-Пасажирський", MovementType.ODD_DAYS, LocalTime.of(19, 15), Duration.ofSeconds(25200)),
                new Train("590РЕ", "Запоріжжя-1", "Львів", MovementType.EVEN_DAYS, LocalTime.of(4, 55), Duration.ofSeconds(75600))
        );

        return insertEntities(trains);
    }

    default OperationResult<Train> restoreByNumber(@NonNull String number) {
        OperationResult<Train> findResult = findByNumber(number, true);
        if (findResult.getStatus() != OperationStatus.SUCCESS) {
            return findResult;
        }

        Train train = findResult.getEntity();
        if (train == null) {
            return new OperationResult<>(OperationStatus.ENTITY_NOT_FOUND);
        }

        return restoreById(train.getId());
    }
}
