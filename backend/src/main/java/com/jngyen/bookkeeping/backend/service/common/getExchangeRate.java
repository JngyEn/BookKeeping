package com.jngyen.bookkeeping.backend.service.common;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@Service
public class getExchangeRate {

  public Mono<JsonNode> getExchangeRateByBaseCurrency(String baseCurrency) {
    WebClient webClient = WebClient.create("https://v6.exchangerate-api.com/v6/94b7b12367902c235b7894fb/latest");
    Mono<JsonNode> exchangeRate = webClient.get()
        .uri("/" + baseCurrency)
        .retrieve()
        .bodyToMono(String.class)
        .map(response -> {
          try {
            return new ObjectMapper().readTree(response);
          } catch (Exception e) {
            return null;
          }

        });
    return exchangeRate;
  }
    
}
