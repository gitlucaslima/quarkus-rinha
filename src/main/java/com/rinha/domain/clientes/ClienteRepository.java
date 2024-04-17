package com.rinha.domain.clientes;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;

@ApplicationScoped
public class ClienteRepository implements ReactivePanacheMongoRepository<Cliente> {


    public Uni<Cliente> findById(Long id) {
        return find("idCliente", id).firstResult().onItem().ifNull().failWith(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

}
