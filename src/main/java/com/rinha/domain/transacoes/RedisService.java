package com.rinha.domain.transacoes;

import io.quarkus.redis.client.reactive.ReactiveRedisClient;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.redis.client.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class RedisService {

    @Inject
    ReactiveRedisClient redisClient;

    public Uni<List<String>> getReactiveKeys() {
        return redisClient.keys("*").map(response -> {
            List<String> result = new ArrayList<>();
            response.forEach(key -> result.add(key.toString()));
            return result;
        });
    }

    public Uni<Boolean> existsKey(String key) {
        return redisClient.exists(Collections.singletonList(key)).map(Response::toBoolean);
    }

    public Uni<String> setReactiveKey(String key, String value) {
        return redisClient.set(Arrays.asList(key, value)).map(Response::toString);
    }

    public Uni<String> getReactiveValue(String key) {

        return this.existsKey(key).flatMap(exists -> {
            if (exists) {
                System.out.println("Existe a chave: " + key);
                return this.redisClient.get(key).map(Response::toString);
            } else {
                System.out.println("Nao existe a chave: " + key);
                return Uni.createFrom().nullItem();
            }
        });
    }

    public void setReactiveValue(String key, String value) {
        redisClient.set(Arrays.asList(key, value)).subscribe().with(response -> {
        });
    }

    public void deleteReactiveKey(String key) {
        redisClient.del(Collections.singletonList(key)).subscribe().with(response -> {
        });
    }

}
