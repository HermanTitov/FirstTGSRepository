package LaGavioTa.project.util.errors;

/// Кастомная ошибка, если объекта нет в БД

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
