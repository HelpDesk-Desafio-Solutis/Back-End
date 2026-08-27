package br.com.shared.exceptions.exceptionClass;

public class InactiveEntityException extends RuntimeException {
    public InactiveEntityException(String message) {
        super(message);
    }
}
