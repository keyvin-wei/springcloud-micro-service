package com.keyvin.cloud.hystrix;

import com.keyvin.cloud.service.HelloService;
import org.springframework.stereotype.Component;

/**
 * @author weiwh
 * @date 2019/12/29 18:36
 */
@Component
public class HelloFallback implements HelloService {

    @Override
    public String sayHello() {
        System.out.println("获取hello失败！");
        return "获取hello失败！hystrix feign:服务提供者挂了";
    }
}
