package com.nokia.productservice.service;

import com.nokia.productservice.repository.ProductRepository;
import com.nokia.productservice.schemas.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Get the product by ids
     *
     * @param id
     * @return
     */
    public Product getProduct(String id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }


    /**
     * Get all the Product that are there
     *
     * @return List<Product>
     */
    public List<Product> getAllProduct(List<String> stringList) {
        return productRepository.findByIdIn(stringList);
    }

    /**
     * Get all product
     *
     * @return List<Product>
     */
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    /**
     * Save the product
     *
     * @param product
     * @return
     */
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }


    /**
     * Update the product details
     *
     * @param product
     * @param id
     * @return
     */
    public Product updateProduct(String id, Product product) {
        Optional<Product> savedProductOptional = productRepository.findById(id);
        Product savedProduct = null;
        if (savedProductOptional.isPresent()) {
            savedProduct = savedProductOptional.get();
            savedProduct.setDescription(product.getDescription());
            savedProduct.setManufacturer(product.getManufacturer());
            savedProduct.setMrp(product.getMrp());
            savedProduct.setQuantity(product.getQuantity());
        }
        productRepository.save(savedProduct);
        return savedProduct;
    }

    /**
     * Update product details as a part of patch request
     *
     * @param id
     * @param formParams
     * @return
     */
    public Product updateProduct(String id, MultiValueMap<String, String> formParams) {
        Optional<Product> savedProductOptional = productRepository.findById(id);
        Product savedProduct = savedProductOptional.get();
        Assert.notNull(savedProduct, " Product id not found : " + id);
        // save only the values that are changed as the part of the update
        if (formParams.getFirst("name") != null)
            savedProduct.setName(formParams.getFirst("name"));
        if (formParams.getFirst("description") != null)
            savedProduct.setDescription(formParams.getFirst("description"));
        if (formParams.getFirst("mrp") != null)
            savedProduct.setMrp(Double.parseDouble(formParams.getFirst("mrp")));
        if (formParams.getFirst("manufacturer") != null)
            savedProduct.setManufacturer(formParams.getFirst("manufacturer"));
        if (formParams.getFirst("quantity") != null)
            savedProduct.setQuantity(Integer.parseInt(formParams.getFirst("quantity")));
        return productRepository.save(savedProduct);
    }


    /**
     * Updates the inventory with the quantity already purchased
     *
     * @param param productid -> quantity map
     * @return
     */
    public String updateInventory(MultiValueMap<String, String> param) {
        Assert.notEmpty(param, "The Product-quantity map should not be empty");
        List<String> productId = param.toSingleValueMap().keySet().stream()
                .collect(Collectors.toList());
        //get all the products with this ids
        List<Product> productList = productRepository.findByIdIn(productId).stream()
                .map(product -> {
                    int quantity = Integer.parseInt(param.getFirst(product.getId()));
                    //update the product list
                    product.setQuantity(product.getQuantity() - quantity);
                    return product;
                })
                .collect(Collectors.toList());
        List<Product> updatedProductId = productRepository.saveAll(productList);
        // if all the ids that are send are updated
        if (productId.size() == updatedProductId.size()) {
            return "UPDATED";
        }
        return "NOT UPDATED";
    }

    /**
     * Delete product by id
     *
     * @param id
     */
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }


}
