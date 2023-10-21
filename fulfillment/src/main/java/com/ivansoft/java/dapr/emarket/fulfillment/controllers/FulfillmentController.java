package com.ivansoft.java.dapr.emarket.fulfillment.controllers;

import com.ivansoft.java.dapr.emarket.fulfillment.services.OrderStateService;
import io.dapr.Rule;
import io.dapr.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.publisher.Mono;
import io.dapr.client.domain.CloudEvent;
import com.ivansoft.java.dapr.emarket.common.models.Order;
import com.ivansoft.java.dapr.emarket.common.Utils;
import java.util.logging.Logger;
//import com.ivansoft.java.dapr.emarket.fulfillment.models.DaprSubscription;
//import com.ivansoft.java.dapr.emarket.fulfillment.models.SubscriptionData;
//import java.util.Map;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestHeader;


@RestController
public class FulfillmentController {
    private final static Logger logger = Logger.getLogger(FulfillmentController.class.getName());
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private OrderStateService orderStateService;

    /*
     * without CloudEvent, is more simple but don't have benefits of CloudEvent (metrics, tracing, etc.)
    @PostMapping(path="/createorder", consumes = MediaType.ALL_VALUE)
    public Mono<Void> createOrder(@RequestBody SubscriptionData<Order> event) {
        return Mono.fromRunnable(() -> {
            try {
                System.out.println("Received event with data: " + event.getData());
            } catch (Exception e) {
                logger.error("Error occurred while processing event: " + event, e);
                throw new RuntimeException(e);
            }
        });
    }*/

    @Topic(name = "${pubsub:topic}", pubsubName = "${pubsub:name}",
            //deadLetterTopic = "${pubsub:deadtopic}",
            rule = @Rule(match = "event.type == \"v2\"", priority = 1))
    @PostMapping(path = "/createorder")
    public Mono<Void> createOrderV2(@RequestBody(required = false) CloudEvent cloudEvent) {
        return Mono.fromRunnable(() -> {
            try {
                logger.info("Subscriber got: " + cloudEvent.getData());
                logger.info("Subscriber got: " + OBJECT_MAPPER.writeValueAsString(cloudEvent));

                Order order = Utils.deserializeOrder(cloudEvent.getData().toString());
                logger.info("Order received: " + order);

                orderStateService.saveOrder(order);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /*
     * Example for dead letter topics
    @Topic(name = "createorderfailed", pubsubName = "orderpubsub")
    @PostMapping(path = "/createorderfailed")
    public Mono<Void> handleMessageWithErrorHandler(@RequestBody(required = false) CloudEvent<String> cloudEvent) {
        return Mono.fromRunnable(() -> {
        try {
            logger.info("Subscriber got: " + cloudEvent.getData());
            logger.info("Subscriber got: " + OBJECT_MAPPER.writeValueAsString(cloudEvent));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        });
    }*/
}
