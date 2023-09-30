package com.ivansoft.java.dapr.emarket.purchaseagent.controllers;

import com.ivansoft.java.dapr.emarket.common.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ivansoft.java.dapr.emarket.purchaseagent.models.Response;
import com.ivansoft.java.dapr.emarket.purchaseagent.services.OrderService;

import java.io.IOException;

@RestController
@RequestMapping("/orders")
public class OrdersController {
    private static final Logger log = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    public Response createOrder(@RequestBody Order order) throws IOException {
        log.info("Order received: {}", order);
        orderService.publishOrder(order);
        return new Response("OK", "Order received");
    }
}
