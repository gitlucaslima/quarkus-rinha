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
        // Criar uma instância do PostResponseDTO
        PostResponseDTO response = new PostResponseDTO();

        // Criar uma instância da Transacao
        Transacao novaTransacao = new Transacao();
        novaTransacao.setClienteId(idCliente);
        novaTransacao.setTipo(body.getTipo());
        novaTransacao.setValor(body.getValor());
        novaTransacao.setDescricao(body.getDescricao());
        novaTransacao.setData(String.valueOf(Timestamp.from(Instant.now())));

        // Verificar se o cliente possui limite suficiente antes de persistir a transação
        return clienteRepository.findById(idCliente)
                .onItem().transformToUni(cliente -> {
                    if (body.getTipo().equals(TipoTransacao.c)) {
                        cliente.setSaldo(cliente.getSaldo() + body.getValor());
                    } else if (body.getTipo().equals(TipoTransacao.d)) {
                        long novoSaldo = cliente.getSaldo() - body.getValor();
                        if (novoSaldo < -cliente.getLimite()) {
                            throw new IllegalArgumentException("Limite insuficiente para realizar a transação de débito");
                        }
                        cliente.setSaldo(cliente.getSaldo() - body.getValor());
                    } else {
                        throw new IllegalArgumentException("Tipo de transação não suportado: " + body.getTipo());
                    }

                    return clienteRepository.persistOrUpdate(cliente).replaceWith(cliente);
                })
                .onItem().transformToUni(clienteAtualizado -> transacaoRepository.persist(novaTransacao)
                        .map(transacaoPersistida -> {
                            response.setLimite(clienteAtualizado.getLimite());
                            response.setSaldo(clienteAtualizado.getSaldo());
                            return response;
                        }));
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
                        transacaoDTO.setRealizada_em(Timestamp.valueOf(transacao.getData()));
                        return transacaoDTO;
                    }).collect(Collectors.toList())) // Convertendo Stream para List
                    .onItem().invoke(response::setUltimas_transacoes) // Configurando a resposta com as últimas transações
                    .replaceWith(response); // Retornando a resposta completa
        });
    }


}
