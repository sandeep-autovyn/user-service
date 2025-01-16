package com.user.management.service;

import com.user.management.entity.RandomUserApiData;
import com.user.management.exception.RandomUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class RandomUserGenerationWebService implements RandomUserGenerationService {
    private final WebClient webClient;


    public String randomUserBaseUrl = "https://randomuser.me/";

    public RandomUserGenerationWebService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(randomUserBaseUrl).build();
    }


    public RandomUserApiData generateRandomUsers_(int count) {
        log.info("Fetching Random User");
        Object ob = WebClient.builder().build()
                .get()
                .uri(randomUserBaseUrl + ServiceConstant.GET_RANDOM_USER + count)
                .retrieve().
                onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    log.error("Error while fetching Random User giving the 4XX error");
                    throw new RandomUserException("Error while fetching Random User", "4XX ERROR");
                }).onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                    log.error("Error while fetching Random User giving the 5XX error");
                    throw new RandomUserException("Error while fetching Random User", "5XX ERROR");
                }).bodyToMono(Object.class)
                .log().block();

        System.out.println(ob.toString());
        return null;
    }

    @Override
    public RandomUserApiData generateRandomUsers(int count) {
        log.info("Fetching Random User");
        String uri = UriComponentsBuilder.fromHttpUrl(randomUserBaseUrl + ServiceConstant.GET_RANDOM_USER + count)
                .toUriString();
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<RandomUserApiData> responseEntity = restTemplate.getForEntity(uri, RandomUserApiData.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                return responseEntity.getBody();
            } else {
                log.error("Error fetching Random User, status: {}", responseEntity.getStatusCode());
                throw new RandomUserException("Error while fetching Random User", responseEntity.getStatusCode().toString());
            }
        } catch (HttpClientErrorException e) {
            log.error("Client error while fetching Random User", e);
            throw new RandomUserException("Error while fetching Random User", "4XX ERROR");
        } catch (HttpServerErrorException e) {
            log.error("Server error while fetching Random User", e);
            throw new RandomUserException("Error while fetching Random User", "5XX ERROR");
        } catch (Exception e) {
            log.error("Unexpected error while fetching Random User", e);
            throw new RandomUserException("Error while fetching Random User", "UNKNOWN ERROR");
        }
    }


    public RandomUserApiData getRandomUserFallback(Throwable t) {
        log.error("Error while fetching Random User", t);
        return new RandomUserApiData();
    }
}
