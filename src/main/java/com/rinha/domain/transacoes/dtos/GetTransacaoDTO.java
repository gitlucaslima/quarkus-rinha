package com.rinha.domain.transacoes.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetTransacaoDTO {

    private SaldoDTO saldo;
    private List<TransacaoDTO> ultimas_transacoes;

    @Override
    public String toString() {
        return "GetTransacaoDTO{" +
                "saldo=" + saldo +
                ", ultimas_transacoes=" + ultimas_transacoes +
                '}';
    }

}
