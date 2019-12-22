## springcloud-micro-service
Springcloud Dalston.SR4学习


微服务设计原则：职责单一原则 Single Responsibility Principle  

----
#### Eureka
Eureka Server提供服务注册和发现，实现服务治理   

模块 | 端口 | 说明
--- | ---- | ---- 
eureka-server | 8761 | 注册中心
eureka-provider | 8762 | 服务提供
eureka-provider-a | 8763 | 服务提供a
eureka-provider-b | 8764 | 服务提供b
eureka-provider-c | 8765 | 服务提供c
ribbon-consumer | 8766 | ribbon

#### CAP原则又称CAP定理
指的是在一个分布式系统中， Consistency（一致性）、 Availability（可用性）、Partition tolerance（分区容错性），三者不可得兼。  
CAP原则是NOSQL数据库的基石。  
分布式系统的CAP理论：  
理论首先把分布式系统中的三个特性进行了如下归纳：  
一致性（C）：在分布式系统中的所有数据备份，在同一时刻是否同样的值。（等同于所有节点访问同一份最新的数据副本）  
可用性（A）：在集群中一部分节点故障后，集群整体是否还能响应客户端的读写请求。（对数据更新具备高可用性）  
分区容忍性（P）：以实际效果而言，分区相当于对通信的时限要求。系统如果不能在时限内达成数据一致性，就意味着发生了分区的情况，必须就当前操作在C和A之间做出选择。

#### consul
实现分布式系统的服务发现与配置  
不再需要依赖其他工具（比如ZooKeeper等），与Docker等轻量级容器可无缝配合  
使用 Raft 算法来保证一致性, 比复杂的 Paxos 算法更直接. 相较而言, zookeeper 采用的是 Paxos, 而 etcd 使用的则是 Raft.  

#### ribbon
主要功能是提供客户端的软件负载均衡算法，将Netflix的中间层服务连接在一起，即在配置文件中列出Load Balancer（简称LB）后面所有的机器，Ribbon会自动的帮助你基于某种规则（如简单轮询，随即连接等）去连接这些机器  
+ 核心组件
  - ServerList 用于获取地址列表
  - ServerListFilter 用于在原始的服务列表中使用一定策略过虑掉一部分地址
  - IRule 选择一个最终的服务地址作为LB结果。选择策略有轮询、根据响应时间加权、断路器(当Hystrix可用时)等  
+ 主要的负载均衡策略
  - Random 随机负载均衡：随机选择状态为UP的Server
  - WeightedResponseTime 加权响应时间负载均衡：根据相应时间分配一个weight，相应时间越长，weight越小，被选中的可能性越低
  - ZoneAvoidanceRule 区域感知轮询负载均衡：复合判断server所在区域的性能和server的可用性选择server


