package com.keyvin.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author weiwh
 * @date 2019/12/22 22:41
 */
@SpringBootApplication(scanBasePackages = "com.keyvin")
@EnableEurekaClient
public class ProviderBApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderBApplication.class, args);
    }
}
