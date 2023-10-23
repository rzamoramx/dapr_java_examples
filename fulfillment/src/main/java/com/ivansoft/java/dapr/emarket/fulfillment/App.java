package com.ivansoft.java.dapr.emarket.fulfillment;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ivansoft.java.dapr.emarket.common", "com.ivansoft.java.dapr.emarket.fulfillment"})
public class App {
    public static void main(String[] args) {
        // start spring boot application
        org.springframework.boot.SpringApplication.run(App.class, args);
    }
}
