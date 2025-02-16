package com.ceres.erostabler.services;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuotesService {
    private static final String RANDOM_SINGLE_QUOTE_ENDPOINT = "https://zenquotes.io/api/random";
    private final RestTemplate restTemplate;

    public List<?> getRandomQuote() {
        RestClient restClient = RestClient.builder()
                .baseUrl(RANDOM_SINGLE_QUOTE_ENDPOINT)
                .build();

        Object body = restClient.get()
                .uri(RANDOM_SINGLE_QUOTE_ENDPOINT)
                .retrieve().body(Object.class);

        List<?> list = new ArrayList<>();

        if (body != null && body.getClass().isArray()) {
             var ls  = List.of(body);

             list = ls.stream()
                     .flatMap(o -> JSON.parseArray(o.toString()).stream())
                     .toList();

        } else if (body instanceof Collection) {
            list = new ArrayList<>((Collection<?>) body);
        }

        System.out.println(body);
        return list;
    }
}
