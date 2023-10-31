package com.ivansoft.java.dapr.emarket.purchaseagent.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class ResponseCreation extends Response {
    private String orderId;

    public ResponseCreation(String status, String message, String id) {
        super(status, message);
        this.orderId = id;
    }
}
