package com.nokia.orderservice.controller;

import com.nokia.orderservice.schemas.Order;
import com.nokia.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Order> getAllOrders() {
        return orderService.getAllOrder();
    }

    @RequestMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Order addOrder(@RequestBody Order product) {
        return orderService.addOrder(product);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Order getOrder(@PathVariable("id") String id) {
        return orderService.getOrder(id);
    }


    @RequestMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Order updateOrder(@PathVariable("id") Integer id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }


}
