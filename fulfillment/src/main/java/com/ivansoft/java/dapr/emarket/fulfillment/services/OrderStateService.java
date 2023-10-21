package com.ivansoft.java.dapr.emarket.fulfillment.services;

import com.ivansoft.java.dapr.emarket.common.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ivansoft.java.dapr.emarket.common.Utils;
import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos.SaveStateRequest;

@Service
public class OrderStateService {
    private static final Logger log = Logger.getLogger(OrderStateService.class.getName());

    @Value("${orderstate.dapr.app.id}")
    private String ORDER_STATE_APP_ID;
    @Value("${orderstate.grpc.savestate.method}")
    private String METHOD_NAME;

    public void saveOrder(Order order) throws Exception {
        try(DaprClient client = new DaprClientBuilder().build()) {
            // serialize order
            String serializedOrder = Utils.serializeOrder(order);
            SaveStateRequest saveStateRequest = SaveStateRequest.newBuilder()
                    .setKey(java.util.UUID.randomUUID().toString())
                    .setValue(serializedOrder)
                    .build();

            // invoke save state method (grpc)
            client.invokeMethod(ORDER_STATE_APP_ID, METHOD_NAME, serializedOrder, HttpExtension.NONE).block();
            log.info("Order state saved successfully");
        }
    }
}
