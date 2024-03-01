package com.rinha.domain.transacoes;

import com.rinha.domain.clientes.Cliente;
import com.rinha.domain.clientes.ClienteRepository;
import com.rinha.domain.transacoes.dtos.*;
import com.rinha.domain.transacoes.enums.TipoTransacao;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransacaoService {

    @Inject
    TransacaoRepository transacaoRepository;

    @Inject
    ClienteRepository clienteRepository;

    public Uni<PostResponseDTO> create(Long idClient, PostRequestDTO transacao) {
        return clienteRepository.findById(idClient).onItem().transformToUni(cliente -> realizarTransacao(cliente, transacao));
    }

    private Uni<PostResponseDTO> realizarTransacao(Cliente clienteAlvo, PostRequestDTO body) {
        TipoTransacao tipo = body.getTipo();

        if (body.getDescricao().isEmpty() || body.getDescricao().length() > 10) {
            return Uni.createFrom().failure(new IllegalArgumentException("Limite da descrição para transação é de 10 caracteres"));
        }

        Uni<Transacao> transacaoUni;
        if (tipo == TipoTransacao.c) {
            transacaoUni = realizarTransacaoCredito(clienteAlvo, body);
        } else if (tipo == TipoTransacao.d) {
            transacaoUni = realizarTransacaoDebito(clienteAlvo, body);
        } else {
            return Uni.createFrom().failure(new IllegalArgumentException("Tipo de transação inválido"));
        }

        return transacaoUni.onItem().transform(transacao -> {
            PostResponseDTO response = new PostResponseDTO();
            preencherResponse(response, clienteAlvo);
            return response;
        });
    }


    private Uni<Transacao> realizarTransacaoCredito(Cliente cliente, PostRequestDTO body) {
        Long saldoAntigo = cliente.getSaldo();
        cliente.setSaldo(saldoAntigo + body.getValor());

        Transacao transacao = new Transacao();
        transacao.setTipo(body.getTipo());
        transacao.setValor(body.getValor());
        transacao.setDescricao(body.getDescricao());
        transacao.setData(Timestamp.from(Instant.now()));
        transacao.setClienteId(cliente.getIdCliente());

        return transacaoRepository.persist(transacao);
    }

    private Uni<Transacao> realizarTransacaoDebito(Cliente cliente, PostRequestDTO body) {
        return validarLimiteDisponivel(cliente, body.getValor()).flatMap(valido -> {
            if (!valido) {
                return Uni.createFrom().failure(new IllegalArgumentException("Transação de débito excede o limite disponível"));
            }
            Long saldoAntigo = cliente.getSaldo();
            cliente.setSaldo(saldoAntigo - body.getValor());

            Transacao transacao = new Transacao();
            transacao.setTipo(body.getTipo());
            transacao.setValor(body.getValor());
            transacao.setDescricao(body.getDescricao());
            transacao.setData(Timestamp.from(Instant.now()));
            transacao.setClienteId(cliente.getIdCliente());

            return transacaoRepository.persist(transacao);
        });
    }

    private Uni<Boolean> validarLimiteDisponivel(Cliente cliente, Long valor) {
        long limiteDisponivel = cliente.getLimite() * -1;
        return Uni.createFrom().item((cliente.getSaldo() - valor) >= limiteDisponivel);
    }


    private void preencherResponse(PostResponseDTO response, Cliente cliente) {
        response.setSaldo(cliente.getSaldo());
        response.setLimite(cliente.getLimite());
    }

    public Uni<GetTransacaoDTO> getExtrato(Long id) {
        return clienteRepository.findById(id).onItem().transformToUni(cliente -> {
            GetTransacaoDTO response = new GetTransacaoDTO();

            // Configurar Saldo
            SaldoDTO saldo = new SaldoDTO();
            saldo.setTotal(cliente.getSaldo());
            saldo.setLimite(cliente.getLimite());
            saldo.setData_extrato(Timestamp.from(Instant.now()));
            response.setSaldo(saldo);

            // Configurar Lista de Últimas Transações
            return transacaoRepository.findAllByClienteId(id).map(t -> t.stream().map(transacao -> {
                        TransacaoDTO transacaoDTO = new TransacaoDTO();
                        transacaoDTO.setTipo(transacao.getTipo());
                        transacaoDTO.setValor(transacao.getValor());
                        transacaoDTO.setDescricao(transacao.getDescricao());
                        transacaoDTO.setRealizada_em(transacao.getData());
                        return transacaoDTO;
                    }).collect(Collectors.toList())) // Convertendo Stream para List
                    .onItem().invoke(response::setUltimas_transacoes) // Configurando a resposta com as últimas transações
                    .replaceWith(response); // Retornando a resposta completa
        });
    }


}
