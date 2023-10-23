package com.ivansoft.java.dapr.emarket.purchaseagent.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    String status;
    String message;
}
