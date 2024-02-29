package com.rinha.domain.transacoes;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)

@MongoEntity(collection = "transacoes")
public class Transacao extends ReactivePanacheMongoEntity implements Serializable {

    private Long clienteId;
    private String tipo;
    private Long valor;
    private String descricao;
    private Timestamp data;
}
