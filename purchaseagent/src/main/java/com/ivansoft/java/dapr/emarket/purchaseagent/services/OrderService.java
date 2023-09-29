package com.ivansoft.java.dapr.emarket.purchaseagent.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ivansoft.java.dapr.emarket.purchaseagent.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.Metadata;
import static java.util.Collections.singletonMap;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    @Value("${dapr.pubsub.message_ttl_in_seconds}")
    private String MESSAGE_TTL_IN_SECONDS;
    @Value("${dapr.pubsub.name}")
    private String PUBSUB_NAME;
    @Value("${dapr.pubsub.topic}")
    private String TOPIC_NAME;

    public void publishOrder(Order order) {
        log.info("Publishing order: {}", order);
        log.info("MESSAGE_TTL_IN_SECONDS: {}", MESSAGE_TTL_IN_SECONDS);
        log.info("PUBSUB_NAME: {}", PUBSUB_NAME);
        log.info("TOPIC_NAME: {}", TOPIC_NAME);

        DaprClient client = new DaprClientBuilder().build();

        client.publishEvent(PUBSUB_NAME, 
                            TOPIC_NAME, 
                            order,
                            singletonMap(Metadata.TTL_IN_SECONDS, MESSAGE_TTL_IN_SECONDS)).block();
        log.info("Order published: {}", order);
    }
}
