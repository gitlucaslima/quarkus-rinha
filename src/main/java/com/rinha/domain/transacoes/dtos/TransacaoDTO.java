package com.rinha.domain.transacoes.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rinha.domain.transacoes.enums.TipoTransacao;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

public class TransacaoDTO implements Serializable {

    @JsonProperty("valor")
    private Long valor;

    @JsonProperty("tipo")
    private TipoTransacao tipo;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("realizada_em")
    private Date realizada_em;

    @Override
    public String toString() {
        return "TransacaoDTO{" +
                "valor=" + valor +
                ", tipo=" + tipo +
                ", descricao='" + descricao + '\'' +
                ", realizada_em=" + realizada_em +
                '}';
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getRealizada_em() {
        return realizada_em;
    }

    public void setRealizada_em(Date realizada_em) {
        this.realizada_em = realizada_em;
    }
}
