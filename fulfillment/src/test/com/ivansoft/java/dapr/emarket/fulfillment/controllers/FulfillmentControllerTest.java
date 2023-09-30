package com.ivansoft.java.dapr.emarket.fulfillment.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivansoft.java.dapr.emarket.common.Utils;
import com.ivansoft.java.dapr.emarket.fulfillment.services.OrderStateService;
import io.dapr.client.domain.CloudEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.ivansoft.java.dapr.emarket.common.models.Order;


@SpringBootTest
@AutoConfigureMockMvc
class FulfillmentControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderStateService orderStateService;

    @Test
    public void testCreateOrderV2() throws Exception {
        // Create a sample CloudEvent
        CloudEvent cloudEvent = new CloudEvent();
        cloudEvent.setType("v2");
        cloudEvent.setData(Utils.serializeOrder(new Order(
                "test", "product", 1, "pending", 10.0
        )));

        // Mock the behavior of orderStateService.saveOrder
        //when(orderStateService.saveOrder(any(Order.class))).thenReturn(Mono.empty());

        // add cloud event object to request body
        String json = OBJECT_MAPPER.writeValueAsString(cloudEvent);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/createorder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk());
                //.andExpect(content().json(expectedJson));
    }
}