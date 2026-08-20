package desk.help.core.app.usecases.exceptions.exceptionClass;

public class InactiveEntityException extends RuntimeException {
    public InactiveEntityException(String message) {
        super(message);
    }
}
