# Spring Cloud Eureka

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下8个服务当中（cluster-集群、singleton-单机）：
>
> * [03-spring-cloud-cluster-eureka-order-3500](https://github.com/wicksonZhang/Spring-Cloud/tree/main/03-spring-cloud-cluster-eureka-order-3500)
> * [03-spring-cloud-cluster-eureka-payment-3600](https://github.com/wicksonZhang/Spring-Cloud/tree/main/03-spring-cloud-cluster-eureka-payment-3600)
> * [03-spring-cloud-cluster-eureka-payment-3700](https://github.com/wicksonZhang/Spring-Cloud/tree/main/03-spring-cloud-cluster-eureka-payment-3700)
> * [03-spring-cloud-cluster-eureka-server-3300](https://github.com/wicksonZhang/Spring-Cloud/tree/main/03-spring-cloud-cluster-eureka-server-3300)
> * [03-spring-cloud-cluster-eureka-server-3400](https://github.com/wicksonZhang/Spring-Cloud/tree/main/03-spring-cloud-cluster-eureka-server-3400)
> * [03-spring-cloud-singleton-eureka-order-3100](https://github.com/wicksonZhang/Spring-Cloud/tree/main/03-spring-cloud-singleton-eureka-order-3100)
> * [03-spring-cloud-singleton-eureka-payment-3200](https://github.com/wicksonZhang/Spring-Cloud/tree/main/03-spring-cloud-singleton-eureka-payment-3200)
> * [03-spring-cloud-singleton-eureka-server-3000](https://github.com/wicksonZhang/Spring-Cloud/tree/main/03-spring-cloud-singleton-eureka-server-3000)

## 基础概念

### Eureka 是什么？

* `Eureka` 是 `Netflix` 团队开发的一个注册中心组件，`Eureka` 在 `Spring Cloud` 体系中主要的作用是允许服务互相发现和通信。

* 在 `Eureka` 组件中有如下两个比较重要的角色：

  * `Eureka Server`：`Eureka` 服务端负责维护服务实例的注册表。所有的服务实例都会向 `Eureka Server` 注册自己的位置和状态信息，并定期更新。
  * `Eureka Client`： `Eureka` 客户端会向 `Eureka` 服务端注册自己，并从服务器获取其他服务的信息，以便实现服务之间的通信。

* 如下图是 `Spring Cloud` 体系中 `Eureka` 组件在其中发挥的作用：

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202312281455088.png" alt="image-20231228145552037" style="zoom:80%;float:left" />

* 最后我们通过一个例子总结一下，想象一下你入职一家新公司，就像使用 Eureka 的服务发现机制一样。当你需要办理入职手续或者与其他部门的同事交流时，你并不需要自己去了解每个人的位置和联系方式。相反，你可以通过公司的内部联系系统（就像 Eureka 服务器一样），在其中找到你需要的同事或资源的位置信息。这种服务发现的机制让你能够更迅速地定位和联系到公司内部的各个部门或同事，类似于使用 Eureka 找到服务实例，你可以轻松地找到需要的人或资源，然后进行有效的沟通和协作。

  

### Eureka 的优缺点

**优点**

* **高可用性**：`Eureka` 服务器本身支持高可用性配置，可以部署多个实例以防止单点故障，提高系统的稳定性。
* **易于集成：** Eureka 对于许多云服务和框架都有良好的集成性，如与Spring Cloud等主流框架配合使用。
* **轻量级：** 相对较小的内存占用和开销，适用于各种规模的应用。

**缺点**

* **单点故障**：Eureka 在单个实例故障时可能导致整个服务发现系统的不可用。虽然使用集群可以避免这个问题，但是这也是一个潜在的风险。
* **复杂性管理**：随着服务的增加，管理多个 `Eureka` 服务器和大量实例可能会变得复杂。



### Eureka 解决了什么问题

* **服务注册和发现**

  * 在上面章节已经讲过，Eureka 允许服务实例自动注册到注册中心报告自己的位置和状态。这使得服务通信之间更加简单。
  * 例如我们上一章节的案例，我们不使用 `Eureka`会产生的问题，我们直接就将相关的服务地址写死了。

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202312281543801.png" alt="image-20231228154320764" style="zoom:100%;float:left" />

  

* **服务健康检查**

  * Eureka 可以监控服务实例的健康状态，定期发送心跳来告知注册中心其状态，这有助于实现自动发现故障实例并剔除出服务池，确保系统的可靠性。
  * 我们还是以上面的案例为例，如果 服务 `02-spring-cloud-payment-2100` 出现故障但是在服务 `02-spring-cloud-order-2000` 不知道，这会导致服务的不可靠性。



* **简化了服务间通信** 
  * 通过 Eureka 注册中心，服务之间的通信变得更加简单，服务实例的位置信息可以动态地从注册中心获取，而不需要硬编码或手动配置服务地址。



## Eureka 单机操作

* 实现需求
  * 我们将上一章节的 订单、支付微服务注册到 `Eureka` 当中

* 实现思路

  * Step-1：创建注册中心 `03-spring-cloud-singleton-eureka-server-3000`
  * Step-2：创建订单服务 `03-spring-cloud-singleton-eureka-order-3100`
  * Step-3：创建支付服务 `03-spring-cloud-singleton-eureka-payment-3200`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202312291434362.png" alt="image-20231229143406316" style="zoom:100%;float:left" />

### 创建注册中心

* 注册中心服务名：`03-spring-cloud-singleton-eureka-server-3000`
* 实现步骤
  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类

* 导入 `pom.xml` 依赖

```xml
    <dependencies>
        <!-- 公共依赖包 -->
        <dependency>
            <groupId>cn.wickson.cloud</groupId>
            <artifactId>01-spring-cloud-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- 服务注册中心的服务端 eureka-server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
```

* 修改 `application.properties` 文件

```properties
# Eureka服务端口
server.port=3000
# 单机版名称
eureka.instance.hostname=localhost
# 是否向注册中心注册自己
eureka.client.register-with-eureka=false
# 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
eureka.client.fetch-registry=false
# 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
```

* 创建主启动类

```java
/**
 * Eureka 服务端-单机节点注册中心
 *
 * @author ZhangZiHeng
 * @date 2023-12-28
 */
@EnableEurekaServer
@SpringBootApplication
public class SpringCloudSingletonEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSingletonEurekaServerApplication.class, args);
    }

}
```

### 创建订单服务

* 订单服务名：`03-spring-cloud-singleton-eureka-order-3100`
* 实现步骤
  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类
* 导入 `pom.xml` 依赖

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

* 修改 `application.properties` 文件

```properties
# 服务端口
server.port=3100
# 应用名称
spring.application.name=spring-cloud-eureka-order-3100
# 是否向注册中心注册自己
eureka.client.register-with-eureka=true
# 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
eureka.client.fetch-registry=true
# 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
eureka.client.serviceUrl.defaultZone=http://localhost:3000/eureka
```

* Step-3：创建主启动类

```java
/**
 * 订单服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2023-12-28
 */
@EnableEurekaClient
@SpringBootApplication
public class SpringCloudSingletonEurekaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSingletonEurekaOrderApplication.class, args);
    }

}
```

### 创建支付服务

* 订单服务名：`03-spring-cloud-singleton-eureka-payment-3200`
* 实现步骤
  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类
* 导入 `pom.xml` 依赖

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

* 修改 `application.properties` 文件

```properties
# 服务端口
server.port=3200
# 应用名称
spring.application.name=spring-cloud-eureka-payment-3200
# 是否向注册中心注册自己
eureka.client.register-with-eureka=true
# 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
eureka.client.fetch-registry=true
# 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
eureka.client.serviceUrl.defaultZone=http://localhost:3000/eureka
```

* Step-3：创建主启动类

```java
/**
 * 支付服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2023-12-28
 */
@EnableEurekaClient
@SpringBootApplication
public class SpringCloudSingletonEurekaPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSingletonEurekaPaymentApplication.class, args);
    }

}
```

### 单元测试

* 当我们启动 注册中心、订单服务、支付服务 时，在 `Eureka` 中观察服务实例情况

* 访问 `http://localhost:3000/`

  ![image-20231229143659837](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202312291436891.png)

  

## Eureka 集群操作

> 上面我们已经说过了我们部署单机节点可能会出现节点故障，针对这个问题我们采用的方式是进行集群化部署。

* 实现需求
  * 我们将上一章节的 订单、支付微服务注册到 `Eureka` 当中

* 实现思路

  * Step-1：创建注册中心1 `03-spring-cloud-cluster-eureka-server-3300`
  * Step-2：创建注册中心2 `03-spring-cloud-cluster-eureka-server-3400`
  * Step-3：创建订单服务 `03-spring-cloud-cluster-eureka-order-3500`
  * Step-4：创建支付服务1 `03-spring-cloud-cluster-eureka-payment-3600`
  * Step-5：创建支付服务2 `03-spring-cloud-cluster-eureka-payment-3700`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401021602600.png" alt="image-20240102160234554" style="zoom:100%;float:left" />



### 创建注册中心1

* 注册中心服务名：`03-spring-cloud-cluster-eureka-server-3300`
* 实现步骤
  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类

* Step-1：导入 `pom.xml` 依赖

```xml
    <dependencies>
        <!-- 公共依赖包 -->
        <dependency>
            <groupId>cn.wickson.cloud</groupId>
            <artifactId>01-spring-cloud-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- 服务注册中心的服务端 eureka-server -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
    </dependencies>
```

* Step-2：修改 `application.properties` 文件

```properties
# Eureka服务端口
server.port=3300
# 集群版名称
eureka.instance.hostname=eureka3300.com
# 是否向注册中心注册自己
eureka.client.register-with-eureka=false
# 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
eureka.client.fetch-registry=false
# 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
eureka.client.serviceUrl.defaultZone=http://eureka3400.com:3400/eureka/
```

* Step-3：创建主启动类

```java
/**
 * Eureka 服务端1-集群节点注册中心
 *
 * @author ZhangZiHeng
 * @date 2024-01-02
 */
@EnableEurekaServer
@SpringBootApplication
public class SpringCloudClusterEurekaServer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudClusterEurekaServer1Application.class, args);
    }

}
```

**注意：由于我们采用的集群配置，其中 `properties` 文件中的集群名称对应着 `hosts` 映射文件，需要修改为如下配置：**

* 路径：`C:\Windows\System32\drivers\etc`

```
# Eureka
127.0.0.1 eureka3300.com
127.0.0.1 eureka3400.com
```



### 创建注册中心2

* 注册中心服务名： `03-spring-cloud-cluster-eureka-server-3400`

* 实现步骤：同上述 `创建注册中心1` 一致

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401021610787.png" alt="image-20240102161027760" style="zoom:100%;float:left" />



### 创建订单服务

* 注册中心服务名：`03-spring-cloud-cluster-eureka-order-3500`
* 实现步骤
  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类
  * Step-4：修改配置文件
* Step-1：导入 `pom.xml` 依赖

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
server.port=3500
# 应用名称
spring.application.name=spring-cloud-cluster-eureka-order
# 是否向注册中心注册自己
eureka.client.register-with-eureka=true
# 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
eureka.client.fetch-registry=true
# 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
eureka.client.serviceUrl.defaultZone=http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka

# 默认就是应用名称:端口，设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-order:3500
eureka.instance.instance-id=spring-cloud-cluster-eureka-order:3500
# 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
eureka.instance.prefer-ip-address=true
```

* Step-3：创建主启动类

```java
@EnableEurekaClient
@SpringBootApplication
public class SpringCloudClusterEurekaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudClusterEurekaOrderApplication.class, args);
    }

}
```

* Step-4：修改 `RestTemplateConfig` 文件

```java
/**
 * RestTemplate 配置
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
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

**以下内容需要注意：**

1. Eureka 集群注册节点在 `application.properties` 中需要配置两个集群服务

   ```properties
   # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
   eureka.client.serviceUrl.defaultZone=http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
   ```

2. Eureka 服务实例需要在 `application.properties` 中配置唯一标识

   ```properties
   # 默认就是应用名称:端口，设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-order:3500
   eureka.instance.instance-id=spring-cloud-cluster-eureka-order:3500
   # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
   eureka.instance.prefer-ip-address=true
   ```

   <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401021620917.png" alt="image-20240102162025882" style="zoom:100%;float:left" />

3. `RestTemplateConfig.java` 文件中需要添加 `@LoadBalanced` 做负载均衡。由于我们从订单服务调用两个支付微服务。所以需要进行负载均衡

   ```java
   	@Bean
       @LoadBalanced
       public RestTemplate restTemplate() {
           return new RestTemplate();
       }
   ```

   

### 创建支付服务1

* 注册中心服务名：`03-spring-cloud-cluster-eureka-payment-3600`
* 实现步骤
  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类
  * Step-4：编写业务类
* Step-1：导入 `pom.xml` 依赖

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
server.port=3600
# 应用名称
spring.application.name=spring-cloud-cluster-eureka-payment
# 是否向注册中心注册自己
eureka.client.register-with-eureka=true
# 表示自己就是注册中心，职责是维护服务实例，并不需要去检索服务
eureka.client.fetch-registry=true
# 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
eureka.client.serviceUrl.defaultZone=http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka

# 设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-payment:3600
eureka.instance.instance-id=spring-cloud-cluster-eureka-payment:3600
# 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
eureka.instance.prefer-ip-address=true
```

* Step-3：创建主启动类

```java
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"cn.wickson.cloud"})
public class SpringCloudClusterEurekaPayment1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudClusterEurekaPayment1Application.class, args);
    }

}
```

* Step-4：编写业务类

  * 具体业务详见 `微服务-基础知识` 那一章节内容

* **以下内容需要注意：**

  1. Eureka 集群注册节点在 `application.properties` 中需要配置两个集群服务

  ```properties
  # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
  eureka.client.serviceUrl.defaultZone=http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
  ```

  2. Eureka 服务实例需要在 `application.properties` 中配置唯一标识

  ```properties
  # 默认就是应用名称:端口，设置Eureka服务实例的唯一标识为 spring-cloud-cluster-eureka-order:3500
  eureka.instance.instance-id=spring-cloud-cluster-eureka-order:3500
  # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
  eureka.instance.prefer-ip-address=true
  ```

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401021620917.png" alt="image-20240102162025882" style="zoom:100%;float:left" />

  3. spring.application.name 两个支付服务的配置名称一样

  ```properties
  spring.application.name=spring-cloud-cluster-eureka-payment
  ```



### 创建支付服务2

* 注册中心服务名：`03-spring-cloud-cluster-eureka-payment-3700`

* 实现步骤：同上述 `创建支付订单1` 一致

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401021637829.png" alt="image-20240102163714796" style="zoom:100%;float:left" />



### 单元测试

* 我们测试集群环境是否搭建成功

![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031409099.gif)



## Eureka 服务发现

* 具体信息可以参考博文：https://developer.aliyun.com/article/1133813



## Eureka 自我保护

* 自我保护机制是为了避免因网络抖动或分区而影响服务注册中心的整体可用性，保证服务的稳定性和可靠性。例如：当服务主线网络波动时，导致注册中心中的部分节点失去联系，这会让注册中心任务服务已经离线，但并不立即将其从注册表中移除，从而避免了在网络分区期间误删正常服务的情况。

* 当访问注册中心时，如果出现如下内容表示 `Eureka` 开启了自我保护机制

  ![image-20240102165640923](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401021656958.png)

* `Eureka` 自我保护机制可以参考这篇博文：https://www.cnblogs.com/linjiqin/p/10090000.html