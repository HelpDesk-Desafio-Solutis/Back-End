package br.com.shared.exceptions.exceptionClass;

public class EnumIsNotValidException extends RuntimeException {
    public EnumIsNotValidException(String message) {
        super(message);
    }
}
