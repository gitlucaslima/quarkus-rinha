package com.rinha.domain.transacoes;

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


    public Uni<PostResponseDTO> realizarTransacao(Long idCliente, PostRequestDTO body) {
        // Verificar se o valor é nulo ou menor que 1
        if (body.getValor() == null || body.getValor() < 1) {
            return Uni.createFrom().failure(new IllegalArgumentException("Valor deve ser maior que 0"));
        }

        if (body.getTipo() == null || body.getTipo().isEmpty() || !body.getTipo().equals("c") && !body.getTipo().equals("d")) {
            return Uni.createFrom().failure(new IllegalArgumentException("Tipo é inválido, deve ser 'c' para crédito e 'd' para débito"));
        }

        // Verificar se a descrição é nula ou excede o limite de caracteres
        if (body.getDescricao() == null || body.getDescricao().isEmpty() || body.getDescricao().length() > 10) {
            return Uni.createFrom().failure(new IllegalArgumentException("Descrição é obrigatória e deve ter no máximo 10 caracteres"));
        }

        // Criar uma instância da Transacao
        Transacao novaTransacao = new Transacao();
        novaTransacao.setClienteId(idCliente);
        novaTransacao.setTipo(TipoTransacao.valueOf(body.getTipo()));
        novaTransacao.setValor(body.getValor());
        novaTransacao.setDescricao(body.getDescricao());
        novaTransacao.setData(Timestamp.from(Instant.now()));

        // Verificar se o cliente possui limite suficiente antes de persistir a transação
        return clienteRepository.findById(idCliente).onItem().transformToUni(cliente -> {
            if (TipoTransacao.valueOf(body.getTipo()).equals(TipoTransacao.c)) {
                cliente.setSaldo(cliente.getSaldo() + body.getValor());
            } else if (TipoTransacao.valueOf(body.getTipo()).equals(TipoTransacao.d)) {
                long novoSaldo = cliente.getSaldo() - body.getValor();
                if (novoSaldo < -cliente.getLimite()) {
                    return Uni.createFrom().failure(new IllegalArgumentException("Limite insuficiente para realizar a transação de débito"));
                }
                cliente.setSaldo(cliente.getSaldo() - body.getValor());
            } else {
                return Uni.createFrom().failure(new IllegalArgumentException("Tipo de transação não suportado: " + body.getTipo()));
            }

            // Persistir a transação e atualizar o cliente
            return transacaoRepository.persist(novaTransacao).onItem().transformToUni(ignore -> clienteRepository.persistOrUpdate(cliente))
                    .onItem().transform(updatedCliente -> {
                        PostResponseDTO response = new PostResponseDTO();
                        response.setLimite(updatedCliente.getLimite());
                        response.setSaldo(updatedCliente.getSaldo());
                        return response;
                    });
        });
    }


    public Uni<GetTransacaoDTO> extrato(Long id) {
        return clienteRepository.findById(id).onItem().transformToUni(cliente -> {
            GetTransacaoDTO response = new GetTransacaoDTO();

            // Configurar Saldo
            SaldoDTO saldo = new SaldoDTO();
            saldo.setTotal(cliente.getSaldo());
            saldo.setLimite(cliente.getLimite());
            saldo.setData_extrato(String.valueOf(Timestamp.from(Instant.now())));
            response.setSaldo(saldo);

            // Configurar Lista de Últimas Transações
            return transacaoRepository.findLast10ByClienteIdAndOrderByDataDesc(id).map(t -> t.stream().map(transacao -> {
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
