package com.rinha.domain.clientes;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)

@MongoEntity(collection = "clientes")
public class Cliente extends ReactivePanacheMongoEntity implements Serializable {

    private String nome;
    private Long limite;
    private Long saldo;

}
