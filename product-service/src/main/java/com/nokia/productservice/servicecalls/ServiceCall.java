package com.nokia.productservice.servicecalls;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Interface to all service calls
 *
 * @author Soumik Dutta
 * @since 0.0.1
 */
public interface ServiceCall {

    RestTemplate getRestTemplate();

    DiscoveryClient getDiscoveryClient();

    <T> T execute(@Nullable Object requestObject, String path, Class<T> responseObject, String requestMethod) throws IOException;

    /**
     * create service url with serviceName and uri
     * This will lookup discovery server and get the
     * host and the port and create the service URL
     * for the caller
     *
     * @param serviceName
     * @param uri
     * @return service uri
     */
    default String createServiceUrl(String serviceName, String uri) {
        String serviceUrl;
        //lookup the discovery server and get the host and port
        ServiceInstance instanceInfo = getDiscoveryClient().getInstances(serviceName).get(0);
        serviceUrl = instanceInfo.getHost();
        serviceUrl = (serviceUrl.startsWith("http") ? serviceUrl : "http://" + serviceUrl) + ":" + instanceInfo.getPort() + uri;
        return serviceUrl;
    }


    /**
     * Get param values appended in the URL
     *
     * @param url
     * @param params
     * @return
     */
    default String appendParam(String url, String params) {
        String appendedUrl;
        appendedUrl = url + "?" + params;
        return appendedUrl;
    }


    /**
     * Create a @see{@link RestTemplate} will have a POST
     *
     * @param inputObject
     * @param uri
     * @param returnType
     * @param <T>
     * @return
     * @throws IOException
     */
    default <T> T executePostRequest(Object inputObject, String uri, Class<T> returnType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String string = getRestTemplate().postForObject(uri, inputObject, String.class);
        //if the response is not expected
        if (returnType == null)
            return null;
        //if the return type is String then don't convert
        if (returnType.getName() == "java.lang.String")
            return (T) string;
        //other wise convert to the return type object and send
        return objectMapper.readValue(string, returnType);
    }

    /**
     * Create a @see{@link RestTemplate} will have a PATCH
     *
     * @param inputObject
     * @param uri
     * @param returnType
     * @param <T>
     * @return
     * @throws IOException
     */
    default <T> T executePatchRequest(Object inputObject, String uri, Class<T> returnType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String string = getRestTemplate().patchForObject(uri, inputObject, String.class);
        //if the return type is String then don't convert
        if (returnType.getName() == "java.lang.String")
            return (T) string;
        //other wise convert to the return type object and send
        return objectMapper.readValue(string, returnType);
    }

    /**
     * Execute get request
     *
     * @param uri
     * @param returnType
     * @param <T>
     * @return
     * @throws IOException
     */
    default <T> T executeGetRequest(String uri, Class<T> returnType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String string = getRestTemplate().getForObject(uri, String.class, "test=true");
        //if the response is not expected
        if (returnType == null)
            return null;
        //if the return type is String then don't convert
        if (returnType.getName() == "java.lang.String")
            return (T) string;

        //other wise convert to the return type object and send
        return objectMapper.readValue(string, returnType);
    }

    default <T> T exchange(String uri, HttpMethod method, HttpEntity<String> entity, Class<T> returnType) {

        return null;
    }

    /**
     * Delete request
     *
     * @param uri
     */
    default <T> T executeDeleteRequest(String uri, Class<T> returnType) {
        try {
            getRestTemplate().delete(new URI(uri));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return (T) "Successfully Deleted";

    }

}