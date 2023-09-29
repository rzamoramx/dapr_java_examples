package com.ivansoft.java.dapr.emarket.fulfillment.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import io.dapr.Topic;
import io.dapr.client.domain.CloudEvent;

@RestController
public class FulfillmentController {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Topic(name = "createorder", pubsubName = "pubsub")
    @PostMapping("/createorder")
    public Mono<Void> createOrder(@RequestBody(required = false) CloudEvent<String> event) {
        return Mono.fromRunnable(() -> {
            try {
                System.out.println("Received event with data: " + event.getData());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    @Topic(name = "createorderfailed", pubsubName = "pubsub")
    @PostMapping(path = "/createorderfailed")
    public Mono<Void> handleMessageWithErrorHandler(@RequestBody(required = false) CloudEvent<String> cloudEvent) {
        return Mono.fromRunnable(() -> {
        try {
            System.out.println("Subscriber got: " + cloudEvent.getData());
            System.out.println("Subscriber got: " + OBJECT_MAPPER.writeValueAsString(cloudEvent));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        });
    }
}
