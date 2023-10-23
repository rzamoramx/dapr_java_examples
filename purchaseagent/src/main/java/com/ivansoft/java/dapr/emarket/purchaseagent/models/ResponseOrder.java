package com.ivansoft.java.dapr.emarket.purchaseagent.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.ivansoft.java.dapr.emarket.common.models.Order;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ResponseOrder extends Response {
    Order data;

    public ResponseOrder(String status, String message, Order data) {
        super(status, message);
        this.data = data;
    }
}
