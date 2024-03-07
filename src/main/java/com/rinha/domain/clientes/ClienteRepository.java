package com.rinha.domain.clientes;

import com.rinha.domain.clientes.dtos.ClienteResponseDTO;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@ApplicationScoped
public class ClienteRepository implements ReactivePanacheMongoRepository<Cliente> {


    @CacheResult(cacheName = "cliente")
    public Uni<Cliente> findById(@CacheKey Long id) {
        return find("idCliente", id).firstResult().onItem().ifNull().failWith(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

}
