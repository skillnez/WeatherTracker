package com.skillnez.weathertracker.service;

import com.skillnez.weathertracker.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Service
public class ApiClientService {

    private final WebClient webClient;

    private static final int CONNECTION_TIMEOUT_SECONDS = 20;

    @Autowired
    public ApiClientService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String getApiResponse (String requestUri, Object... uriVariables) {
        return webClient.get()
                .uri(requestUri, uriVariables)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> {
                                    log.error("API error. Status: {}, Body: {}", clientResponse.statusCode(), body);
                                    return new ApiException("Api response error has occurred " + body);
                                })
                )
                .bodyToMono(String.class)
                .doOnSubscribe(subscription ->
                        log.info("Calling external API: {}", requestUri)
                )
                .doOnSuccess(body ->
                        log.debug("Received response from {}: {}", requestUri, body)
                )
                .doOnError(e ->
                        log.error("Error during API call to {}: {}", requestUri, e.getMessage(), e)
                )
                .onErrorResume(e -> Mono.error(new ApiException("Unexpected api response error has occurred")))
                .timeout(Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS))
                .log()
                .block();
    }

}
