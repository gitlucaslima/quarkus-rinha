package com.rinha.domain.transacoes;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class TransacaoRepository implements ReactivePanacheMongoRepository<Transacao> {


    public Uni<Transacao> findById(Long id) {
        return findById(id);
    }

    public Uni<List<Transacao>> findAllTransacoes() {
        return listAll();
    }

    public Uni<List<Transacao>> findAllByClienteId(Long clienteId) {
        return find("clienteId", clienteId).list();
    }


}
