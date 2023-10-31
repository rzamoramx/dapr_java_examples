package com.ivansoft.java.dapr.emarket.purchaseagent.controllers;

import com.ivansoft.java.dapr.emarket.common.models.Order;
import com.ivansoft.java.dapr.emarket.purchaseagent.models.ResponseCreation;
import com.ivansoft.java.dapr.emarket.purchaseagent.models.ResponseOrder;
import com.ivansoft.java.dapr.emarket.common.services.OrderStateService;
import org.springframework.web.bind.annotation.*;
import com.ivansoft.java.dapr.emarket.purchaseagent.models.Response;
import com.ivansoft.java.dapr.emarket.purchaseagent.services.FulfillmentService;
import java.util.Optional;
import java.util.logging.Logger;
import java.io.IOException;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger log = Logger.getLogger(OrdersController.class.getName());

    //@Autowired
    private final FulfillmentService fulfillmentService;

    private final OrderStateService orderStateService;

    // inject dependencies by constructor is considered a good practice due the following reasons:
    // 1. Immutable dependencies. The class is immutable, so it is thread-safe.
    // 2. Compile-time safety. The class is guaranteed to be initialized with all its dependencies.
    // 3. Testability. The class can be easily tested with mock dependencies.
    public OrdersController(FulfillmentService fulfillmentService, OrderStateService orderStateService) {
        this.fulfillmentService = fulfillmentService;
        this.orderStateService = orderStateService;
    }

    @PostMapping("/")
    public Response createOrder(@RequestBody Order order) throws IOException {
        log.info("Order received: " + order);

        // assign an id to the order using uuid v4
        order.setId(java.util.UUID.randomUUID().toString());

        fulfillmentService.publishOrder(order);
        return new ResponseCreation("OK", "Order received", order.getId());
    }

    @GetMapping("/{id}")
    public ResponseOrder getOrder(@PathVariable String id) throws Exception {
        log.info("getOrder: " + id);
        Optional<Order> order = orderStateService.getState(id);
        return order.map(value ->
                new ResponseOrder("ok", "retrieved data", value)).orElseGet(() ->
                new ResponseOrder("error", "order not found", null));
    }
}
