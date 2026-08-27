package br.com.user.microservice.core.app.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDto {

    private String email;
    private String password;

}
