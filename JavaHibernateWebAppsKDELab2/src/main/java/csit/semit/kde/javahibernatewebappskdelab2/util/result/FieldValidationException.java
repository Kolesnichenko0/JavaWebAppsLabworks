package csit.semit.kde.javahibernatewebappskdelab2.util.result;

import lombok.Getter;

@Getter
public class FieldValidationException extends IllegalArgumentException {
    private final FieldName fieldName;

    public FieldValidationException(FieldName fieldName) {
        this.fieldName = fieldName;
    }

    public FieldValidationException(String message, FieldName fieldName) {
        super(message);
        this.fieldName = fieldName;
    }

}
