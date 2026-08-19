package desk.help.core.enums;

import desk.help.core.app.usecases.exceptions.exceptionClass.EnumIsNotValidException;

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
