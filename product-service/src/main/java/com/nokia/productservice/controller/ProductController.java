package com.nokia.productservice.controller;

import com.nokia.productservice.schemas.Product;
import com.nokia.productservice.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /*@RequestMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Product> getAllProductsByIds(List<String> stringList) {
        return productService.getAllProduct(stringList);
    }*/

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Product> getAllProducts() {
        return productService.getAllProduct();
    }

    @RequestMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Product getProduct(@PathVariable("id") String id) {
        return productService.getProduct(id);
    }


    @RequestMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Product updateProduct(@PathVariable("id") String id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @RequestMapping(
            value = "/{id}",
            consumes = APPLICATION_FORM_URLENCODED_VALUE,
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Product updateProduct(@RequestParam MultiValueMap<String, String> formParams, @PathVariable("id") String id) {
        return productService.updateProduct(id, formParams);
    }

    /**
     * Update all the product with the quantity that is already ordered
     *
     * @param formParams
     * @return
     */
    @RequestMapping(
            method = RequestMethod.PATCH,
            consumes = APPLICATION_FORM_URLENCODED_VALUE
    )
    public String updateInventory(@RequestParam MultiValueMap<String, String> formParams) {
        return productService.updateInventory(formParams);
    }


    @RequestMapping(
            value = "/{id}",
            method = RequestMethod.DELETE
    )
    public void deleteProduct(@PathVariable("id") String id) {
        productService.deleteProduct(id);
    }

}
