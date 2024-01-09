# Spring Cloud `Hystrix`

## 基本概念

### `Hystrix` 是什么

* `Hystrix` 是 `Netflix`  开源的一个用于构建分布式系统中的延迟和容错的库。主要核心功能有 **服务降级、服务熔断、服务限流**。
  * **服务降级**：当依赖服务不可用时，返回一个备用的默认值或者执行备选方案，确保系统的基本功能仍然可以使用。
  * **服务熔断**：服务熔断是一种机制，用于防止故障在分布式系统中蔓延。类似于家庭中的保险丝，服务熔断器会在依赖服务出现故障时进行打开，停止对该服务的调用，避免持续的失败请求导致系统崩溃。
  * **服务限流**：服务限流是一种控制系统流量的方法，用于防止系统被过多请求压垮。通过限制对某个服务的并发请求或单位时间内的请求数量，可以避免系统因过载而崩溃或变得不稳定。



### `Hystrix` 优缺点

**优点**

* **容错性强：** Hystrix 提供了服务降级、熔断和限流等机制，可以防止故障在整个系统中扩散，保证系统的稳定性和可用性。
* **提高系统可靠性：** 它能够在依赖服务不可用时提供备用方案，确保系统的基本功能可以继续运行。
* **监控和度量：** Hystrix 提供了丰富的监控功能，可以实时监控依赖服务的调用情况和性能指标，帮助进行故障排查和性能优化。



**缺点**

* 目前 `Hystrix`已经停止更新了。
* **复杂性增加：** 在项目中引入 Hystrix 可能会增加代码复杂性，需要对服务调用进行额外的封装和配置。



### `Hystrix` 应用场景

​		在一个微服务架构中，服务之间存在着依赖关系。例如，一个电子商务平台的订单服务依赖于库存服务和支付服务。如果库存服务出现了延迟或者不可用，订单服务可能因为等待库存服务的响应而出现延迟，甚至导致服务不可用。在这种情况下，我们就可以采用 `Hystrix` 进行处理。

**电商平台的库存服务**：

- **服务降级：** 当库存服务不可用时，订单服务可以返回一个预先设定的默认库存量，确保用户可以继续下单。
- **服务熔断：** 如果库存服务连续出现错误响应或超时，Hystrix 可以打开断路器，暂时停止对库存服务的调用，防止因大量失败请求导致订单系统崩溃。
- **服务限流：** 控制对库存服务的访问速率，防止订单系统因过多的库存查询请求而受到压力，保持系统稳定性。

**在线支付系统的支付服务**：

- **服务降级：** 当支付服务不可用时，可以返回一个预定义的错误码或信息，告知用户支付暂时不可用，而不是让用户无限等待或看到错误页面。
- **服务熔断：** 在支付服务故障频发时，Hystrix 可以打开断路器，阻止进一步的支付请求，避免因支付服务的连续失败导致整个系统不可用。
- **服务限流：** 控制支付请求的速率，避免系统因支付请求过多而导致支付服务不稳定或响应时间增加。



## Spring Cloud Hystrix 具体实现

### 服务降级

* 实现需求

  * 我们还是基于 支付微服务和订单服务的例子进行模拟

* 实现思路

  * 注册中心我们还是延用 Eureka 的集群版。
  * Step-1：创建订单服务 `08-spring-cloud-hystrix-order-8100`
  * Step-2：创建支付服务 `08-spring-cloud-hystrix-payment-8000`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401091439174.png" alt="image-20240109143939131" style="zoom:100%;float:left" />

#### 创建支付服务

* 创建支付服务： `08-spring-cloud-hystrix-payment-8000`

* 实现步骤

  1. Step-1：导入 `pom.xml` 依赖
  2. Step-2：修改 `application.properties` 文件
  3. Step-3：创建主启动类
  4. Step-4：编写业务类
  5. Step-5：编写控制类

* **Step-1：导入 `pom.xml` 依赖**

  ```xml
      <dependencies>
          <!-- 引入公共依赖包 -->
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
  
          <!-- 熔断限流 hystrix -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
          </dependency>
  ```

* **Step-2：修改 `application.properties` 文件**

  ```properties
  # 服务端口
  server.port=8000
  # 应用名称
  spring.application.name=spring-cloud-hystrix-payment
  # 是否向注册中心注册自己
  eureka.client.register-with-eureka=true
  # 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
  eureka.client.fetch-registry=true
  # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
  eureka.client.serviceUrl.defaultZone=http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
  # 设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-payment:3600
  eureka.instance.instance-id=spring-cloud-hystrix-payment:8000
  # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
  eureka.instance.prefer-ip-address=true
  ```

* **Step-3：创建主启动类**

  ```java
  /**
   * hystrix 启动类
   *
   * @author ZhangZiHeng
   * @date 2024-01-08
   */
  @EnableEurekaClient
  @SpringBootApplication
  public class SpringCloudHystrixPaymentApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(SpringCloudHystrixPaymentApplication.class, args);
      }
  
  }
  ```

* **Step-4：编写业务类**

  * `PaymentServiceImpl.java`

  ```java
  /**
   * 支付服务-应用服务实现类
   *
   * @author ZhangZiHeng
   * @date 2024-01-08
   */
  @Service
  public class PaymentServiceImpl implements IPaymentService {
  
      /**
       * 成功
       *
       * @return String
       */
      @Override
      public ResultUtil paymentBySuccess() {
          return ResultUtil.success("ThreadPool：" + Thread.currentThread().getName() + ", payment service success");
      }
  
      /**
       * 连接超时
       *
       * @return String
       */
      @Override
      public ResultUtil paymentByTimeOut() {
          int timeNumber = 3;
          try {
              // 模拟网络延迟
              TimeUnit.SECONDS.sleep(timeNumber);
          } catch (Exception exception) {
              exception.printStackTrace();
          }
          return ResultUtil.success("ThreadPool：" + Thread.currentThread().getName() + ", payment service timeout：" + timeNumber);
      }
  
  }
  ```

* **Step-5：编写控制类**

  ```java
  /**
   * 支付微服务-控制类
   *
   * @author ZhangZiHeng
   * @date 2024-01-08
   */
  @Slf4j
  @Validated
  @RestController
  @RequestMapping("/payment")
  public class PaymentController {
  
      @Resource
      private IPaymentService paymentService;
  
      @Value("${server.port}")
      private Integer serverPort;
  
      @GetMapping("/hystrix/success")
      public ResultUtil paymentSuccess() {
          return paymentService.paymentBySuccess();
      }
  
      @GetMapping("/hystrix/timeOut")
      public ResultUtil paymentTimeOut() {
          return paymentService.paymentByTimeOut();
      }
  
  }
  ```

#### 创建订单服务

> 订单服务会主动进行调用支付服务，所以我们将 `服务降级、服务熔断、服务隔离` 写在订单服务中。两端都可以写，我们目前写在调用方（订单服务）中。

* 创建订单服务： `08-spring-cloud-hystrix-order-8100`

* 实现步骤

  1. Step-1：导入 `pom.xml` 依赖
  2. Step-2：修改 `application.properties` 文件
  3. Step-3：创建主启动类
  4. Step-4：编写业务类
  5. Step-5：编写控制类

* **Step-1：导入 `pom.xml` 依赖**

  ```xml
      <dependencies>
          <!-- 公共依赖服务 -->
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
  
          <!-- 熔断限流 hystrix -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
          </dependency>
      </dependencies>
  ```

* **Step-2：修改 `application.properties` 文件**

  ```properties
  # 服务端口
  server.port=8100
  # 应用名称
  spring.application.name=spring-cloud-hystrix-order
  
  # ============================= Eureka =================================
  # 是否向注册中心注册自己
  eureka.client.register-with-eureka=true
  # 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
  eureka.client.fetch-registry=true
  # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
  eureka.client.serviceUrl.defaultZone=http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
  # 设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-payment:3600
  eureka.instance.instance-id=spring-cloud-hystrix-order:8100
  # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
  eureka.instance.prefer-ip-address=true
  # ============================= Eureka =================================
  
  # ============================= open Feign 配置start =============================
  # 建立连接所有的时间，适用于网络状况正常的情况，两端建立所花费时间
  feign.client.config.default.connect-timeout=5000
  # 建立连接后服务端读取可用资源所有的时间，默认是 1S
  feign.client.config.default.read-timeout=5000
  # =============================  open Feign 配置end  =============================
  
  # ============================= hystrix =================================
  # 配置 hystrix 连接超时
  feign.hystrix.enabled=true
  # https://blog.csdn.net/tszxlzc/article/details/106625387
  # 目前设置为5 S，默认是 1S（default_executionTimeoutInMilliseconds = 1000）
  hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds=5000
  # ============================= hystrix =================================
  ```

* **Step-3：创建主启动类**

  ```java
  /**
   * 订单服务-启动类
   *
   * @author ZhangZiHeng
   * @date 2024-01-08
   */
  @EnableHystrix // 启用 Hystrix 断路器
  @EnableEurekaClient // 启用 Eureka 客户端
  @EnableFeignClients // 启用 Feign 客户端
  @SpringBootApplication
  public class SpringCloudHystrixOrderApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(SpringCloudHystrixOrderApplication.class, args);
      }
  
  }
  ```

* **Step-4：OpenFeign 调用类**

  * `IPaymentHystrixService.java`

  ```java
  /**
   * Feign远程调用接口支付接口
   *
   * @author ZhangZiHeng
   * @date 2024-01-09
   */
  @Component
  // value 声明一个 Feign 客户端，value 指定了要调用的服务名为 "SPRING-CLOUD-HYSTRIX-PAYMENT"，
  // fallback 指定了当调用失败时的降级处理类为 PaymentFallbackService.class
  @FeignClient(value = "SPRING-CLOUD-HYSTRIX-PAYMENT", fallback = PaymentFallbackService.class)
  public interface IPaymentHystrixService {
  
      /**
       * 成功
       *
       * @return String
       */
      @GetMapping("/payment/hystrix/success")
      public ResultUtil paymentBySuccess();
  
      /**
       * 连接超时
       *
       * @return String
       */
      @GetMapping("/payment/hystrix/timeOut")
      public ResultUtil paymentByTimeOut();
  
  }
  ```

  * `PaymentFallbackService.java`

  ```java
  @Component
  public class PaymentFallbackService implements IPaymentHystrixService {
  
      @Override
      public ResultUtil paymentBySuccess() {
          return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR);
      }
  
      @Override
      public ResultUtil paymentByTimeOut() {
          return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR);
      }
  
  }
  ```

* **Step-5：编写控制类**

  ```java
  /**
   * 订单服务-控制类
   *
   * @author ZhangZiHeng
   * @date 2024-01-08
   */
  @Slf4j
  @Validated
  @RestController
  @RequestMapping("/order")
  public class OrderController {
  
      @Resource
      private IPaymentHystrixService paymentHystrixService;
  
      @GetMapping("/getPaymentSuccess")
      public ResultUtil getPaymentSuccess() {
          return paymentHystrixService.paymentBySuccess();
      }
  
      @GetMapping("/getPaymentTimeOut")
      public ResultUtil getPaymentTimeOut() {
          return paymentHystrixService.paymentByTimeOut();
      }
  
  }
  ```

* **注意：我们上面的操作直接做的全局限流操作，其实可以对单个方法进行限流。**

  ```java
  @GetMapping("/getPaymentTimeOut")
  @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
      @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value = "5000")
  })
  public ResultUtil getPaymentTimeOut() {
      return paymentHystrixService.paymentByTimeOut();
  }
  
  public ResultUtil paymentTimeOutFallbackMethod(){
      return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR);
  }
  ```

#### 单元测试

* 正常情况调用

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401091546155.gif" alt="动画" style="zoom:100%;float:left" />

* 我们将支付服务服务进行关闭，然后调用

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401091548590.gif" alt="动画" style="zoom:100%;float:left;" />



### 服务熔断





