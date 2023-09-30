package com.ivansoft.java.dapr.emarket.common.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@ToString
public class Order implements Serializable {
    @NonNull
    String client;

    @NonNull
    String product;

    int quantity;

    String status;

    double price;
}
