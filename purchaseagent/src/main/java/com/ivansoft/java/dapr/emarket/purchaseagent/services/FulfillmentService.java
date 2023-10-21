package com.ivansoft.java.dapr.emarket.purchaseagent.services;

import com.ivansoft.java.dapr.emarket.common.models.Order;
import com.ivansoft.java.dapr.emarket.common.Utils;
import io.dapr.client.domain.CloudEvent;
import io.dapr.client.domain.PublishEventRequest;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.Metadata;
import java.io.IOException;
import java.util.UUID;
import static java.util.Collections.singletonMap;

@Service
public class FulfillmentService {
    private static final Logger log = Logger.getLogger(FulfillmentService.class.getName());
    @Value("${dapr.pubsub.message_ttl_in_seconds}")
    private String MESSAGE_TTL_IN_SECONDS;
    @Value("${dapr.pubsub.name}")
    private String PUBSUB_NAME;
    @Value("${dapr.pubsub.topic}")
    private String TOPIC_NAME;

    public void publishOrder(Order order) throws IOException {
        log.info("Publishing order: " + order);
        log.info("MESSAGE_TTL_IN_SECONDS: " + MESSAGE_TTL_IN_SECONDS);
        log.info("PUBSUB_NAME: " + PUBSUB_NAME);
        log.info("TOPIC_NAME: " + TOPIC_NAME);

        DaprClient client = new DaprClientBuilder().build();

        /*
         * Publish without CloudEvent, is more simple but don't have benefits of CloudEvent (metrics, tracing, etc.)
        client.publishEvent(PUBSUB_NAME,
                            TOPIC_NAME, 
                            order,
                            singletonMap(Metadata.TTL_IN_SECONDS, MESSAGE_TTL_IN_SECONDS)).block();
        log.info("Order published: {}", order);*/

        CloudEvent<String> cloudEvent = new CloudEvent<>();
        cloudEvent.setId(UUID.randomUUID().toString());
        cloudEvent.setType("example");
        cloudEvent.setSpecversion("1");
        cloudEvent.setDatacontenttype("text/plain");
        // serialize order
        cloudEvent.setData(Utils.serializeOrder(order));

        //Publishing messages
        client.publishEvent(
                new PublishEventRequest(PUBSUB_NAME, TOPIC_NAME, cloudEvent)
                        .setContentType(CloudEvent.CONTENT_TYPE)
                        .setMetadata(singletonMap(Metadata.TTL_IN_SECONDS, MESSAGE_TTL_IN_SECONDS))).block();
        log.info("Published cloud event with message: " + cloudEvent.getData());
    }
}
