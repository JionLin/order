package com.example.orderdemo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Order service entry point with OpenFeign enabled.
 *
 * @author jiaolin
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.orderdemo.order.client")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
