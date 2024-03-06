package com.rinha.domain.transacoes.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDTO{
    private Long limite;
    private Long saldo;

    @Override
    public String toString() {
        return "PostResponseDTO{" +
                "limite=" + limite +
                ", saldo=" + saldo +
                '}';
    }
}
