package com.ivansoft.java.dapr.emarket.purchaseagent.services;

import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos;
import com.ivansoft.java.dapr.emarket.common.models.Order;
import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos.GetStateRequest;
import com.ivansoft.java.dapr.emarket.common.GrpcStateServiceProtos.GetStateResponse;
import com.ivansoft.java.dapr.emarket.common.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OrderStateService {
    private static final Logger log = Logger.getLogger(OrderStateService.class.getName());

    @Value("${orderstate.dapr.app.id}")
    private String ORDER_STATE_APP_ID;

    @Value("${orderstate.grpc.getstate.method}")
    private String METHOD_NAME;

    public Optional<Order> getState(String key) throws Exception {
        try(DaprClient client = new DaprClientBuilder().build()) {
            GetStateRequest getStateRequest = GetStateRequest.newBuilder()
                    .setKey(key)
                    .build();

            GetStateResponse resul = client.invokeMethod(ORDER_STATE_APP_ID, METHOD_NAME, getStateRequest,
                    HttpExtension.NONE, GetStateResponse.class).block();

            if (resul != null) {
                log.info("Order state retrieved successfully");
                // deserialize order
                Order order = Utils.deserializeOrder(resul.getValue());
                return Optional.of(order);
            } else {
                log.info("Order state not found");
                return Optional.empty();
            }
        }
    }
}
