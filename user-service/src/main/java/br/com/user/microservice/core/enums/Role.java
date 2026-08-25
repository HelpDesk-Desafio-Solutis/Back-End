package br.com.user.microservice.core.enums;

import br.com.shared.exceptions.exceptionClass.EnumIsNotValidException;

import java.util.Arrays;

public enum Role {

    CLIENT,
    TECHNICIAN,
    ADMIN;

    public static Role checkValue(String value) {
        if (Arrays.stream(Role.values()).anyMatch(r -> r.name().equals(value))) {
            return Role.valueOf(value);
        }

        throw new EnumIsNotValidException(
                "Cargo não reconhecido"
        );

    }

}
