package com.nokia.productservice.schemas;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;


/**
 * The details of the product
 */
@Data
@Document
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String manufacturer;
    private Double mrp;
    private Integer quantity;
}
