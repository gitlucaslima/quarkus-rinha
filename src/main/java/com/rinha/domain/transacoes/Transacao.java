package com.rinha.domain.transacoes;

import com.rinha.domain.transacoes.enums.TipoTransacao;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@MongoEntity(collection = "transacoes")
@Cacheable
public class Transacao extends ReactivePanacheMongoEntity implements Serializable {

    private Long clienteId;
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;
    private Long valor;
    private String descricao;
    private Date data;

    public Transacao() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transacao transacao)) return false;
        return Objects.equals(getClienteId(), transacao.getClienteId()) && getTipo() == transacao.getTipo() && Objects.equals(getValor(), transacao.getValor()) && Objects.equals(getDescricao(), transacao.getDescricao()) && Objects.equals(getData(), transacao.getData());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClienteId(), getTipo(), getValor(), getDescricao(), getData());
    }

    public Transacao(Long clienteId, TipoTransacao tipo, Long valor, String descricao, Date data) {
        this.clienteId = clienteId;
        this.tipo = tipo;
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
