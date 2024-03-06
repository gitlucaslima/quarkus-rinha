package com.rinha.domain.transacoes.dtos;

import com.rinha.domain.transacoes.enums.TipoTransacao;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDTO{

    @NotNull(message = "Valor é obrigatório")
    @Min(value = 1, message = "Valor deve ser maior que 0")
    private Long valor;

    @NotNull(message = "Tipo é obrigatório")
    private TipoTransacao tipo;

    @NotEmpty(message = "Descrição é obrigatória")
    private String descricao;


    @Override
    public String toString() {
        return "PostRequestDTO{" +
                "valor=" + valor +
                ", tipo=" + tipo +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
