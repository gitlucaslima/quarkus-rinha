package com.rinha.domain.transacoes.dtos;

public class PostResponseDTO {
    private Long limite;
    private Long saldo;


    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }

    public Long getSaldo() {
        return saldo;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }
}
