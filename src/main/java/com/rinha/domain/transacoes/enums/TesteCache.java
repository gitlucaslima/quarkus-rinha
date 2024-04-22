package com.rinha.domain.transacoes.enums;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TesteCache implements Serializable {
    private String id;
    private String nome;
}
