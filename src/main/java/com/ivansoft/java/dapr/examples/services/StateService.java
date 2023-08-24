package com.ivansoft.java.dapr.examples.services;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.State;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StateService {
    private static final String STATE_STORE_NAME = "statestore";

    public Optional<String> retrieveState(String key) {
        try (DaprClient client = new DaprClientBuilder().build()) {
            State<String> state = client.getState(STATE_STORE_NAME, key, String.class).block();
            return Optional.ofNullable(state.getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void storeState(String key, String value) {
        try (DaprClient client = new DaprClientBuilder().build()) {
            client.saveState(STATE_STORE_NAME, key, value).block();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
