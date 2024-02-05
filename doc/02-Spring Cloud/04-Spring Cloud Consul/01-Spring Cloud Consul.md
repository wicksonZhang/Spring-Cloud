# Spring Cloud Consul

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下2个服务当中：
>
> * [05-spring-cloud-singleton-consul-order-5100](https://github.com/wicksonZhang/Spring-Cloud/tree/main/05-spring-cloud-singleton-consul-order-5100)
> * [05-spring-cloud-singleton-consul-payment-5000](https://github.com/wicksonZhang/Spring-Cloud/tree/main/05-spring-cloud-singleton-consul-payment-5000)

## 基础概念

> 官网文档地址：https://developer.hashicorp.com/consul/docs/intro

### Consul 是什么？

* `Consul` 用于服务发现、健康检查和动态配置的中间件。在 Spring Cloud 来实现服务注册与发现、配置管理、负载均衡等功能，从而构建和管理分布式系统。



### Consul 优缺点

**优点**

1. **服务发现与注册**
2. **健康检查**
3. **提供图形化界面**



### Consul 解决了什么问题

> 我们上面讲解的 `Eureka` 、`Zookeeper`、`Consul` 本质上都是注册中心，解决的问题都是分布式场景中存在的问题。例如，服务注册与发现、健康检查、一致性等问题。

* Consul 解决了分布式系统中服务发现、健康检查、一致性、动态配置等方面的问题，有助于构建可靠、灵活和高效的分布式系统。



## 安装 `Consul`

> `Consul` 下载地址：[Install | Consul | HashiCorp Developer](https://developer.hashicorp.com/consul/install#Windows)
>
> 本次下载的版本是1.6.1：https://releases.hashicorp.com/consul/1.6.1/consul_1.6.1_windows_amd64.zip

**安装环境**

* 操作系统：Win 11
* Consul 版本：Consul-1.6.1

**安装步骤**

1. Step-1：下载安装包
2. Step-2：解压安装包
3. Step-3：执行 exe 文件

**具体实现**

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031628306.png" alt="image-20240103162816270" style="zoom:100%;float:left" />



## 启动 `Consul`

* 启动命令

  ```sh
  consul agent -dev
  ```

* 具体操作如下

  ![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031631442.gif)

* 访问 `Consul` 首页：http://localhost:8500

  ![image-20240103163244876](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031632909.png)



## Consul 单机操作

* 实现需求

  * 我们将上一章节的订单、支付微服务注册到 `Consul` 当中

* 实现思路

  * Step-1：创建支付服务 `04-spring-cloud-singleton-consul-payment-5000`
  * Step-2：创建订单服务 `04-spring-cloud-singleton-consul-order-5100`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031716768.png" alt="image-20240103171620741" style="zoom:100%;float:left" />

### 创建支付服务

* 创建支付服务：`05-spring-cloud-consul-payment-5000`

* 实现步骤

  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类
  * Step-4：编写业务类

* Step-1：导入 `pom.xml` 依赖

  ```xml
      <dependencies>
          <!-- 导入公共依赖包 -->
          <dependency>
              <groupId>cn.wickson.cloud</groupId>
              <artifactId>01-spring-cloud-common</artifactId>
              <version>1.0-SNAPSHOT</version>
          </dependency>
  
          <!-- 导入 consul 依赖 -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-consul-discovery</artifactId>
          </dependency>
      </dependencies>
  ```

* Step-2：修改 `application.properties` 文件

  ```properties
  # 端口
  server.port=5000
  # 服务别名
  spring.application.name=spring-cloud-consul-payment-5000
  # consul 注册节点
  spring.cloud.consul.host=localhost
  spring.cloud.consul.port=8500
  spring.cloud.consul.discovery.service-name=${spring.application.name}
  ```

* Step-3：创建主启动类

  ```java
  /**
   * 支付服务-微服务启用类
   *
   * @author ZhangZiHeng
   * @date 2024-01-03
   */
  @EnableDiscoveryClient
  @SpringBootApplication
  public class SpringCloudConsulPayment {
  
      public static void main(String[] args) {
          SpringApplication.run(SpringCloudConsulPayment.class, args);
      }
  
  }
  ```

* Step-4：编写业务类

  ```java
  /**
   * 支付服务-控制类
   *
   * @author ZhangZiHeng
   * @date 2024-01-03
   */
  @RestController
  @RequestMapping("/payment")
  public class PaymentController {
  
      @Value("${server.port}")
      private String serverPort;
  
      @GetMapping("/consul")
      public ResultUtil paymentConsul() {
          return ResultUtil.success("Spring Cloud with consul port: " + serverPort + ", UUID：" + UUID.randomUUID().toString());
      }
  
  }
  ```

  

### 创建订单服务

* 创建支付服务：`05-spring-cloud-consul-order-5100`

* 实现步骤

  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类
  * Step-4：编写业务类

* 其中 Step-1、Step-2、Step-3 与上面操作 `创建支付服务` 保持一致，只需要修改名称

* Step-4：编写业务类

  * `OrderController.java` 控制类，其中 PAYMENT_URL 需要修改为 `05-spring-cloud-consul-payment-5000` 注册的服务名。

  ```java
  /**
   * 订单服务-控制类
   *
   * @author ZhangZiHeng
   * @date 2023-01-03
   */
  @Slf4j
  @Validated
  @RestController
  @RequestMapping("/order")
  public class OrderController {
  
      public static final String PAYMENT_URL = "http://spring-cloud-consul-payment-5000";
  
      @Resource
      private RestTemplate restTemplate;
  
      /**
       * 获取订单
       *
       * @return ResultUtil
       */
      @GetMapping("/getPayment/consul")
      public ResultUtil getPayment() {
          return restTemplate.getForObject(PAYMENT_URL + "/payment/consul" , ResultUtil.class);
      }
  
  }
  ```

  * `RestTemplateConfig.java`

  ```java
  /**
   * RestTemplate 配置
   *
   * @author ZhangZiHeng
   * @date 2023-01-03
   */
  @Configuration
  public class RestTemplateConfig {
  
      @Bean
      @LoadBalanced
      public RestTemplate restTemplate() {
          return new RestTemplate();
      }
  
  }
  ```

  

### 单元测试

#### 是否在 `Consul` 注册节点

> http://localhost:8500

![image-20240103172254096](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031722134.png)



#### 查看支付服务信息

![image-20240103172500397](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031725440.png)



#### 查看订单服务信息

![image-20240103172528120](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031725158.png)



#### 访问订单服务

> 访问订单服务时，我们可以看到对应的端口信息时 支付微服务的 5000

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031757127.png" alt="image-20240103173057222" style="zoom:100%;float:left" />



## CAP 理论

> 我们已经整体的学习了三个注册中心：Eureka、Zookeeper、Consul。在微服务体系中他们主要的作用都是承担了服务发现与注册，服务的健康检查等等。我们从开发语言和CAP角度看一下这三个注册中心有什么不同。

* **CAP 理论**
  * **Consistency：** 一致性，指系统的所有节点在同一时间看到的数据是一致的。
  * **Availability：** 可用性，指系统在有请求时能够返回正确的响应，即系统保证服务可用，不会因节点故障而出现不可用的情况。
  * **Partition Tolerance：** 分区容错性，指系统在遇到网络分区（节点之间通信失败，不能互相通信）的情况下仍然能够继续运行。

| 特性         | Eureka                                             | Zookeeper                                                  | Consul                                                   |
| :----------- | :------------------------------------------------- | ---------------------------------------------------------- | -------------------------------------------------------- |
| **开发语言** | Java                                               | Java                                                       | Go、但提供了 REST API 可以用于其他语言                   |
| **CAP 特性** | 通常为 AP（可用性和分区容错性）                    | 通常为 CP（一致性和分区容错性）                            | 可以根据配置变为 CP 或 AP                                |
| **服务发现** | 可以作为服务注册与发现的中心组件，支持微服务架构。 | 提供服务发现功能，但需要更多自定义和额外开发。             | 提供了内置的服务注册与发现功能，易于集成和使用。         |
| **使用场景** | 适用于需要快速搭建和维护的微服务架构。             | 适用于需要强一致性的分布式系统，如分布式协调、配置管理等。 | 适用于需要灵活性并可以根据需求选择一致性或可用性的系统。 |
| **社区支持** | 相对活跃的社区支持，但可能不如其他选项成熟。       | 成熟且活跃的社区支持，被广泛应用于许多大规模系统中。       | 有不少用户和社区支持，但相对于 Zookeeper 可能规模较小。  |
