# Spring Cloud Stream

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下服务当中：
>
> * [11-spring-cloud-stream-consumer1-11200](https://github.com/wicksonZhang/Spring-Cloud/tree/main/11-spring-cloud-stream-consumer1-11200)
> * [11-spring-cloud-stream-consumer2-11300](https://github.com/wicksonZhang/Spring-Cloud/tree/main/11-spring-cloud-stream-consumer2-11300)
> * [11-spring-cloud-stream-gateway-11400](https://github.com/wicksonZhang/Spring-Cloud/tree/main/11-spring-cloud-stream-gateway-11400)
> * [11-spring-cloud-stream-producer-11100](https://github.com/wicksonZhang/Spring-Cloud/tree/main/11-spring-cloud-stream-producer-11100)
> * [11-spring-cloud-stream-web](https://github.com/wicksonZhang/Spring-Cloud/tree/main/11-spring-cloud-stream-web)
> * [11-spring-cloud-stream-websocket-11500](https://github.com/wicksonZhang/Spring-Cloud/tree/main/11-spring-cloud-stream-websocket-11500)

## 基础概念

* **消息驱动**：是一种编程模型，其中组件之间通过异步消息传递来实现松耦合、分布式的通信和协作，提高系统的可伸缩性和弹性。

### Spring Cloud Stream 解决了什么问题？

​		Spring Cloud Stream **解决了消息驱动微服务架构中消息生产者和消息消费者的解耦、消息传递、以及不同消息代理系统的适配问题**。

​		假设有一个电商系统，其中订单服务负责处理订单相关的业务，当订单服务产生一个新的订单时，需要将这条订单信息发送到**消息通道**，而不需要关心消息是如何被处理、传递到哪里的。订单服务产生订单信息之后，库存服务需要减少相应库存，那么库存服务只需要通过订阅相应的**消息通道**，处理订单创建的消息。

​		再举一个场景的例子，类似于微信公众号的消息推送。当公众号推送消息之后，只有订阅了这个公众号的人才能收到消息。

​		这种方式下，消息生产者和消费者之间是松耦合的，它们可以独立部署和演化，更好地支持微服务架构的原则。



### Spring Cloud Stream 是什么？

Spring Cloud Stream 是基于 Spring Boot 的一个用于构建消息驱动微服务的框架。具体的核心概念和特定如下：

1. **Binder（绑定器）：** Spring Cloud Stream 引入了 Binder 的概念，它是与消息代理系统交互的适配器。通过 Binder，可以方便地切换消息代理系统，比如从 RabbitMQ 切换到 Kafka，而不用修改应用程序代码。
2. **消息通道（Message Channels）：** Spring Cloud Stream 使用消息通道来实现消息的传递。应用程序可以将消息发送到通道，并从通道接收消息。
3. **消息处理（Message Processing）：** Spring Cloud Stream 提供了一组注解，如 `@StreamListener`，使得消息的处理逻辑变得简单明了。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401181521725.gif" alt="img" style="zoom:100%;float:left" />



### Spring Cloud Stream 的优缺点

**优点**

1. **简化配置和开发：** Spring Cloud Stream 简化了消息驱动微服务的配置和开发，通过声明式的方式，开发者只需要关注业务逻辑而不用过多考虑底层的消息传递细节。
2. **适配多种消息代理系统：** Spring Cloud Stream 支持多种消息代理系统，包括 RabbitMQ、Kafka、Redis 等，这使得系统更具灵活性。
3. **整合 Spring 生态系统：** Spring Cloud Stream 是 Spring Cloud 生态系统的一部分，可以与其他 Spring Cloud 组件无缝集成。



**缺点**

1. **过度抽象可能导致不灵活：** 尽管高度的抽象使得开发变得简单，但在一些特定场景下，过度的抽象可能会导致不够灵活。一些复杂的消息处理需求可能需要更详细的配置和定制。



## 核心注解

​		如下图中是 Spring Cloud Stream 的基本原理，其中*Binder* 层负责和MQ中间件的通信，应用程序 *Application Core* 通过 *inputs* 接收 *Binder* 包装后的 Message，相当于是消费者Consumer；通过 *outputs* 投递 Message给 *Binder*，然后由 *Binder* 转换后投递给MQ中间件，相当于是生产者Producer。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401220931781.png" alt="SCSt-with-binder" style="zoom:100%;float:left" />

* 针对上图中提供了相关的注解信息，具体如下：

| 组成            | 说明                                                         |
| --------------- | ------------------------------------------------------------ |
| Middleware      | 中间件，目前支持 *RabbitMQ* 和 *KafKa*                       |
| Binder          | 负责和MQ中间件进行连接和通信，可以动态的改变消息类型（对应 *Kafka* 的 topic，*RabbitMQ* 的 exchange） |
| @Input          | 注解标识输入通道，通过该输入通道接收到的消息进入应用程序     |
| @Output         | 注解标识输出通达，发布的消息将通过该通道离开应用程序         |
| @StreamListener | 监听队列，用于消费者的队列的消息接收                         |
| @EnableBinding  | 将信道 *change* 和 *exchange* 绑定在一起                     |



## 具体操作

* 准备工作
  * 具体 `RabbitMQ` 消息队列

* 实现需求

  1. 我们创建消息生产者和消息消费者。当生产者产生消息之后，两个消费者会通过消息通道监听到对应的消息。具体大流程如下图

     <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401190958348.png" alt="img" style="zoom:52%;float:left" />

* 实现思路

  1. Step-1：创建消息生产者 `11-spring-cloud-stream-producer`
  2. Step-2：创建消息消费者1 `11-spring-cloud-stream-consumer1-11200`
  3. Step-3：创建消息消费者2 `11-spring-cloud-stream-consumer2-11300`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401191428602.png" alt="image-20240119142823575" style="zoom:100%;float:left" />



### 创建消息生产者

**实现步骤**

```tex
Step-1: 创建消息生产者服务 11-spring-cloud-stream-producer-11100
Step-2: 导入 pom.xml 依赖
Step-3: 创建 bootstrap.yml
Step-4: 创建启动类 SpringCloudStreamProducerApplication
Step-5: 创建控制类 ProducerController
Step-6: 创建消息生产者 IMessageProvider、MessageProviderImpl
```

**Step-2: 导入 pom.xml 依赖**

```xml
    <dependencies>
        <!-- 引入公共依赖模块 -->
        <dependency>
            <groupId>cn.wickson.cloud</groupId>
            <artifactId>01-spring-cloud-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- Spring Cloud netflix eureka 服务-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <!-- Spring Cloud Stream 依赖 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
        </dependency>
    </dependencies>
```

**Step-3: 创建 bootstrap.yml**

```yaml
# 服务端口
server:
  port: 11100
# 应用名称
spring:
  application:
    name: spring-cloud-stream-producer
  cloud:
# --------------------------- Spring Cloud Stream 配置 start ---------------------------
    stream:
      # 绑定 rabbitmq 服务信息
      binders:
        # 定义的名称，用于整合 binding
        defaultRabbit:
          # 消息组件类型
          type: rabbit
          # 设置 RabbitMQ 的相关环境配置
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      # 服务的整合处理
      bindings:
        # 通道名称
        output:
          destination: stream-exchange
          content-type: application/json
          binder: defaultRabbit
# --------------------------- Spring Cloud Stream 配置 end ---------------------------

#--------------------------------- Eureka 配置 start ---------------------------------
eureka:
  instance:
    hostname: spring-cloud-stream-producer
    # 设置Eureka服务实例的唯一标识为 spring-cloud-stream-producer:11100
    instance-id: spring-cloud-stream-producer:11100
    # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
    prefer-ip-address: true
    # 设置心跳间隔时间, 默认 30 S
    lease-renewal-interval-in-seconds: 2
    # 设置服务过期时间配置,超过这个时间没有接收到心跳EurekaServer就会将这个实例剔除, 默认 90 S
    lease-expiration-duration-in-seconds: 5
  client:
    service-url:
      register-with-eureka: true
      fetch-registry: true
      # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
#--------------------------------- Eureka 配置  end  ---------------------------------
```

**Step-4: 创建启动类 SpringCloudStreamProducerApplication**

```java
/**
 * Spring Cloud Stream Producer 启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudStreamProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamProducerApplication.class, args);
    }

}
```

**Step-5: 创建控制类 ProducerController**

```java
/**
 * 生产者控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/producer")
public class ProducerController {

    @Resource
    private IMessageProvider messageProvider;

    @GetMapping(value = "/sendMessage")
    public String sendMessage() {
        return messageProvider.sendMessage();
    }

}
```

**Step-6: 创建消息生产者 IMessageProvider、MessageProviderImpl**

* `IMessageProvider.java`

```java
/**
 * 消息生产者
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
public interface IMessageProvider {

    String sendMessage();

}
```

* `MessageProviderImpl.java`

```java
/**
 * 消息生产者-实现类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@Slf4j
// 定义消息的推送管道
@EnableBinding(Source.class)
public class MessageProviderImpl implements IMessageProvider {

    // 消息发送管道
    @Resource
    private MessageChannel output;

    @Override
    public String sendMessage() {
        String uuid = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(uuid).build());
        return "生产者生产一条消息：" + uuid;
    }
}
```

### 创建消息消费者1

**实现步骤**

```tex
Step-1: 创建消息消费者1服务 11-spring-cloud-stream-consumer1-11200
Step-2: 导入 pom.xml 依赖
Step-3: 创建 bootstrap.yml
Step-4: 创建启动类 SpringCloudStreamConsumer1Application
Step-5: 创建监听类 Consumer1MessageListener
```

**Step-2: 导入 pom.xml 依赖**

```xml
    <dependencies>
        <!-- 引入公共依赖模块 -->
        <dependency>
            <groupId>cn.wickson.cloud</groupId>
            <artifactId>01-spring-cloud-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- Spring Cloud netflix eureka 服务-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>

        <!-- Spring Cloud Stream 依赖 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-stream-rabbit</artifactId>
        </dependency>
    </dependencies>
```

**Step-3: 创建 bootstrap.yml**

```yml
# 服务端口
server:
  port: 11200
# 应用名称
spring:
  application:
    name: spring-cloud-stream-consumer1
  cloud:
    # --------------------------- Spring Cloud Stream 配置 start ---------------------------
    stream:
      # 绑定 rabbitmq 服务信息
      binders:
        # 定义的名称，用于整合 binding
        defaultRabbit:
          # 消息组件类型
          type: rabbit
          # 设置 RabbitMQ 的相关环境配置
          environment:
            spring:
              rabbitmq:
                host: localhost
                port: 5672
                username: guest
                password: guest
      # 服务的整合处理
      bindings:
        # 通道名称
        input:
          # 表示使用的 exchange 名称定义
          destination: stream-exchange
          # 设置消息类型
          content-type: application/json
          # 设置绑定消息服务的具体设置
          binder: defaultRabbit
          group: consumer1
# --------------------------- Spring Cloud Stream 配置 end ---------------------------

#--------------------------------- Eureka 配置 start ---------------------------------
eureka:
  instance:
    hostname: spring-cloud-stream-consumer1
    # 设置Eureka服务实例的唯一标识为 spring-cloud-stream-consumer1:11200
    instance-id: spring-cloud-stream-consumer1:11200
    # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
    prefer-ip-address: true
    # 设置心跳间隔时间, 默认 30 S
    lease-renewal-interval-in-seconds: 2
    # 设置服务过期时间配置,超过这个时间没有接收到心跳EurekaServer就会将这个实例剔除, 默认 90 S
    lease-expiration-duration-in-seconds: 5
  client:
    service-url:
      register-with-eureka: true
      fetch-registry: true
      # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
#--------------------------------- Eureka 配置  end  ---------------------------------
```

**Step-4: 创建启动类 SpringCloudStreamConsumer1Application**

```java
/**
 * Spring Cloud Stream 消费者1启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@EnableEurekaClient
@SpringBootApplication
public class SpringCloudStreamConsumer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamConsumer1Application.class, args);
    }
}
```

**Step-5: 创建监听类 Consumer1MessageListener**

```java
/**
 * 消费者监听器
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@Slf4j
@Component
@EnableBinding(Sink.class)
public class Consumer1MessageListener {

    @Value("${server.port}")
    private String serverPort;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        log.info("Server.Port:{} , Consumer1MessageListener receive message :{}", serverPort, message.getPayload());
    }

}
```

### 创建消息消费者2

* 具体实现步骤和 创建消息消费者2 是一致的。



## 单元测试

我们在上面的基础上，重新新增了两个服务进行单元测试，分别是 `WebSocket` 和 `Gateway` 两个微服务 和 前端项目。

* 服务信息如下：

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401211627925.png" alt="image-20240121162736884" style="zoom:100%;float:left" />

* Eureka 服务信息如下：

  ![image-20240121163236518](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401211632581.png)

### 消费者处于同一个组

> 当两个消费者处于同一个组：`group: consumer1`，这两个组中只会有其中一个服务会接收到消息，不会重复消费。

* `bootstrap.yml`

  ```yaml
        # 服务的整合处理
        bindings:
          # 通道名称
          input:
            # 表示使用的 exchange 名称定义
            destination: stream-exchange
            # 设置消息类型
            content-type: application/json
            # 设置绑定消息服务的具体设置
            binder: defaultRabbit
            group: consumer1
  ```

  

* 当两个消费者处于同一个组：`group: consumer1`

![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401211639065.gif)

### 消费者处于不同的组

> 当两个消费者处于不同的个组
>
> * consumer1 处于 `group:consumer1`
> * consumer2 处于 `group:consumer2`
> * 这两个组中只都会接收到消息，不会重复消费。

* 当两个消费者处于不同的组

  ![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401220922909.gif)
