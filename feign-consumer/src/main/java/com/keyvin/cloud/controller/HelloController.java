package com.keyvin.cloud.controller;

import com.keyvin.cloud.api.HelloApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weiwh
 * @date 2019/12/22 22:42
 */
@RestController
public class HelloController {
    @Autowired
    private HelloApi helloApi;

    @GetMapping("/hello")
    public String hello() {
        System.out.println("feign hello!");
        return "HelloController feign hello!";
    }

    @GetMapping("/hi")
    public String hi() {
        System.out.println("feign hi!");
        return helloApi.sayHello();
    }
}
