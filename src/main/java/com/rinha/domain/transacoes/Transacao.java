package com.rinha.domain.transacoes;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rinha.domain.transacoes.enums.TipoTransacao;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)

@MongoEntity(collection = "transacoes")
@Cacheable
public class Transacao extends ReactivePanacheMongoEntity implements Serializable {

    private Long clienteId;
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;
    private Long valor;
    private String descricao;
    private String data;

    @Override
    public String toString() {
        return "Transacao{" +
                "clienteId=" + clienteId +
                ", tipo=" + tipo +
                ", valor=" + valor +
                ", descricao='" + descricao + '\'' +
                ", data=" + data +
                '}';
    }
}
