package com.nokia.orderservice.repository;


import com.nokia.orderservice.schemas.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
