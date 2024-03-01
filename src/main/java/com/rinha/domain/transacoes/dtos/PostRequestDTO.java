package com.rinha.domain.transacoes.dtos;

import com.rinha.domain.transacoes.enums.TipoTransacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class PostRequestDTO {

    @NotNull(message = "Valor é obrigatório")
    @Min(value = 1, message = "Valor deve ser maior que 0")
    private Long valor;

    @NotNull(message = "Tipo é obrigatório")
    private TipoTransacao tipo;

    @NotEmpty(message = "Descrição é obrigatória")
    private String descricao;

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

    @Override
    public String toString() {
        return "PostRequestDTO{" +
                "valor=" + valor +
                ", tipo=" + tipo +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
