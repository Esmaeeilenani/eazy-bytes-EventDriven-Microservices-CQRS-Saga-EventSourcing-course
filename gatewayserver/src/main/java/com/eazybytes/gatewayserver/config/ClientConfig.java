package com.eazybytes.gatewayserver.config;

import com.eazybytes.gatewayserver.service.client.CustomerSummeryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfig {

    //Self URL
    @Value("${app.base-url}")
    private String baseUrl;


    //Building the webClient out of an interface
    @Bean
    CustomerSummeryClient customerClient() {
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CustomerSummeryClient.class);
    }


}
