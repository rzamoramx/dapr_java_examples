package com.ivansoft.java.dapr.examples.stateservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Response {
    String status;
    String message;
}
