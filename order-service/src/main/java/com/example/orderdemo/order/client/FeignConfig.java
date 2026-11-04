package com.example.orderdemo.order.client;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign error mapping configuration.
 *
 * @author jiaolin
 */
@Configuration
public class FeignConfig {

    /**
     * Register custom Feign error decoder.
     *
     * @return error decoder bean
     */
    @Bean
    public ErrorDecoder inventoryErrorDecoder() {
        return new InventoryFeignErrorDecoder();
    }
}
