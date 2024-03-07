package com.rinha.domain.transacoes.dtos;

public class SaldoDTO {

    private Long total;
    private String data_extrato;
    private Long limite;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public String getData_extrato() {
        return data_extrato;
    }

    public void setData_extrato(String data_extrato) {
        this.data_extrato = data_extrato;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }
}
