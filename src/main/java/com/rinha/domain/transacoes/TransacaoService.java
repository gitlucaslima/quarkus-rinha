package com.rinha.domain.transacoes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.rinha.domain.clientes.ClienteRepository;
import com.rinha.domain.transacoes.dtos.*;
import com.rinha.domain.transacoes.enums.TipoTransacao;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.stream.Collectors;

@ApplicationScoped
public class TransacaoService {

    @Inject
    TransacaoRepository transacaoRepository;

    @Inject
    ClienteRepository clienteRepository;

    @Inject
    RedisService redisService;

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
            return transacaoRepository.persist(novaTransacao).onItem().transformToUni(ignore -> clienteRepository.persistOrUpdate(cliente)).onItem().transform(updatedCliente -> {
                PostResponseDTO response = new PostResponseDTO();
                response.setLimite(updatedCliente.getLimite());
                response.setSaldo(updatedCliente.getSaldo());

                this.redisService.existsKey("extrato_" + idCliente.toString()).subscribe().with(exists -> {
                    if (exists) {
                        this.redisService.deleteReactiveKey("extrato_" + idCliente.toString());
                    }
                });
                return response;
            });
        });
    }


    public Uni<GetTransacaoDTO> extrato(Long id) {

        return clienteRepository.findById(id).onItem().transformToUni(cliente -> redisService.getReactiveValue("extrato_" + id.toString()).flatMap(cachedExtrato -> {
            System.out.println("dentro metodo extrato");
            if (cachedExtrato != null && !cachedExtrato.isEmpty()) {
                System.out.println("pesquisando no cache");
                GetTransacaoDTO response = jsonToExtrato(cachedExtrato);
                return Uni.createFrom().item(response);
            } else {
                // Se a lista de transações não estiver no cache ou estiver vazia, consultar o banco de dados
                return transacaoRepository.findLast10ByClienteIdAndOrderByDataDesc(id).map(transacoes -> {
                    System.out.println("pesquisando no banco de dados");
                    GetTransacaoDTO response = new GetTransacaoDTO();
                    // Converter as transações em DTO
                    var transacoesDto = transacoes.stream().map(transacao -> {
                        TransacaoDTO transacaoDTO = new TransacaoDTO();
                        transacaoDTO.setTipo(transacao.getTipo());
                        transacaoDTO.setValor(transacao.getValor());
                        transacaoDTO.setDescricao(transacao.getDescricao());
                        transacaoDTO.setRealizada_em(transacao.getData());
                        return transacaoDTO;
                    }).collect(Collectors.toList());
                    SaldoDTO saldo = new SaldoDTO();
                    saldo.setLimite(cliente.getLimite());
                    saldo.setTotal(cliente.getSaldo());
                    saldo.setData_extrato(LocalDate.now().toString());
                    response.setSaldo(saldo);
                    response.setUltimas_transacoes(transacoesDto);
                    return response;
                }).onItem().transform(response -> {
                    redisService.setReactiveValue("extrato_" + id.toString(), listTransacoesToJson(response));
                    return response;
                });
            }
        }));
    }

    private GetTransacaoDTO jsonToExtrato(String extrato) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Usamos o TypeReference para indicar que estamos lendo uma lista de TransacaoDTO
            return objectMapper.readValue(extrato, new TypeReference<GetTransacaoDTO>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String listTransacoesToJson(GetTransacaoDTO extrato) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        try {
            return objectMapper.writeValueAsString(extrato);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
