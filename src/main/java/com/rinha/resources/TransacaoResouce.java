package com.rinha.resources;

import com.rinha.domain.transacoes.RedisService;
import com.rinha.domain.transacoes.TransacaoService;
import com.rinha.domain.transacoes.dtos.PostRequestDTO;
import com.rinha.domain.transacoes.enums.TesteCache;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clientes")
public class TransacaoResouce {

    @Inject
    TransacaoService transacaoService;

    @Inject
    RedisService redisService;


    @POST
    @Path("/{id}/transacoes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> transacoes(@PathParam("id") Long idCliente, @Valid PostRequestDTO body) {
        return transacaoService.realizarTransacao(idCliente, body).map(postResponseDTO -> Response.status(200).entity(postResponseDTO).build()).onFailure().recoverWithItem(throwable -> {
            if (throwable instanceof EntityNotFoundException) {
                return Response.status(Response.Status.NOT_FOUND).entity(throwable.getMessage()).build();
            }
            if (throwable instanceof IllegalArgumentException) {
                return Response.status(422).entity(throwable.getMessage()).build();
            }
            if (throwable instanceof ValidationException) {
                return Response.status(422).entity(throwable.getMessage()).build();
            }
            return Response.status(422).entity(throwable.toString()).build();
        });
    }

    @GET
    @Path("/{id}/extrato")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> extrato(@PathParam("id") Long id) {
        return transacaoService.extrato(id).map(getTransacaoDTO -> Response.status(200).entity(getTransacaoDTO).build()).onFailure().recoverWithItem(throwable -> {
            if (throwable instanceof EntityNotFoundException) {
                return Response.status(Response.Status.NOT_FOUND).entity(throwable.getMessage()).build();
            } else if (throwable instanceof IllegalArgumentException) {
                return Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build();
            } else if (throwable instanceof ValidationException) {
                return Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build();
            }
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(throwable.getMessage()).build();
        });
    }


    @POST
    @Path("/cache")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void cache(TesteCache testeCache) {
        redisService.setReactiveValue(testeCache.getId(), testeCache.getNome());
    }

    @GET
    @Path("/cache/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<String> cache(@PathParam("key") String key) {
        return redisService.getReactiveValue(key);
    }

    @GET
    @Path("/cache/keys")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<String>> cacheKeys() {
        return redisService.getReactiveKeys();
    }

}
