package com.rinha.domain.clientes;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import jakarta.persistence.Cacheable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)

@MongoEntity(collection = "clientes")
@Cacheable
public class Cliente extends ReactivePanacheMongoEntity implements Serializable {

    private Long idCliente;
    private String nome;
    private Long limite;
    private Long saldo;

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nome='" + nome + '\'' +
                ", limite=" + limite +
                ", saldo=" + saldo +
                '}';
    }
}
