package com.ivansoft.java.dapr.examples.stateservice.models;

import lombok.Data;

@Data
public class State {
    String key;
    String value;
}
