package csit.semit.kde.javahibernatewebappskdelab2.dao;

import csit.semit.kde.javahibernatewebappskdelab2.entity.SoftDeletable;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationStatus;
import csit.semit.kde.javahibernatewebappskdelab2.util.result.dao.OperationResult;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolation;
import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import jakarta.validation.Validator;

import java.lang.reflect.Field;
import java.util.*;

public interface EntityDAO<E> {
    SessionFactory getSessionFactory();

    Validator getValidator();

    Class<E> getEntityClass();

    OperationResult<E> findByKeySet(E template, boolean includeDeleted);

    default void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }

    private OperationResult<E> validateEntity(E entity) {
        Validator validator = getValidator();
        Set<ConstraintViolation<E>> violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<E> violation : violations) {
                System.err.println(violation.getInvalidValue());
                System.err.println(violation.getPropertyPath());
                System.err.println(violation.getMessage());
            }
            return new OperationResult<>(OperationStatus.VALIDATION_ERROR, FieldName.getByRealName(violations.iterator().next().getPropertyPath().toString()));
        }
        return new OperationResult<>(OperationStatus.SUCCESS);
    }

    private OperationResult<E> checkDuplicateWithDeleted(E entity) {
        OperationResult<E> findResult = findByKeySet(entity, true);
        System.out.println(findResult);
        if (findResult.getStatus() == OperationStatus.SUCCESS) {
            return new OperationResult<>(OperationStatus.DUPLICATE_ENTRY, findResult.getEntity(), findResult.getFoundFields());
        } else if (findResult.getStatus() == OperationStatus.VALIDATION_ERROR) {
            return new OperationResult<>(OperationStatus.VALIDATION_ERROR);
        } else if (findResult.getStatus() == OperationStatus.DATABASE_ERROR) {
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        }
        return new OperationResult<>(OperationStatus.SUCCESS);
    }

    default OperationResult<E> getAllList(boolean includeDeleted) {

        try (Session session = getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());

            Root<E> root = criteriaQuery.from(getEntityClass());

            if (SoftDeletable.class.isAssignableFrom(getEntityClass())) {
                if (includeDeleted) {
                    criteriaQuery.select(root).where(criteriaBuilder.isTrue(root.get("isDeleted")));
                } else {
                    criteriaQuery.select(root).where(criteriaBuilder.isFalse(root.get("isDeleted")));
                }
            } else {
                criteriaQuery.select(root);
            }

            List<E> results = session.createQuery(criteriaQuery).getResultList();

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

    default OperationResult<E> findDeletedEntities() {
        try (Session session = getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
            Root<E> root = criteriaQuery.from(getEntityClass());

            if (SoftDeletable.class.isAssignableFrom(getEntityClass())) {
                criteriaQuery.select(root).where(criteriaBuilder.isTrue(root.get("isDeleted")));
            } else {
                return new OperationResult<>(OperationStatus.ENTITIES_NOT_FOUND);
            }

            List<E> results = session.createQuery(criteriaQuery).getResultList();

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

    default OperationResult<E> findById(@NonNull Long id) {
        try (Session session = getSessionFactory().openSession()) {
            E result = session.get(getEntityClass(), id);

            if (result == null) {
                return new OperationResult<>(OperationStatus.ENTITY_NOT_FOUND);
            } else if (result instanceof SoftDeletable && ((SoftDeletable) result).getIsDeleted()) {
                return new OperationResult<>(OperationStatus.ENTITY_DELETED);
            } else {
                return new OperationResult<>(OperationStatus.SUCCESS, result);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> findByKey(@NonNull Field field, @NonNull Object value, boolean includeDeleted) {
        try (Session session = getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
            Root<E> root = criteriaQuery.from(getEntityClass());

            if (!includeDeleted && SoftDeletable.class.isAssignableFrom(getEntityClass())) {
                criteriaQuery.select(root).where(
                        criteriaBuilder.and(
                                criteriaBuilder.equal(root.get(field.getName()), value),
                                criteriaBuilder.isFalse(root.get("isDeleted"))
                        )
                );
            } else {
                criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(field.getName()), value));
            }

            E result = session.createQuery(criteriaQuery).uniqueResult();

            if (result == null) {
                return new OperationResult<>(OperationStatus.ENTITY_NOT_FOUND);
            } else {
                return new OperationResult<>(OperationStatus.SUCCESS, result);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> findByFieldValue(@NonNull Field field, @NonNull Object value, boolean includeDeleted) {
        try (Session session = getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
            Root<E> root = criteriaQuery.from(getEntityClass());

            Predicate predicate = criteriaBuilder.equal(root.get(field.getName()), value);
            if (!includeDeleted && SoftDeletable.class.isAssignableFrom(getEntityClass())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.isFalse(root.get("isDeleted")));
            }

            criteriaQuery.select(root).where(predicate);

            List<E> results = session.createQuery(criteriaQuery).getResultList();

            if (results.isEmpty()) {
                return new OperationResult<>(OperationStatus.ENTITIES_NOT_FOUND);
            } else {
                return new OperationResult<>(OperationStatus.SUCCESS, results);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> findByKeys(@NonNull Map<Field, Object> fieldValues, boolean includeDeleted) {
        try (Session session = getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<E> criteriaQuery = criteriaBuilder.createQuery(getEntityClass());
            Root<E> root = criteriaQuery.from(getEntityClass());

            List<Predicate> predicates = new ArrayList<>();
            Set<FieldName> foundFields = new HashSet<>();

            if (!includeDeleted && SoftDeletable.class.isAssignableFrom(getEntityClass())) {
                predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));
            }

            for (Map.Entry<Field, Object> entry : fieldValues.entrySet()) {
                Predicate predicate = criteriaBuilder.equal(root.get(entry.getKey().getName()), entry.getValue());
                predicates.add(predicate);
                if (session.createQuery(criteriaQuery.select(root).where(predicate)).uniqueResult() != null) {
                    foundFields.add(FieldName.getByRealName(entry.getKey().getName()));
                }
            }

            criteriaQuery.select(root).where(predicates.toArray(new Predicate[0]));

            E result = session.createQuery(criteriaQuery).uniqueResult();

            if (result == null) {
                return new OperationResult<>(OperationStatus.ENTITY_NOT_FOUND);
            } else {
                return new OperationResult<>(OperationStatus.SUCCESS, result, foundFields);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> insert(@NonNull E entityToSave) {
        OperationResult<E> validationResult = validateEntity(entityToSave);
        if (validationResult.getStatus() != OperationStatus.SUCCESS) {
            return validationResult;
        }

        OperationResult<E> duplicateCheckResult = checkDuplicateWithDeleted(entityToSave);
        if (duplicateCheckResult.getStatus() != OperationStatus.SUCCESS) {
            return duplicateCheckResult;
        }

        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entityToSave);
            transaction.commit();
            return new OperationResult<>(OperationStatus.SUCCESS, entityToSave);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> insertEntities(@NonNull List<E> entitiesToSave) {
        for (E entity : entitiesToSave) {
            OperationResult<E> validationResult = validateEntity(entity);
            if (validationResult.getStatus() != OperationStatus.SUCCESS) {
                return new OperationResult<>(validationResult.getStatus(), entity, validationResult.getFoundFields());
            }

            OperationResult<E> duplicateCheckResult = checkDuplicateWithDeleted(entity);
            if (duplicateCheckResult.getStatus() != OperationStatus.SUCCESS) {
                return duplicateCheckResult;
            }
        }

        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<E> persistedEntities = new ArrayList<>();
            for (E e : entitiesToSave) {
                session.persist(e);
                persistedEntities.add(e);
            }
            transaction.commit();
            return new OperationResult<>(OperationStatus.SUCCESS, persistedEntities);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> update(@NonNull Long id, @NonNull E entityToUpdate) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            OperationResult<E> validationResult = validateEntity(entityToUpdate);
            if (validationResult.getStatus() != OperationStatus.SUCCESS) {
                return validationResult;
            }

            transaction = session.beginTransaction();

            E existingEntity = session.get(getEntityClass(), id);

            if (existingEntity == null) {
                return new OperationResult<>(OperationStatus.ENTITY_NOT_FOUND);
            } else if (existingEntity instanceof SoftDeletable && ((SoftDeletable) existingEntity).getIsDeleted()) {
                return new OperationResult<>(OperationStatus.ENTITY_DELETED);
            }

            OperationResult<E> duplicateCheckResult = checkDuplicateWithDeleted(entityToUpdate);
            if (duplicateCheckResult.getStatus() == OperationStatus.DUPLICATE_ENTRY) {
                E duplicateEntity = duplicateCheckResult.getEntity();
                if (duplicateEntity != null && !duplicateEntity.equals(existingEntity)) {
                    return duplicateCheckResult;
                }
            } else if (duplicateCheckResult.getStatus() != OperationStatus.SUCCESS) {
                return duplicateCheckResult;
            }

            E mergedEntity = session.merge(entityToUpdate);

            transaction.commit();

            return new OperationResult<>(OperationStatus.SUCCESS, mergedEntity);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> delete(@NonNull Long id) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            E existingEntity = session.get(getEntityClass(), id);
            if (existingEntity == null) {
                return new OperationResult<>(OperationStatus.ENTITY_NOT_FOUND);
            } else if (existingEntity instanceof SoftDeletable && ((SoftDeletable) existingEntity).getIsDeleted()) {
                return new OperationResult<>(OperationStatus.ENTITY_ALREADY_DELETED);
            }

            if (existingEntity instanceof SoftDeletable) {
                ((SoftDeletable) existingEntity).setIsDeleted(true);
            }

            transaction.commit();
            return new OperationResult<>(OperationStatus.SUCCESS, existingEntity);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> permanentDelete(@NonNull Long id) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            E existingEntity = session.get(getEntityClass(), id);
            if (existingEntity == null) {
                return new OperationResult<>(OperationStatus.ENTITY_NOT_FOUND);
            }

            session.remove(existingEntity);
            transaction.commit();
            return new OperationResult<>(OperationStatus.SUCCESS, existingEntity);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> restoreById(@NonNull Long id) {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            E existingEntity = session.get(getEntityClass(), id);
            if (existingEntity == null || !(existingEntity instanceof SoftDeletable)) {
                return new OperationResult<>(OperationStatus.ENTITY_NOT_FOUND);
            }

            SoftDeletable softDeletableEntity = (SoftDeletable) existingEntity;
            if (!softDeletableEntity.getIsDeleted()) {
                return new OperationResult<>(OperationStatus.ENTITY_ALREADY_ACTIVE);
            }

            softDeletableEntity.setIsDeleted(false);
            transaction.commit();

            return new OperationResult<>(OperationStatus.SUCCESS, existingEntity);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }

    default OperationResult<E> truncateTable() {
        Transaction transaction = null;
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String tableName = getEntityClass().getSimpleName().toLowerCase() + "s";
            session.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            transaction.commit();
            return new OperationResult<>(OperationStatus.SUCCESS);
        } catch (HibernateException e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.DATABASE_ERROR);
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
            return new OperationResult<>(OperationStatus.UNKNOWN_ERROR);
        }
    }
}