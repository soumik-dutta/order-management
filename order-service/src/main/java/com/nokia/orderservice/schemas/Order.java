package com.nokia.orderservice.schemas;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;


import java.util.Date;
import java.util.List;


@Data
@Document
public class Order {
    @Id
    private String id;
    private List<OrderItems> orderItems;
    private Date date;
    private String purchaserName;
    private String deliveryAddress;
    private String phoneNumber;
    private Double totalCost;

    @NoArgsConstructor
    @Data
    public static class OrderItems {
        public OrderItems(String productId, Integer quantity, Double productPrice, Double totalPrice) {
            this.productId = productId;
            this.quantity = quantity;
            this.productPrice = productPrice;
            this.totalPrice = totalPrice;
        }

        @Id
        private String id;
        private String productId;
        private Integer quantity;
        private Double productPrice;
        private Double totalPrice;
    }
}
