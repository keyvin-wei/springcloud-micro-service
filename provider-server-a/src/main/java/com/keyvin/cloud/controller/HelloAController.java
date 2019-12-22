package com.keyvin.cloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weiwh
 * @date 2019/12/22 22:42
 */
@RestController
public class HelloAController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/hello")
    public String hello() {
        return "hello world, port:" + port;
    }
}
