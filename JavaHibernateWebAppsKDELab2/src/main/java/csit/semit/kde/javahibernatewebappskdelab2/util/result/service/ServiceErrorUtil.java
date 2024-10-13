package csit.semit.kde.javahibernatewebappskdelab2.util.result.service;

import csit.semit.kde.javahibernatewebappskdelab2.util.result.FieldName;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Set;

public class ServiceErrorUtil {
    public static String getMessage(ServiceResult<?> serviceResult) {
        ServiceStatus status = serviceResult.getStatus();
        switch (status) {
            case VALIDATION_ERROR:
                FieldName invalidField = serviceResult.getFieldName();
                return "Invalid field - " + invalidField.getRealName();
            case DUPLICATE_ENTRY:
                Set<FieldName> foundFields = serviceResult.getFoundFields();
                FieldName duplicateField = foundFields.iterator().next();
                return "Duplicate entry found for field - " + duplicateField.getRealName();
            case ENTITY_NOT_FOUND:
                return "The requested entity was not found.";
            case ENTITIES_NOT_FOUND:
                return "No entities were found.";
            case ENTITY_DELETED:
                return "The entity was deleted";
            case ENTITY_ALREADY_DELETED:
                return "The entity has already been deleted.";
            case ENTITY_ALREADY_ACTIVE:
                return "The entity is already active.";
            case DATABASE_ERROR:
                return "Database error occurred. Please try again later.";
            case FIELD_NOT_FOUND, UNKNOWN_ERROR:
                return "An unknown error occurred. Please try again later.";
            default:
                return "An unexpected error occurred. Please try again later.";
        }
    }

    public static Integer getHttpErrorStatusCode(ServiceStatus status) {
        return switch (status) {
            case VALIDATION_ERROR -> 422;
            case DUPLICATE_ENTRY, ENTITY_DELETED, ENTITY_ALREADY_ACTIVE, ENTITY_ALREADY_DELETED ->
                    HttpServletResponse.SC_CONFLICT; // 409
            case DATABASE_ERROR, FIELD_NOT_FOUND, UNKNOWN_ERROR -> HttpServletResponse.SC_INTERNAL_SERVER_ERROR; // 500
            default -> null;
        };
    }
}