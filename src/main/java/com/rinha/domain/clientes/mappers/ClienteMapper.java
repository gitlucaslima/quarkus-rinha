package com.rinha.domain.clientes.mappers;

import com.rinha.domain.clientes.Cliente;
import com.rinha.domain.clientes.dtos.ClienteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClienteMapper {

    public List<ClienteResponseDTO> toDTO(List<Cliente> clientes) {

       return clientes.stream().map(cliente -> {
            ClienteResponseDTO clienteResponseDTO = new ClienteResponseDTO();
            clienteResponseDTO.setNome(cliente.getNome());
            clienteResponseDTO.setLimite(cliente.getLimite());
            clienteResponseDTO.setSaldo(cliente.getSaldo());
            return clienteResponseDTO;
        }).collect(Collectors.toList());
    }

    public Cliente toEntity(com.rinha.domain.clientes.dtos.ClienteResponseDTO clienteResponseDTO) {
        Cliente cliente = new Cliente();
        cliente.setNome(clienteResponseDTO.getNome());
        cliente.setLimite(clienteResponseDTO.getLimite());
        cliente.setSaldo(clienteResponseDTO.getSaldo());
        return cliente;
    }


}
