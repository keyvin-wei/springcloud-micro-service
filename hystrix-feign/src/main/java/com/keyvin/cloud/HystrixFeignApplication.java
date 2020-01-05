package com.keyvin.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

/**
 * @author weiwh
 * @date 2019/12/29 17:39
 */
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication(scanBasePackages = "com.keyvin")
public class HystrixFeignApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixFeignApplication.class, args);
    }

}
