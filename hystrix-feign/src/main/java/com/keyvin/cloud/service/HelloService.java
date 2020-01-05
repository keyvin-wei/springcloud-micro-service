package com.keyvin.cloud.service;

import com.keyvin.cloud.hystrix.HelloFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author weiwh
 * @date 2020/1/5 23:34
 */
@Component
@FeignClient(value = "provider-server", fallback= HelloFallback.class)
public interface HelloService {

    @RequestMapping("/hello")
    String sayHello();
}
