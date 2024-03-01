package com.rinha.resources;

import com.rinha.domain.clientes.ClienteRepository;
import com.rinha.domain.clientes.dtos.ClienteResponseDTO;
import com.rinha.domain.clientes.mappers.ClienteMapper;
import com.rinha.domain.transacoes.TransacaoService;
import com.rinha.domain.transacoes.dtos.PostRequestDTO;
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
    public Uni<Response> transacoes(@PathParam("id") Long id, @Valid PostRequestDTO body) {
        return transacaoService.create(id, body).map(postResponseDTO -> Response.ok(postResponseDTO).build()).onFailure().recoverWithItem(error -> switch (getClassification(error)) {
            case ENTITY_NOT_FOUND -> Response.status(Response.Status.NOT_FOUND).build(); // 404: Recurso não encontrado
            case ILLEGAL_ARGUMENT -> Response.status(422).build(); // 422: Entidade não processável
            default -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // 500: Erro interno do servidor
        });
    }

    @GET
    @Path("/{id}/extrato")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> extrato(@PathParam("id") Long id) {
        return transacaoService.getExtrato(id).map(getTransacaoDTO -> Response.ok(getTransacaoDTO).build()).onFailure().recoverWithItem(error -> switch (getClassification(error)) {
            case ENTITY_NOT_FOUND -> Response.status(Response.Status.NOT_FOUND).build(); // 404: Recurso não encontrado
            default -> Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // 500: Erro interno do servidor
        });
    }


    private ExceptionClassification getClassification(Throwable throwable) {
        if (throwable instanceof EntityNotFoundException) {
            return ExceptionClassification.ENTITY_NOT_FOUND;
        } else if (throwable instanceof IllegalArgumentException) {
            return ExceptionClassification.UNPROCESSABLE_ENTITY;
        }
        return ExceptionClassification.OTHER;
    }

    enum ExceptionClassification {
        ENTITY_NOT_FOUND, ILLEGAL_ARGUMENT, UNPROCESSABLE_ENTITY, OTHER
    }


}
