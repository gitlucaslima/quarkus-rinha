package com.rinha.domain.transacoes;

import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransacaoRepository implements ReactivePanacheMongoRepository<Transacao> {


    @CacheResult(cacheName = "transacoes")
    public Uni<List<Transacao>> findAllByClienteId(@CacheKey Long clienteId) {
        return find("clienteId", clienteId).list();
    }

}
