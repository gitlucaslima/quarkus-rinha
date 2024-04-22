package com.rinha.domain.transacoes;

import com.rinha.domain.transacoes.dtos.GetTransacaoDTO;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransacaoCache implements Serializable {

    public Long id;
    public GetTransacaoDTO transacaoDTO;

}
