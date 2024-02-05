# Spring Cloud Ribbon

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下服务当中：
>
> * [06-spring-cloud-ribbon-order-6000](https://github.com/wicksonZhang/Spring-Cloud/tree/main/06-spring-cloud-ribbon-order-6000)

## 基础概念

### Ribbon 是什么

* 官方地址：https://github.com/Netflix/ribbon

* `Ribbon` 是 `NetFlix` 提供的一个基于 HTTP 和 TCP 客户端的负载均衡器。主要解决了微服务架构之间服务与服务之间的通信进行负载均衡。



### Ribbon 优缺点

**优点**

1. **集成性：** Ribbon 集成简单。Ribbon可以轻松的集成 Java 微服务架构中，与 Spring Cloud、 `Eureka` 和 `Consul` 中使用。
2. **负载均衡策略：** Ribbon 提供了多种负载均衡算法。例如轮询、随机、加权等等。



**缺点**

1. **不适用于非 Java 生态系统：** Ribbon 主要针对 Java 平台，对于其他语言或平台的应用集成可能不那么方便。
2. **依赖性：** 在 Netflix 宣布不再主动维护后，Ribbon 的更新和维护可能受到限制，可能会存在安全或功能方面的风险。



### Ribbon 解决了什么问题

* **服务与服务调用的负载均衡**：例如，一个电子商务网站可能有多个商品服务的实例，通过 Ribbon 进行负载均衡，确保用户请求能够均匀地分布到各个商品服务实例上，提高系统的可用性和性能。



### Nginx 和 Ribbon 有什么区别

* Nginx：客户端所有请求统一交给 Nginx，由 Nginx 进行实现负载均衡请求转发，属于服务器端负载均衡。

* Ribbon：是从 eureka 注册中心服务器端上获取服务注册信息列表，缓存到本地，然后在本地实现轮询负载均衡策略。

* 如下是 `Nginx` 在微服务架构中的体现

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401041636186.png" alt="image-20240104163643137" style="zoom:80%;float:left" />

* 如下是 `Nginx` 的配置，并不是配置了所有的服务代码，而是只配置一个网关即可。

  ```nginx
  worker_processes  1;
  
  events {
      worker_connections  1024;
  }
  
  http {
      include       mime.types;
      default_type  application/octet-stream;
      sendfile        on;
      keepalive_timeout  65;
  
      server {
          listen       80;
          server_name  localhost;
  
          location / {
              root   /home/ruoyi/projects/ruoyi-ui;
              try_files $uri $uri/ /index.html;
              index  index.html index.htm;
          }
  
          location /prod-api/{
              proxy_set_header Host $http_host;
              proxy_set_header X-Real-IP $remote_addr;
              proxy_set_header REMOTE-HOST $remote_addr;
              proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
              # 只暴露网关端口
              proxy_pass http://localhost:8080/;
          }
  
          # 避免actuator暴露
          if ($request_uri ~ "/actuator") {
              return 403;
          }
  
          error_page   500 502 503 504  /50x.html;
          location = /50x.html {
              root   html;
          }
      }
  }
  ```

  

## Ribbon 具体实现

* 实现需求

  * 我们本章节的 `Ribbion` 实现，还是基于我们 `Eureka` 的集群案例，只是不需要订单服务。采用 `Ribbon` 的服务。

* 实现思路

  * Step-1：创建订单服务 `06-spring-cloud-ribbon-order-6000`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401041753693.png" alt="image-20240104175322656" style="zoom:100%;float:left" />



* 创建订单服务：`06-spring-cloud-ribbon-order-6000`

* 实现步骤

  1. Step-1：导入 `pom.xml` 依赖
  2. Step-2：修改 `application.properties` 文件
  3. Step-3：创建主启动类
  4. Step-4：编写业务类

* Step-1：导入 `pom.xml` 依赖

  * 在 `eureka-client` 依赖包中存在在 `Ribbon` 的相关依赖

  ```xml
      <dependencies>
          <!-- 公共依赖包 -->
          <dependency>
              <groupId>cn.wickson.cloud</groupId>
              <artifactId>01-spring-cloud-common</artifactId>
              <version>1.0-SNAPSHOT</version>
          </dependency>
  
          <!-- 服务注册中心的客户端端 eureka-client -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
      </dependencies>
  ```

  

* Step-2：修改 `application.properties` 文件

  ```properties
  # 服务端口
  server.port=6000
  # 应用名称
  spring.application.name=spring-cloud-ribbon-order
  # 是否向注册中心注册自己
  eureka.client.register-with-eureka=true
  # 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
  eureka.client.fetch-registry=true
  # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
  eureka.client.serviceUrl.defaultZone=http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
  
  # 默认就是应用名称:端口，设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-order:3500
  eureka.instance.instance-id=spring-cloud-ribbon-order:6000
  # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
  eureka.instance.prefer-ip-address=true
  ```

  

* Step-3：创建主启动类

  ```java
  /**
   * 订单服务-微服务启用类
   *
   * @author ZhangZiHeng
   * @date 2024-01-04
   */
  @EnableDiscoveryClient
  @SpringBootApplication
  public class SpringCloudRibbonOrderApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(SpringCloudRibbonOrderApplication.class, args);
      }
  
  }
  ```

  

* Step-4：编写业务类

  * restTemplate.getForObject：返回对象为响应体中数据转化成的对象，基本上可以理解为 Json
  * restTemplate.getForEntity：返回的对象为 ResponseEntity 对象，包含一些重要信息。

  ```java
  @Slf4j
  @Validated
  @RestController
  @RequestMapping("/order")
  public class OrderController {
  
      public static final String PAYMENT_URL = "http://SPRING-CLOUD-CLUSTER-EUREKA-PAYMENT";
  
      @Resource
      private RestTemplate restTemplate;
  
      /**
       * 获取订单
       * restTemplate.getForObject：返回对象为响应体中数据转化成的对象，基本上可以理解为 Json
       *
       * @param id id
       * @return ResultUtil
       */
      @GetMapping("/getPayment/{id}")
      public ResultUtil getPaymentByObject(@PathVariable("id") Long id) {
          return restTemplate.getForObject(PAYMENT_URL + "/payment/getById/" + id, ResultUtil.class);
      }
  
      /**
       * 获取订单
       * restTemplate.getForEntity：返回的对象为 ResponseEntity 对象，包含一些重要信息
       *
       * @param id id
       * @return ResultUtil
       */
      @GetMapping("/getPayment/entity/{id}")
      public ResultUtil getPaymentByEntity(@PathVariable("id") Long id) {
          ResponseEntity<ResultUtil> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/getById/" + id, ResultUtil.class);
          if (entity.getStatusCode().is2xxSuccessful()) {
              return entity.getBody();
          } else {
              return ResultUtil.failure(ResultCodeEnum.FAILURE);
          }
      }
  
  }
  ```

  

* 访问：http://localhost:6000/order/getPayment/entity/1

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401050947833.png" alt="image-20240105094725803" style="zoom:100%;float:left" />



* **注意：** 在浏览器访问可能会出现如下情况

  * 具体解决方案如：https://blog.csdn.net/whiteBearClimb/article/details/108054219

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401050946759.png" alt="image-20240105094624716" style="zoom:100%;float:left" />



## Ribbon 核心组件之IRule

* IRule：该接口表示负载均衡的策略，其中不同的实现类代表了不同的负载策略。

* 如下图是 IRule 的 `UML` 类图。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401051029156.png" alt="image-20240105102931121" style="zoom:100%;float:left" />

1. `RoundRobinRule`
   * **轮询机制：**Ribbon 默认采用的也是 轮询机制。
2. `RandomRule`
   * **随机机制**
3. `RetryRule`
   * **重试机制：**当某个实例出现故障时，它可以尝试选择其他实例进行重试，帮助提高服务的可靠性。
4. `WeightedResponseTimeRUle`
   * **加权响应时间机制：**权重机制是对 `RoundRobinRule` 策略的扩展，响应的速度越快的实例权重就越大。
5. `BestAvailableRule`
   * **最佳可用机制机制：**该机制会选择并发量最小的实例来处理请求，以确保选择的实例负载相对较低。
6. `AvailabilityFilteringRule`
   * **可用性过滤机制：**该机制会过滤掉故障实例和并发连接数过高的实例，选择剩余的实例来处理请求，以确保选择的实例都是可用的且负载适中的。
7. `ZoneAvoidanceRule`
   * **区域避免机制：**该机制会尽量避免选择和调用处于相同区域的实例，以增加系统的可用性和容错性。



### 自定义负载均衡机制

> 我们基于上面案例进行开发

**代码结构**

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401051134809.png" alt="image-20240105113440780" style="zoom:100%;float:left" />



**实现思路**

1. Step-1：自定义规则类 `MySelfRule`
2. Step-2：修改主启动类



* Step-1：自定义规则类 `MySelfRule`

  * 注意：新建的 `MySelfRule` 不要和主启动类放在同一个包下

  ```java
  package cn.wickson.cloud.ribbon.rule;
  
  /**
   * 自定义规则
   *
   * @author ZhangZiHeng
   * @date 2024-01-05
   */
  @Configuration
  public class MySelfRule {
  
      /**
       * 自定义随机规则
       *
       * @return IRule
       */
      @Bean
      public IRule myRule() {
          return new RandomRule();
      }
  
  }
  ```



* Step-2：修改主启动类

  * 注意：我们在主启动类新增一个注解 `@RibbonClient`

  ```java
  /**
   * 订单服务-微服务启用类
   *
   * @author ZhangZiHeng
   * @date 2024-01-04
   */
  @EnableDiscoveryClient
  @SpringBootApplication
  @RibbonClient(name = "spring-cloud-cluster-eureka-payment", configuration = MySelfRule.class)
  public class SpringCloudRibbonOrderApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(SpringCloudRibbonOrderApplication.class, args);
      }
  
  }
  ```



* 测试

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401051304268.gif" alt="动画" style="zoom:100%;float:left" />



## 负载均衡算法

* 负载均衡算法规则如下

```
rest 接口第几次请求 % 服务器集群总数量 = 实际调用服务器位置下标
```

* 例如，假设我们存在两个实例（`3600` 、 `3700`），下标分别为 0 和 1 ，按照负载均衡的轮询算法如下：

```
第一次请求：1 % 2 = 1，则找到下标为1的机器，服务地址为：127.0.0.1：3700
第二次请求：2 % 2 = 0，则找到下标为0的机器，服务地址为：127.0.0.1：3600
第一次请求：3 % 2 = 1，则找到下标为1的机器，服务地址为：127.0.0.1：3700
...
```

* 具体实现方式如下
* `ILoadBalance.java`

```java
public interface ILoadBalance {

    ServiceInstance instances(List<ServiceInstance> serviceInstances);

}
```

* `LoadBalanceImpl.java`
  * 主要是使用到了自旋锁进行实现

```java
/**
 * 默认负载均衡算法
 *
 * @author ZhangZiHeng
 * @date 2024-01-05
 */
public class LoadBalanceImpl implements ILoadBalance {

    private AtomicInteger nextServerCyclicCounter;

    public LoadBalanceImpl() {
        this.nextServerCyclicCounter = new AtomicInteger(0);
    }

    private int incrementAndGetModulo(int modulo) {
        int current;
        int next;
        do {
            current = this.nextServerCyclicCounter.get();
            next = (current + 1) % modulo;
        } while (!this.nextServerCyclicCounter.compareAndSet(current, next));
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        int index = incrementAndGetModulo(serviceInstances.size());
        return serviceInstances.get(index);
    }
}
```