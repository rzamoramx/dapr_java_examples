package com.ivansoft.java.dapr.emarket.orderstateservice.services;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.State;
import java.util.Optional;

public class StateService {
    private static final String STATE_STORE_NAME = "statestore";

    // private property for singleton usage
    private static StateService instance;

    // private constructor for singleton usage
    private StateService() {
    }

    // public method for singleton usage
    public static StateService getInstance() {
        if (instance == null) {
            instance = new StateService();
        }
        return instance;
    }

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
