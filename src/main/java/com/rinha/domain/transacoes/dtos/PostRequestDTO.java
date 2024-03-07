package com.rinha.domain.transacoes.dtos;

import com.rinha.domain.transacoes.enums.TipoTransacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostRequestDTO {

    @NotNull(message = "Valor é obrigatório")
    @Min(value = 1, message = "Valor deve ser maior que 0")
    private Long valor;

    @NotNull(message = "Tipo é obrigatório")
    private String tipo;

    @NotEmpty(message = "Descrição é obrigatória")
    @Size(max = 10, message = "Descrição deve ter no máximo 10 caracteres")
    private String descricao;

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getValor() {
        return valor;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }
}
