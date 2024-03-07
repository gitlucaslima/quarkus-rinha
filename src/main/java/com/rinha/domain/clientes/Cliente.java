package com.rinha.domain.clientes;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import jakarta.persistence.Cacheable;

import java.io.Serializable;
import java.util.Objects;


@MongoEntity(collection = "clientes")
@Cacheable
public class Cliente extends ReactivePanacheMongoEntity implements Serializable {

    private Long idCliente;
    private String nome;
    private Long limite;
    private Long saldo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente cliente)) return false;
        return Objects.equals(getIdCliente(), cliente.getIdCliente()) && Objects.equals(getNome(), cliente.getNome()) && Objects.equals(getLimite(), cliente.getLimite()) && Objects.equals(getSaldo(), cliente.getSaldo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdCliente(), getNome(), getLimite(), getSaldo());
    }

    public Cliente() {
    }

    public Cliente(Long idCliente, String nome, Long limite, Long saldo) {
        this.idCliente = idCliente;
        this.nome = nome;
        this.limite = limite;
        this.saldo = saldo;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

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
