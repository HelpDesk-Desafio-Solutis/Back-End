package br.com.shared.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ErrorResponse {

    private Integer status;
    private String erro;
    private String caminho;
    private LocalDate timestamp;

}
