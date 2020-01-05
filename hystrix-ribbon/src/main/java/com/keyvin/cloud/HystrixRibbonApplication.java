package com.keyvin.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author weiwh
 * @date 2019/12/29 17:39
 */
@EnableHystrix
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "com.keyvin")
public class HystrixRibbonApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixRibbonApplication.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
