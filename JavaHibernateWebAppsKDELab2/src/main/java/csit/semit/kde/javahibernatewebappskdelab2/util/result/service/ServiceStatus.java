package csit.semit.kde.javahibernatewebappskdelab2.util.result.service;

public enum ServiceStatus {
    SUCCESS,                // Успішне виконання операції
    VALIDATION_ERROR,       // Помилка валідації (не пройшла валідація)
    DUPLICATE_ENTRY,        // Об'єкт з таким унікальним полем вже існує
    DATABASE_ERROR,         // Помилка на рівні бази даних
    ENTITY_NOT_FOUND,       // Об'єкт не знайдено
    ENTITIES_NOT_FOUND,
    ENTITY_DELETED,         // Об'єкт видалено
    ENTITY_ALREADY_DELETED,
    ENTITY_ALREADY_ACTIVE,  // Об'єкт вже активний
    FIELD_NOT_FOUND,        // Поле не знайдено
    UNKNOWN_ERROR;          // Невідома помилка
}
