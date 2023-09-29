package com.ivansoft.java.dapr.emarket.stateservice.configuration;

import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DaprConfig {
    @Bean
    public DaprClient daprClient() {
        return new DaprClientBuilder().build();
    }
}
