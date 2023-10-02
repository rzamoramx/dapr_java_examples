package com.ivansoft.java.dapr.emarket.common.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = 123456789L;

    @NonNull
    @JsonProperty("client")
    String client;

    @NonNull
    @JsonProperty("product")
    String product;

    @JsonProperty("quantity")
    int quantity;

    @JsonProperty("status")
    String status;

    @JsonProperty("price")
    double price;
}
