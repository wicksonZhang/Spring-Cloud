# Spring Cloud Gateway

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



