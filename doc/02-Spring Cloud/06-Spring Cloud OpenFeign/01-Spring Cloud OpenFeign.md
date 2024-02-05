# Spring Cloud `OpenFeign`

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下服务当中：
>
> * [07-spring-cloud-openfeign-order-7000](https://github.com/wicksonZhang/Spring-Cloud/tree/main/07-spring-cloud-openfeign-order-7000)

## 基本概念

### `OpenFeign` 是什么

> `OpenFeign` 官网地址：https://spring.io/projects/spring-cloud-openfeign/#overview

* `OpenFeign` 是一个用于简化 `RESTful` 服务调用的声明式 HTTP 客户端库。主要解决的问题还是微服务中服务与服务之间的通信。



### `OpenFeign` 优缺点

**优点**

* **声明式的接口定义**：使用注解和接口定义 HTTP 请求和响应，使得代码更加清晰易读。

  ```java
  @Component 
  @FeignClient(value = "cloud-payment") 
  public interface TestService {
      
      @GetMapping("/payment/list")
      ResultDto list();
      
  }
  ```

* **易于维护**：声明式的方式让代码更直观，易于理解和维护。



**缺点**

* **灵活性受限**：虽然简化了调用过程，但有时某些高级特性可能无法满足特定需求，需要额外的定制或扩展。



### `OpenFeign` 应用场景

* **微服务中服务之间的通信**
  * 微服务架构体系中，服务与服务之间进行通信。`OpenFeign` 可以之间通过声明式接口定义和注解进行开发，简化了配置。
* **服务治理、负载均衡和故障转移**
  * `OpenFeign` 可以集成服务注册与发现机制以及负载均衡器（如Ribbon），实现服务的动态发现和选择，从而提供负载均衡和故障转移的功能。



### Feign 和 `OpenFeign` 区别

> `Feign` 解决了 `Ribbon` 开发过程中模板式的开发。

**Feign**

1. 开发团队：`NetFlix` 团队开发
2. 定义方式：通过接口和注解定义 HTTP 请求和响应
3. 目前已经停止维护

**`OpenFeign`**

1. 开发团队：`Spring Cloud` 团队，对 `Feign` 进行增强
2. 定义方式：通过接口和注解定义 HTTP 请求和响应
3. 目前还在维护



## OpenFeign 具体实现

* 实现需求

  * 我们本章节的 `OpenFeign` 实现，还是基于我们 `Eureka` 的集群案例，只是不需要订单服务。采用 `OpenFeign` 的服务。

* 实现思路

  * 其他的四个服务我们还是延用 Eureka 的集群版。
  * Step-1：创建订单服务 `07-spring-cloud-openfeign-order-7000`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401051751669.png" alt="image-20240105175111635" style="zoom:100%;float:left" />



* 实现步骤

  1. Step-1：导入 `pom.xml` 依赖
  2. Step-2：修改 `application.properties` 文件
  3. Step-3：创建主启动类
  4. Step-4：编写服务调用接口
  5. Step-5：编写控制类
* **Step-1：导入 `pom.xml` 依赖**
  * 本次需要导入依赖：`spring-cloud-starter-openfeign`

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

        <!-- 服务调用依赖包：OpenFeign -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
    </dependencies>
```

* **Step-2：修改 `application.properties` 文件**

```properties
# 服务端口
server.port=7000
# 应用名称
spring.application.name=spring-cloud-openfeign-order
# 是否向注册中心注册自己
eureka.client.register-with-eureka=true
# 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
eureka.client.fetch-registry=true
# 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
eureka.client.serviceUrl.defaultZone=http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka

# 默认就是应用名称:端口，设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-order:3500
eureka.instance.instance-id=spring-cloud-openfeign-order:7000
# 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
eureka.instance.prefer-ip-address=true
```

* **Step-3：创建主启动类**
  * 在启动类中开启服务调用注解：`@EnableFeignClients`

```java
/**
 * OpenFeign 主启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-05
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"cn.wickson.cloud"})
public class SpringCloudOpenFeignOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudOpenFeignOrderApplication.class, args);
    }

}
```

* **Step-4：编写服务调用接口**

```java
/**
 * 远程调用支付服务接口
 *
 * @author ZhangZiHeng
 * @date 2024-01-05
 */
@Component
@FeignClient(value = "SPRING-CLOUD-CLUSTER-EUREKA-PAYMENT")
public interface IPaymentFeignService {

    /**
     * 调用支付服务的顶单请求
     */
    @GetMapping("/payment/getById/{id}")
    public PaymentRespDTO getPaymentById(@PathVariable("id") Long id);

}
```

* **Step-5：编写控制类**

```java
@Slf4j
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IPaymentFeignService paymentFeignService;

    /**
     * 获取订单
     * restTemplate.getForObject：返回对象为响应体中数据转化成的对象，基本上可以理解为 Json
     *
     * @param id id
     * @return ResultUtil
     */
    @GetMapping("/getPayment/{id}")
    public PaymentRespDTO getPaymentByObject(@PathVariable("id") Long id) {
        PaymentRespDTO paymentRespDTO = paymentFeignService.getPaymentById(id);
        log.debug("paymentRespDTO: " + paymentRespDTO);
        return paymentRespDTO;
    }
}
```

* 测试结果如下

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401051758015.gif" alt="动画" style="zoom:100%;float:left" />



## OpenFeign 连接超时

* **Open Feign 连接超时配置默认是 1S**

实现步骤

* 直接在配置文件中添加 `OpenFeign` 连接超时配置

```properties
########################## open Feign 配置连接超时-start ##########################
# 建立连接所有的时间，适用于网络状况正常的情况，两端建立所花费时间
feign.client.config.default.connect-timeout=5000
# 建立连接后服务端读取可用资源所有的时间，默认是 1S
feign.client.config.default.read-timeout=5000
##########################  open Feign 配置连接超时-end  ##########################
```

* 在全局异常配置文件中配置

```java
    /**
     * 系统异常处理
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResultUtil handleThrowable(Throwable e, HttpServletRequest request) {
        log.error("requestUrl：{}，系统内部异常", request.getRequestURI(), e);
        return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR);
    }
```

* 测试，我们在 `PaymentService` 中打上断点

  ![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401071827894.gif)



## OpenFeign 日志打印

**日志级别**

1. `NONE`：默认的，不显示任何日志
2. `Basic`：仅记录请求方法、URL、响应状态以及执行时间
3. `Headers`：除了 `Basic` 中定义的信息之外，还有请求和响应头信息
4. `Full`：除了 `Headers` 中定义的信息之外，还有请求和响应的正文以及元数据



**开启日志打印** 

* `FeignConfig.java`

```java
/**
 * OpenFeign 日志配置
 */
@Configuration
public class FeignConfig {

    /**
     * 1. `NONE`：默认的，不显示任何日志
     * 2. `Basic`：仅记录请求方法、URL、响应状态以及执行时间
     * 3. `Headers`：除了 `Basic` 中定义的信息之外，还有请求和响应头信息
     * 4. `Full`：除了 `Headers` 中定义的信息之外，还有请求和响应的正文以及元数据
     *
     * @return Logger.Level
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
```

* `application.properties`

```properties
########################## open Feign 日志打印-start ##########################
logging.level.cn.wickson.cloud.openfeign.order.feign.IPaymentFeignService=debug
##########################  open Feign 日志打印-end  ##########################
```



**测试**

```java
2024-01-07 19:07:47.416 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] ---> GET http://SPRING-CLOUD-CLUSTER-EUREKA-PAYMENT/payment/getById/1 HTTP/1.1
2024-01-07 19:07:47.416 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] ---> END HTTP (0-byte body)
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] <--- HTTP/1.1 200 (8ms)
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] connection: keep-alive
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] content-type: application/json
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] date: Sun, 07 Jan 2024 11:07:47 GMT
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] keep-alive: timeout=60
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] transfer-encoding: chunked
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] 
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] {"id":1,"amount":"1000.99","port":3600}
2024-01-07 19:07:47.425 DEBUG 16240 --- [nio-7000-exec-2] c.w.c.o.o.feign.IPaymentFeignService     : [IPaymentFeignService#getPaymentById] <--- END HTTP (39-byte body)
```

