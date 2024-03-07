package com.rinha.domain.clientes.dtos;

public class ClienteResponseDTO {

    private String nome;
    private Long limite;
    private Long saldo;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

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
