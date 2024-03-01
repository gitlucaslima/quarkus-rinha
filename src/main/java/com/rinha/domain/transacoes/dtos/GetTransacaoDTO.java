package com.rinha.domain.transacoes.dtos;

import java.util.List;

public class GetTransacaoDTO {

    private SaldoDTO saldo;
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
}
