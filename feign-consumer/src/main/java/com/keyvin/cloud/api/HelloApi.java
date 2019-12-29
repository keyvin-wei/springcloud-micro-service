package com.keyvin.cloud.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author weiwh
 * @date 2019/12/24 0:43
 */
@FeignClient("provider-server")
public interface HelloApi {

    @RequestMapping("/hello")
    String sayHello();
}
