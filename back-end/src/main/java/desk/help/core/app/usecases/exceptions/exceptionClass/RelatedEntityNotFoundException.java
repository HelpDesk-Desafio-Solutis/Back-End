package desk.help.core.app.usecases.exceptions.exceptionClass;

public class RelatedEntityNotFoundException extends RuntimeException {
    public RelatedEntityNotFoundException(String message) {
        super(message);
    }
}
