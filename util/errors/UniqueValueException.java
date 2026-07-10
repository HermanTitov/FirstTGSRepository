package LaGavioTa.project.util.errors;

/// Кастомная ошибка уникального значения

public class UniqueValueException extends RuntimeException {
    public UniqueValueException(String message) {
        super(message);
    }
}
