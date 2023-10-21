package com.ivansoft.java.dapr.emarket.purchaseagent.controllers;

import com.ivansoft.java.dapr.emarket.common.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ivansoft.java.dapr.emarket.purchaseagent.models.Response;
import com.ivansoft.java.dapr.emarket.purchaseagent.services.FulfillmentService;
import java.util.logging.Logger;
import java.io.IOException;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger log = Logger.getLogger(OrdersController.class.getName());

    @Autowired
    private FulfillmentService fulfillmentService;

    @PostMapping("/")
    public Response createOrder(@RequestBody Order order) throws IOException {
        log.info("Order received: " + order);
        fulfillmentService.publishOrder(order);
        return new Response("OK", "Order received");
    }
}
