## springcloud-micro-service
Springcloud Dalston.SR4学习


微服务设计原则：职责单一原则 Single Responsibility Principle  

----
### Eureka
Eureka Server提供服务注册和发现，实现服务治理   

模块 | 端口 | 说明 | 启动顺序
--- | ---- | ---- | ---- 
eureka-server | 8761 | 注册中心 | 1
eureka-provider | 8762 | 服务提供 | 2
eureka-provider-a | 8763 | 服务提供a | 2
eureka-provider-b | 8764 | 服务提供b | 2
eureka-provider-c | 8765 | 服务提供c | 2
ribbon-consumer | 8766 | ribbon负载均衡 | 3
feign-consumer | 8767 | feign可插拔调用服务 | 3
hystrix-ribbon | 8768 | hystrix+ribbon实现断路器 | 3
hystrix-feign | 8769 | hystrix+feign实现断路器 | 3
hystrix-ribbon-dashboard | 8770 | hystrix+feign+dashboard查看负载均衡状态 | 3

### CAP原则又称CAP定理
指的是在一个分布式系统中， Consistency（一致性）、 Availability（可用性）、Partition tolerance（分区容错性），三者不可得兼。  
CAP原则是nosql数据库的基石。  
分布式系统的CAP理论：  
理论首先把分布式系统中的三个特性进行了如下归纳：  
一致性（C）：在分布式系统中的所有数据备份，在同一时刻是否同样的值。（等同于所有节点访问同一份最新的数据副本）  
可用性（A）：在集群中一部分节点故障后，集群整体是否还能响应客户端的读写请求。（对数据更新具备高可用性）  
分区容忍性（P）：以实际效果而言，分区相当于对通信的时限要求。系统如果不能在时限内达成数据一致性，就意味着发生了分区的情况，必须就当前操作在C和A之间做出选择。

### Consul
实现分布式系统的服务发现与配置  
不再需要依赖其他工具（比如ZooKeeper等），与Docker等轻量级容器可无缝配合  
使用 Raft 算法来保证一致性, 比复杂的 Paxos 算法更直接. 相较而言, zookeeper 采用的是 Paxos, 而 etcd 使用的则是 Raft.  
需安装consul后配置，本项目未实现  

### Ribbon
主要功能是提供客户端的软件负载均衡算法，将Netflix的中间层服务连接在一起，即在配置文件中列出Load Balancer（简称LB）后面所有的机器，Ribbon会自动的帮助你基于某种规则（如简单轮询，随即连接等）去连接这些机器  
+ 核心组件
  - ServerList 用于获取地址列表
  - ServerListFilter 用于在原始的服务列表中使用一定策略过虑掉一部分地址
  - IRule 选择一个最终的服务地址作为LB结果。选择策略有轮询、根据响应时间加权、断路器(当Hystrix可用时)等  
+ 主要的负载均衡策略
  - Random 随机负载均衡：随机选择状态为UP的Server
  - WeightedResponseTime 加权响应时间负载均衡：根据相应时间分配一个weight，相应时间越长，weight越小，被选中的可能性越低
  - ZoneAvoidanceRule 区域感知轮询负载均衡：复合判断server所在区域的性能和server的可用性选择server
+ 开启ribbon负载均衡
  - @EnableDiscoveryClient表明向服务注册中心注册，并向ioc注入一个bean（restTemplate）
  - @LoadBalanced注解表明这个restRemplate开启负载均衡
  - 消费端调用http://provider-server/hello，provider-server为服务提供者在yml配置的应用名，hello为调用方法名
```$xslt
    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    public String hello() {
        return restTemplate.getForObject("http://provider-server/hello", String.class);
    }
```
本项目中ribbon启动顺序：eureka-server，provider-server-a、b、c，ribbon-consumer  
启动后访问：[http://127.0.0.1:8766/hello](http://127.0.0.1:8766/hello)

### Feign
Feign是一个声明式的伪Http客户端，用注解方式实现可插拔的特性，目的是让Web Service调用更加简单  
Feign默认集成了Ribbon和Hystrix，并和Eureka结合，默认实现了负载均衡的效果  
依赖时要依赖starter-feign和starter-eureka，要不然会报找不到provider-server异常  
接口定义：  
```aidl
    @FeignClient("provider-server")             //使用步骤1：加注解
    public interface HelloApi {
        @RequestMapping("/hello")
        String sayHello();
    }
    
    public class HelloController {
        @Autowired
        private HelloApi helloApi;              //使用步骤2：controller注入
        @GetMapping("/hi")
        public String hi() {
            System.out.println("feign hi!");
            return helloApi.sayHello();         //使用步骤3：调用
        }
    }
```
本项目中feign启动顺序：eureka-server，provider-server-a、b、c，feign-consumer  
启动后访问：[http://127.0.0.1:8767/hi](http://127.0.0.1:8767/hi)

### Hystrix
服务雪崩效应：服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的瘫痪后果  
断路器Hystrix具备了服务降级、服务熔断、线程隔离、请求缓存、请求合并以及服务监控等强大功能，断路器相当于电路中的保险丝作用  
举例：微服务有server-a、b、c、d、e、f，当对B的调用失败达到一个特定的阀值(5秒之内发生20次失败是Hystrix定义的缺省值), 链路就会被处于open状态， 之后所有所有对服务B的调用都不会被执行， 取而代之的是由断路器提供的一个表示链路open的Fallback消息，开发者可定义这个Fallbak消息，这样就阻止了瀑布式的雪崩    
**实战一：hystrix+ribbon**    
依赖starter-eureka和starter-hystrix，启动类添加注解@EnableHystrix、@EnableDiscoveryClient，控制类添加注解@HystrixCommand(fallbackMethod="method")
```aidl
    public class HystrixRibbonController {
        @Autowired
        private RestTemplate restTemplate;
        
        @GetMapping("/hello")
        @HystrixCommand(fallbackMethod = "hystrixRibbonStores")
        public String hello() {
            return restTemplate.getForObject("http://provider-server/hello", String.class);
        }
        public String hystrixRibbonStores(){
            return "hystrix ribbon:服务提供者挂了";
        }
    }
```
+ @HystrixCommand 表明该方法为hystrix包裹，可以对依赖服务进行隔离、降级、快速失败、快速重试等，注解属性如下：
  - fallbackMethod 降级方法
  - commandProperties 普通配置属性，可配置HystrixCommand对应属性，如采用线程池还是信号量隔离、熔断器熔断规则等
  - ignoreExceptions 忽略的异常，默认HystrixBadRequestException不计入失败
  - groupKey() 组名称，默认使用类名称
  - commandKey 命令名称，默认使用方法名
本项目中hystrix启动顺序：eureka-server，provider-server-a、b、c，hystrix-ribbon  
启动后访问：[http://127.0.0.1:8768/hello](http://127.0.0.1:8768/hello)  
刷新正常，关掉A服务，刷新，断路器生效，提示“hystrix ribbon:服务提供者挂了”  
**实战二：hystrix+feign**  
Feign是自带断路器的，所以pom不用重复引入hystrix，dalston版本中没有默认打开需要在配置文件application.yml中打开  
```aidl
    feign:
      hystrix:
        enabled: true
```
@FeignClient 注解，加上fallback= HelloFallback.class，自定义实现重写调不通时返回异常数据  
本项目中hystrix启动顺序：eureka-server，provider-server-a、b、c，hystrix-feign  
启动后访问：[http://127.0.0.1:8769/hi](http://127.0.0.1:8769/hi)  
刷新正常，关掉A服务，刷新，断路器生效，提示“hystrix feign:服务提供者挂了”  
**实战三：hystrix+ribbon+dashboard**  
Hystrix Dashboard是作为断路器状态的一个组件，提供了数据监控和友好的图形化界面，查看负载均衡情况  
添加spring-boot-starter-actuator、spring-cloud-starter-hystrix-dashboard依赖，启动类加上@EnableHystrixDashboard  
本项目中dashboard启动顺序：eureka-server，provider-server-a、b、c，hystrix-ribbon-dashboard  
启动后访问：[http://127.0.0.1:8770/hystrix](http://127.0.0.1:8770/hystrix)  
输入框输入http://127.0.0.1:8770/hystrix.stream，delay=2000，title=hello，点monitor按钮，进入后会loading等待负载均衡   
然后[http://127.0.0.1:8770/hello](http://127.0.0.1:8770/hello)请求负载均衡，不断刷新，dashboard会显示相关信息    