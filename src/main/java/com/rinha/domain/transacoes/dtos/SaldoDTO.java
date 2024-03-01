package com.rinha.domain.transacoes.dtos;

import java.sql.Timestamp;

public class SaldoDTO {

    private Long total;
    private Timestamp data_extrato;
    private Long limite;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Timestamp getData_extrato() {
        return data_extrato;
    }

    public void setData_extrato(Timestamp data_extrato) {
        this.data_extrato = data_extrato;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }
}
