package com.example.orderdemo.order.client;

import com.example.orderdemo.order.exception.InventoryRemoteException;
import com.example.orderdemo.order.exception.InsufficientStockException;
import com.example.orderdemo.order.exception.SkuNotFoundException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps inventory-service HTTP errors into domain exceptions.
 *
 * @author jiaolin
 */
public class InventoryFeignErrorDecoder implements ErrorDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryFeignErrorDecoder.class);
    private final ErrorDecoder defaultDecoder = new Default();

    /**
     * Decode Feign error response.
     *
     * @param methodKey Feign method key
     * @param response HTTP response
     * @return mapped exception
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        String body = readBody(response);
        int status = response.status();
        if (status == 404) {
            return new SkuNotFoundException(body);
        }
        if (status == 409) {
            return new InsufficientStockException(body);
        }
        LOGGER.warn("Inventory Feign call failed methodKey={}, status={}, body={}", methodKey, status, body);
        return new InventoryRemoteException("Inventory call failed with status " + status + ": " + body);
    }

    private String readBody(Response response) {
        if (response.body() == null) {
            return "";
        }
        try (InputStream inputStream = response.body().asInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            return defaultDecoder.decode("body-read", response).getMessage();
        }
    }
}
