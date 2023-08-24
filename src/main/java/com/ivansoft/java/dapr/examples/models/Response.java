package com.ivansoft.java.dapr.examples.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    String status;
    String message;
}
