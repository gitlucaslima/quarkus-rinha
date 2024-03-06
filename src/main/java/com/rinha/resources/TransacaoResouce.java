package com.rinha.resources;

import com.rinha.domain.clientes.ClienteRepository;
import com.rinha.domain.clientes.dtos.ClienteResponseDTO;
import com.rinha.domain.clientes.mappers.ClienteMapper;
import com.rinha.domain.transacoes.TransacaoService;
import com.rinha.domain.transacoes.dtos.GetTransacaoDTO;
import com.rinha.domain.transacoes.dtos.PostRequestDTO;
import com.rinha.domain.transacoes.dtos.PostResponseDTO;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clientes")
public class TransacaoResouce {

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    ClienteMapper clienteMapper;

    @Inject
    TransacaoService transacaoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<ClienteResponseDTO>> teste() {
        return clienteRepository.findAllClientes().map(clientes -> clienteMapper.toDTO(clientes));
    }

    @POST
    @Path("/{id}/transacoes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> transacoes(@PathParam("id") Long idCliente, @Valid PostRequestDTO body) {
        return transacaoService.realizarTransacao(idCliente, body).map(postResponseDTO -> Response.ok(postResponseDTO).build()).onFailure().recoverWithItem(throwable -> {
            if (throwable instanceof EntityNotFoundException) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            if (throwable instanceof IllegalArgumentException) {
                return Response.status(422).entity(throwable.getMessage()).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        });
    }


    @GET
    @Path("/{id}/extrato")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> extrato(@PathParam("id") Long id) {
        return transacaoService.getExtrato(id).map(getTransacaoDTO -> Response.ok(getTransacaoDTO).build()).onFailure().recoverWithItem(throwable -> {
            if (throwable instanceof EntityNotFoundException) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        });
    }


}
