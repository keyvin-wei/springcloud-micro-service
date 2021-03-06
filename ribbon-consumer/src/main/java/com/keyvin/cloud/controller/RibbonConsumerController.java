package com.keyvin.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author weiwh
 * @date 2019/12/22 22:42
 */
@RestController
public class RibbonConsumerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        System.out.println("ribbon hello!");
        return restTemplate.getForObject("http://provider-server/hello", String.class);
    }
}
