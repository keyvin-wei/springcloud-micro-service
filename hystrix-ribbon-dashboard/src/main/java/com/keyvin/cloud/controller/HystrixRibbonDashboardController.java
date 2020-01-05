package com.keyvin.cloud.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author weiwh
 * @date 2020/1/5 23:47 
 */
@RestController
public class HystrixRibbonDashboardController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    @HystrixCommand(fallbackMethod = "hystrixRibbonStores")
    public String hello() {
        System.out.println("hystrix ribbon hello!");
        return restTemplate.getForObject("http://provider-server/hello", String.class);
    }

    public String hystrixRibbonStores(){
        String str = "hystrix ribbon dashboard:服务提供者挂了";
        return str;
    }

}
