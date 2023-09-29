package com.ivansoft.java.dapr.emarket.stateservice.controllers;

import com.ivansoft.java.dapr.emarket.stateservice.services.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import com.ivansoft.java.dapr.emarket.stateservice.models.State;
import com.ivansoft.java.dapr.emarket.stateservice.models.Response;


@RestController
@RequestMapping("/stateful_service")
public class StateController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(StateController.class);

    @Autowired
    private StateService stateService;

    @GetMapping("/{key}")
    @ResponseBody
    public Response getData(@PathVariable String key) {
        logger.info("Getting data for key: " + key);
        var result = stateService.retrieveState(key).orElse("No data found for key: " + key);
        logger.info("Returning data: " + result);
        return new Response("success", result);
    }

    @PostMapping("/")
    public Response saveData(@RequestBody State body) {
        logger.info("Saving data for key: " + body.getKey());
        stateService.storeState(body.getKey(), body.getValue());
        logger.info("Data saved for key: " + body.getKey());
        return new Response("success", "Data saved for key: " + body.getKey());
    }
}
