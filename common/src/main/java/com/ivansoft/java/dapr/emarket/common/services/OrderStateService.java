package com.ivansoft.java.dapr.emarket.common.services;

import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos;
import com.ivansoft.java.dapr.emarket.common.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import com.ivansoft.java.dapr.emarket.common.Utils;
import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos.SaveStateRequest;

@Service
public class OrderStateService {
    private static final Logger log = Logger.getLogger(OrderStateService.class.getName());

    //@Value("${orderstate.dapr.app.id}")
    private final String ORDER_STATE_APP_ID;

    //@Value("${orderstate.grpc.savestate.method}")
    private final String METHOD_SAVE;

    //@Value("${orderstate.grpc.getstate.method}")
    private final String METHOD_GET;

    public OrderStateService(Environment env) {
        ORDER_STATE_APP_ID = env.getProperty("orderstate.dapr.app.id");
        METHOD_SAVE = env.getProperty("orderstate.grpc.savestate.method");
        METHOD_GET = env.getProperty("orderstate.grpc.getstate.method");
    }

    public Optional<Order> getState(String key) throws Exception {
        try(DaprClient client = new DaprClientBuilder().build()) {
            GrpcStateServiceProtos.GetStateRequest getStateRequest = GrpcStateServiceProtos.GetStateRequest.newBuilder()
                    .setKey(key)
                    .build();

            GrpcStateServiceProtos.GetStateResponse resul = client.invokeMethod(ORDER_STATE_APP_ID, METHOD_GET, getStateRequest,
                    HttpExtension.NONE, GrpcStateServiceProtos.GetStateResponse.class).block();

            if (resul != null) {
                log.info("resul.getResponse().getStatus(): " + resul.getResponse().getStatus());
                log.info("resul.getValue(): " + resul.getValue());

                if (resul.getResponse().getStatus().equals("OK")) {
                    Order order = Utils.deserializeOrder(resul.getValue());
                    return Optional.of(order);
                }

                return Optional.empty();
            }
            else {
                throw new Exception("Something went wrong while getting the order state");
            }
        }
    }

    public void saveOrder(Order order) throws Exception {
        try(DaprClient client = new DaprClientBuilder().build()) {
            // serialize order
            String serializedOrder = Utils.serializeOrder(order);
            SaveStateRequest saveStateRequest = SaveStateRequest.newBuilder()
                    .setKey(java.util.UUID.randomUUID().toString())
                    .setValue(serializedOrder)
                    .build();

            // invoke save state method (grpc)
            client.invokeMethod(ORDER_STATE_APP_ID, METHOD_SAVE, saveStateRequest, HttpExtension.NONE).block();
            log.info("Order state saved successfully");
        }
    }
}
