package com.rinha.domain.clientes.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteResponseDTO {

    private String nome;
    private Long limite;
    private Long saldo;

}
