# Spring Cloud Gateway

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下服务当中：
>
> * [09-spring-cloud-gateway-server-9000](https://github.com/wicksonZhang/Spring-Cloud/tree/main/09-spring-cloud-gateway-server-9000)

## 基础概念

### Gateway 是什么

> 官网地址：https://spring.io/projects/spring-cloud-gateway/

Spring Cloud Gateway：是基于 `Spring Boot2、WebFlux` 和 `Reactor` 的 `Api` 网关，`Gateway` 主要解决了微服务架构中如下问题：

1. **动态路由：**在微服务架构中，当请求进入到达系统时，首先会进入到网关服务，网关会动态的将请求路由到不同的后端服务。
2. **请求过滤和修改：**Gateway 支持使用过滤器来对请求进行处理，例如在请求前、请求后或错误时执行特定逻辑。例如，我们可以在网关中进行身份校验、认证、授权等功能。
3. **负载均衡：** 通过与 Spring Cloud Discovery 结合使用，Gateway 可以自动地将请求分发到可用的服务实例，从而实现负载均衡。



如下图是 Gateway 的工作流程

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401111748874.png" alt="Spring Cloud Gateway Diagram" style="zoom:80%;float:left" />

### Gateway 优缺点

**优点**

* **集成 `Spring` 生态系统：**作为Spring Cloud项目的一部分，Gateway天然地集成了Spring的生态系统。
* **灵活的路由规则：** Spring Cloud Gateway支持动态配置的路由规则，使得系统可以根据需要灵活地进行请求路由，适应不同的微服务架构。
* **强大的过滤器系统：** Gateway提供了灵活的过滤器系统，可以对请求进行各种处理，包括身份验证、鉴权、请求修改等，从而满足不同场景的需求。

**缺点**

* **学习曲线：**如果不熟悉微服务架构和网关概念，可能不太好理解网关的概念。

总体来说 Gateway 是基于 `Zuul` 进行发展而来的，目前来说 `Gateway` 还是十分强悍的一款微服务网关。



### Gateway 应用场景

> 我们需要明白，Gateway 主要的作用还是进行网关，所有的请求首先都会先进入网关服务，我们基于这个功能进行扩展.

* **白名单控制**

  * 我们在请求头中检测，如果是我们白名单中的请求则进行放行，如果不是则直接返回，不进行路由。

  ```java
  @Slf4j
  @Component
  public class UserAuthenticationFilter implements GlobalFilter, Ordered {
      
      @Resource
      private BlackAndWhiteList blackAndWhiteList;
      
       @Override
      public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
          ServerHttpRequest request = exchange.getRequest();
          ServerHttpResponse response = exchange.getResponse();
  
          // 验证需要放行的白名单
          if (this.isValidWhiteList(request)) {
              return chain.filter(exchange);
          }
          
          // TODO ...
          
  		return chain.filter(serverWebExchange);
      }
      
          /**
       * 验证需要放行的白名单
       */
      private boolean isValidWhiteList(ServerHttpRequest request) {
          String path = request.getPath().value();
          String[] whiteList = blackAndWhiteList.getWhiteList();
          if (ArrayUtil.isNotEmpty(whiteList)) {
              for (String whiteUrl : whiteList) {
                  if (path.lastIndexOf(whiteUrl) > -1) {
                      return true;
                  }
              }
          }
          return false;
      }
  ```

* **安全性和认证**

  * 通过在网关层面进行安全性和认证的处理，Spring Cloud Gateway 可以集中管理访问控制、身份验证和授权。

  ```java
       @Override
      public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
          ServerHttpRequest request = exchange.getRequest();
          ServerHttpResponse response = exchange.getResponse();
          
          // 验证token
          String token = request.getHeaders().getFirst(jwtUtil.getHeader());
          if (StrUtil.isBlankOrUndefined(token)) {
              return unauthorizedResponse(response, ResultCodeEnum.TOKEN_ISNULL_ERROR);
          }
          
          // TODO ...
          
  		return chain.filter(serverWebExchange);
      }
  ```



## Gateway 具体操作

* 实现需求

  * 我们本章节的 `Gateway` 实现，还是基于我们 `Eureka` 的集群案例。
  * 我们首先将请求到网关，网关进行动态路由到不同的服务当中。

* 实现思路

  * Step-1：创建网关服务 `09-spring-cloud-gateway-server-9000`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401121303763.png" alt="image-20240112130300730" style="zoom:100%;float:left" />

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401121303356.png" alt="image-20240112130349335" style="zoom:100%;float:left" />

* **实现步骤**

1. Step-1：导入 `pom.xml` 依赖
2. Step-2：修改 `application.yml` 文件
3. Step-3：创建主启动类

* **Step-1：导入 `pom.xml` 依赖**

  * 注意：不需要导入 web 服务

  ```xml
      <dependencies>
          <!-- 微服务网关依赖 -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-gateway</artifactId>
          </dependency>
  
          <!-- 服务注册中心的客户端 eureka-client -->
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
          </dependency>
  
      </dependencies>
  ```

* **Step-2：修改 `application.properties` 文件**

  * **Route（路由）：**路由是构建网关的基本模块，它由ID，目标URI，一系列的断言和过滤器组成，如果断言为true则匹配该路由。
  * **Predicate（断言）：**参考的是 `Java8` 的 `java.util.function.Predicate` 开发人员可以匹配HTTP请求中的所有内容(例如请求头或请求参数)，如果请求与断言相匹配则进行路由
  * **Filter（过滤）：**指的是 `Spring` 框架中 `GatewayFilter` 的实例，使用过滤器，可以在请求被路由前或者之后对请求进行修改。

  ```yaml
  # 服务端口
  server:
    port: 9000
  # 应用名称
  spring:
    application:
      name: spring-cloud-gateway-server
    #--------------------------------- Gateway 配置 start ---------------------------------
    cloud:
      gateway:
        discovery:
          locator:
            enabled: true #开启从注册中心动态创建路由的功能，利用微服务名进行路由
        routes:
          # 订单服务
          - id: order-route # 路由的Id，没有固定的规则，但要求唯一
            uri: lb://SPRING-CLOUD-CLUSTER-EUREKA-ORDER # 替换为你的服务ID，lb:// 表示使用负载均衡
            predicates: # 断言，路径相匹配的进行路由
              - Path=/order/**
  
          # 支付服务
          - id: payment-route # 路由的Id，没有固定的规则，但要求唯一
            uri: lb://SPRING-CLOUD-CLUSTER-EUREKA-PAYMENT # 替换为你的服务ID，lb:// 表示使用负载均衡
            predicates: # 断言，路径相匹配的进行路由
              - Path=/payment/**
    #--------------------------------- Gateway 配置  end  ---------------------------------
  
  #--------------------------------- Eureka 配置 start ---------------------------------
  eureka:
    instance:
      hostname: spring-cloud-gateway-server
      # 设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-payment:3600
      instance-id: spring-cloud-gateway-server:9000
      # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
      prefer-ip-address: true
    client:
      service-url:
        register-with-eureka: true
        fetch-registry: true
        # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
        defaultZone: http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
  #--------------------------------- Eureka 配置  end  ---------------------------------
  
  ```

* **Step-3：创建主启动类**

  ```java
  /**
   * 网关-启动类
   *
   * @author ZhangZiHeng
   * @date 2024-01-12
   */
  @EnableEurekaClient
  @SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
  public class SpringCloudGatewayApplication {
  
      public static void main(String[] args) {
          SpringApplication.run(SpringCloudGatewayApplication.class, args);
      }
  
  }
  ```

* **Step-4：测试**

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401121314267.gif" alt="动画" style="zoom:100%;float:left" />



## Gateway 之 Predicate（断言）

* 关于 `Predicat` 的介绍：https://cloud.spring.io/spring-cloud-gateway/reference/html/#gateway-request-predicates-factories

* 例如，我们需要在 2017 年 1 月 20 日 17:42 之后的请求进行匹配。

  ```yaml
  spring:
    cloud:
      gateway:
        routes:
        - id: after_route
          uri: https://example.org
          predicates:
          - After=2017-01-20T17:42:47.789-07:00[America/Denver]
  ```

  

## Gateway 之 Filter（过滤）

* 关于 `Filter` 的介绍：https://cloud.spring.io/spring-cloud-gateway/reference/html/#global-filters

* 我们自定义 `Filter` 实现过滤用户登录认证

  ```java
  /**
   * 用户认证-过滤器类
   */
  @Component
  public class UserAuthenticationFilter implements GlobalFilter, Ordered {
  
      @Override
      public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
          ServerHttpRequest request = exchange.getRequest();
          ServerHttpResponse response = exchange.getResponse();
          String username = request.getQueryParams().getFirst("username");
          if (username == null) {
              response.setStatusCode(HttpStatus.UNAUTHORIZED);
              response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
              ResultUtil result = ResultUtil.failure(ResultCodeEnum.TOKEN_ISNULL_ERROR);
              DataBuffer dataBuffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
              return response.writeWith(Flux.just(dataBuffer));
          }
          return chain.filter(exchange);
      }
  
      @Override
      public int getOrder() {
          return -1;
      }
  }
  ```

  * 测试

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401121344961.gif" alt="动画" style="zoom:100%;float:left" />

