package desk.help.core.app.usecases.exceptions.exceptionClass;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
