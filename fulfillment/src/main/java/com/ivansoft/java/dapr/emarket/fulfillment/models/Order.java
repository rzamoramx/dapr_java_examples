package com.ivansoft.java.dapr.emarket.fulfillment.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class Order {
    @NonNull
    String client;

    @NonNull
    String product;

    int quantity;

    String status;

    double price;
}
