package com.rinha.domain.transacoes.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;


public class GetTransacaoDTO implements Serializable {

    @JsonProperty("saldo")
    private SaldoDTO saldo;

    @JsonProperty("ultimas_transacoes")
    private List<TransacaoDTO> ultimas_transacoes;

    public SaldoDTO getSaldo() {
        return saldo;
    }

    public void setSaldo(SaldoDTO saldo) {
        this.saldo = saldo;
    }

    public List<TransacaoDTO> getUltimas_transacoes() {
        return ultimas_transacoes;
    }

    public void setUltimas_transacoes(List<TransacaoDTO> ultimas_transacoes) {
        this.ultimas_transacoes = ultimas_transacoes;
    }

    @Override
    public String toString() {
        return "GetTransacaoDTO{" +
                "saldo=" + saldo +
                ", ultimas_transacoes=" + ultimas_transacoes +
                '}';
    }
}
