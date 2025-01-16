package com.user.management.config;

import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;


@Configuration
public class WebClientConfig {

    @Value("${random.user.baseurl}")
    public String RANDOM_USER_BASE_URL;

    //@Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create()
                .wiretap(this.getClass().getCanonicalName(), LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
        ClientHttpConnector conn = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder().baseUrl(RANDOM_USER_BASE_URL)
                .clientConnector(conn)
                .build();
    }
}
