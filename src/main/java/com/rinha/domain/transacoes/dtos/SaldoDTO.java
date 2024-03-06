package com.rinha.domain.transacoes.dtos;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class SaldoDTO {

    private Long total;
    private Timestamp data_extrato;
    private Long limite;

    @Override
    public String toString() {
        return "SaldoDTO{" +
                "total=" + total +
                ", data_extrato=" + data_extrato +
                ", limite=" + limite +
                '}';
    }
}
