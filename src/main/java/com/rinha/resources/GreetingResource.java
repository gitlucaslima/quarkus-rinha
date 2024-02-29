package com.rinha.resources;

import com.rinha.domain.clientes.Cliente;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/clientes")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> clientes() {
        return Cliente.findAll().list().onItem().transform(clientes -> Response.ok(clientes).build()).onFailure().recoverWithItem(Response.serverError().build());
    }

    @POST
    @Path("/{id}/transacoes")
    @Produces(MediaType.APPLICATION_JSON)
    public String transacoes() {
        return "Transacoes";
    }

    @GET
    @Path("/{id}/extrato")
    @Produces(MediaType.APPLICATION_JSON)
    public String extrato() {
        return "Extrato";
    }

}
