package com.rinha.domain.transacoes.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rinha.domain.transacoes.enums.TipoTransacao;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class TransacaoDTO {

    private Long valor;
    private TipoTransacao tipo;
    private String descricao;
    private Timestamp realizada_em;

    @Override
    public String toString() {
        return "TransacaoDTO{" +
                "valor=" + valor +
                ", tipo=" + tipo +
                ", descricao='" + descricao + '\'' +
                ", realizada_em=" + realizada_em +
                '}';
    }
}
