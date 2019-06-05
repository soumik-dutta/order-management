package com.nokia.orderservice.service;

import com.nokia.orderservice.repository.OrderRepository;
import com.nokia.orderservice.schemas.Order;
import com.nokia.orderservice.servicecalls.ProductServiceCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderService {
    private OrderRepository orderRepository;
    private MessageService messageService;
    private ProductServiceCall serviceCall;

    @Autowired
    public OrderService(OrderRepository orderRepository, MessageService messageService, ProductServiceCall serviceCall) {
        this.orderRepository = orderRepository;
        this.messageService = messageService;
        this.serviceCall = serviceCall;
    }

    /**
     * Find all the Orders placed so far
     *
     * @return
     */
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    /**
     * Order placed . Add the order in database and
     * send a message in kafka
     *
     * @param order
     * @return
     */
    public Order addOrder(Order order) {
        //set date
        order.setDate(new Date());
        Order savedOrder = orderRepository.save(order);
        Map<String, String> map = savedOrder.getOrderItems().stream()
                .collect(Collectors.toMap(orderItems -> orderItems.getProductId(), orderItems -> orderItems.getQuantity().toString()));
        try {
            MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
            for (Map.Entry entry : map.entrySet()) {
                multiValueMap.add(entry.getKey().toString(), entry.getValue().toString());
            }
            //update the inventory using api call
            String result = serviceCall.execute(multiValueMap, "/products", String.class, RequestMethod.PATCH);
            log.info("Inventory updated {}", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //send for shipping
        messageService.send(savedOrder);
        return savedOrder;
    }

    /**
     * Get order Invoice
     *
     * @param id
     * @return
     */
    public Order getOrder(String id) {
        if (log.isDebugEnabled()) {
            log.debug("Invoice for order {}", id);
        }
        //get the order details for the order id
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElse(null);
    }

    public Order updateOrder(Integer id, Order product) {
        return null;
    }
}
