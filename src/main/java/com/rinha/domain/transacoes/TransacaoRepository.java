package com.rinha.domain.transacoes;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransacaoRepository implements ReactivePanacheMongoRepository<Transacao> {
    

    public Uni<List<Transacao>> findLast10ByClienteIdAndOrderByDataDesc(Long clienteId) {
        String query = "clienteId = ?1";
        Sort sort = Sort.by("data").descending();
        return find(query, sort, clienteId).list();
    }

}
