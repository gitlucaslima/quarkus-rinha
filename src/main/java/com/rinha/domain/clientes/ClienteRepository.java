package com.rinha.domain.clientes;

import com.rinha.domain.clientes.dtos.ClienteResponseDTO;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

@ApplicationScoped
public class ClienteRepository implements ReactivePanacheMongoRepository<Cliente> {


    public Uni<Cliente> findById(Long id) {
        return find("idCliente", id).firstResult().onItem().ifNull().failWith(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    public Uni<List<Cliente>> findAllClientes() {
        return listAll();
    }

    public Uni<Cliente> saveCliente(ClienteResponseDTO body) {
        Cliente cliente = new Cliente();
        cliente.setNome(body.getNome());
        cliente.setLimite(body.getLimite());
        cliente.setSaldo(body.getSaldo());
        return cliente.persist();
    }

}
