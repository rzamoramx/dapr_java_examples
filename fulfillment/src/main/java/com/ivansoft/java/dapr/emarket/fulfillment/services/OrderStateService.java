package com.ivansoft.java.dapr.emarket.fulfillment.services;

import ch.qos.logback.classic.Logger;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ivansoft.java.dapr.emarket.fulfillment.models.Order;

@Service
public class OrderStateService {
    private static final Logger log = (Logger) LoggerFactory.getLogger(OrderStateService.class);

    @Value("${orderstateapp.id}")
    private String ORDER_STATE_APP_ID;
    @Value("${orderstateapp.methodname}")
    private String METHOD_NAME;

    public void saveOrder(Order order) throws Exception {
        try(DaprClient client = new DaprClientBuilder().build()) {
            client.invokeMethod(ORDER_STATE_APP_ID, METHOD_NAME, order, HttpExtension.NONE).block();
            log.info("Order state saved successfully");
        }
    }
}
