package com.nokia.orderservice.servicecalls;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Data
@Service
@Slf4j
public class ProductServiceCall implements ServiceCall {
    private static final String PRODUCT_SERVICE = "product-service";

    private RestTemplate restTemplate;
    private DiscoveryClient discoveryClient;

    @Autowired
    public ProductServiceCall(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @Override
    public <T> T execute(Object requestObject, String path, Class<T> responseObject, RequestMethod method) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Product service call ");
        }
        String uri = createServiceUrl(PRODUCT_SERVICE, path);
        switch (method) {
            case PATCH:
                return executePatchRequest(requestObject, uri, responseObject);
        }
        return null;
    }
}
