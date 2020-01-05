package com.keyvin.cloud.controller;

import com.keyvin.cloud.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weiwh
 * @date 2019/12/29 18:31
 */
@RestController
public class HystrixFeignController {
    @Autowired
    private HelloService helloService;

    @GetMapping("/hi")
    public String hi() {
        System.out.println("hystrix feign hi!");
        return helloService.sayHello();
    }

}
