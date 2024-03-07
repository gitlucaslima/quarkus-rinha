package com.rinha.domain.transacoes;

import com.rinha.domain.clientes.ClienteRepository;
import com.rinha.domain.transacoes.dtos.*;
import com.rinha.domain.transacoes.enums.TipoTransacao;
import io.quarkus.cache.CacheKey;
import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

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

        if (body.getTipo() == null || !body.getTipo().equals("c") && !body.getTipo().equals("d")) {
            return Uni.createFrom().failure(new IllegalArgumentException("Tipo é invalido, deve ser 'c' para credito e 'd' para debito"));
        }

        // Verificar se a descrição é nula ou excede o limite de caracteres
        if (body.getDescricao() == null || body.getDescricao().length() > 10) {
            return Uni.createFrom().failure(new IllegalArgumentException("Descrição é obrigatória e deve ter no máximo 10 caracteres"));
        }

        // Criar uma instância do PostResponseDTO
        PostResponseDTO response = new PostResponseDTO();

        // Criar uma instância da Transacao
        Transacao novaTransacao = new Transacao();
        novaTransacao.setClienteId(idCliente);
        novaTransacao.setTipo(TipoTransacao.valueOf(body.getTipo()));
        novaTransacao.setValor(body.getValor());
        novaTransacao.setDescricao(body.getDescricao());
        novaTransacao.setData(String.valueOf(Timestamp.from(Instant.now())));


        // Verificar se o cliente possui limite suficiente antes de persistir a transação
        return clienteRepository.findById(idCliente)
                .onItem().transformToUni(cliente -> {
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

                    return clienteRepository.persistOrUpdate(cliente);
                })
                .onItem().transformToUni(clienteAtualizado -> transacaoRepository.persist(novaTransacao)
                        .map(transacaoPersistida -> {
                            response.setLimite(clienteAtualizado.getLimite());
                            response.setSaldo(clienteAtualizado.getSaldo());
                            return response;
                        }));
    }


    @CacheResult(cacheName = "extrato")
    public Uni<GetTransacaoDTO> getExtrato(@CacheKey Long id) {
        return clienteRepository.findById(id).onItem().transformToUni(cliente -> {
            GetTransacaoDTO response = new GetTransacaoDTO();

            // Configurar Saldo
            SaldoDTO saldo = new SaldoDTO();
            saldo.setTotal(cliente.getSaldo());
            saldo.setLimite(cliente.getLimite());
            saldo.setData_extrato(String.valueOf(Timestamp.from(Instant.now())));
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
