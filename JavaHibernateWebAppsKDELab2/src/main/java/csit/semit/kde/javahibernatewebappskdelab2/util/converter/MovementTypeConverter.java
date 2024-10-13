package csit.semit.kde.javahibernatewebappskdelab2.util.converter;

import csit.semit.kde.javahibernatewebappskdelab2.enums.MovementType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class MovementTypeConverter implements AttributeConverter<MovementType, String> {

    @Override
    public String convertToDatabaseColumn(MovementType movementType) {
        return (movementType == null) ? null : movementType.getDisplayName();
    }

    @Override
    public MovementType convertToEntityAttribute(String dbData) {
        return (dbData == null || dbData.isEmpty()) ? null : MovementType.getByDisplayName(dbData);
    }
}
