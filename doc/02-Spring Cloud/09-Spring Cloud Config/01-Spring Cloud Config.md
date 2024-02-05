# Spring Cloud Config

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下服务当中：
>
> * [10-spring-cloud-config-client1-11000](https://github.com/wicksonZhang/Spring-Cloud/tree/main/10-spring-cloud-config-client1-11000)
> * [10-spring-cloud-config-server-10000](https://github.com/wicksonZhang/Spring-Cloud/tree/main/10-spring-cloud-config-server-10000)
> * [10-spring-cloud-config](https://github.com/wicksonZhang/Spring-Cloud/tree/main/10-spring-cloud-config)

## Spring Cloud Config 基础概念

> 官网地址：https://docs.spring.io/spring-cloud-config/docs/current/reference/html/

### Spring Cloud Config 解决了什么问题？

​		在传统的开发和部署过程中，配置信息通常会被打包到服务的可执行 `JAR` 文件中。当需要修改某个服务的配置时，就需要重新编译和部署整个服务，这在生产环境中可能是一个繁琐且潜在风险较高的操作。

​		微服务架构中，每个微服务通常都有自己的配置文件，这些配置文件包含了服务的各种属性、连接信息、端口号等。而在 `Spring Cloud Config` 中通过将配置文件集中存储在一个外部的配置服务器上，而不是打包到每个微服务的 `JAR` 文件中，就解决了这个问题。



### Spring Cloud Config 是什么？

* `Spring Cloud Config`：Spring Cloud Config 提供了集中式的外部配置管理服务，包含了服务端和客户端两个部分。允许将应用程序的配置信息存储在中心位置，便于集中管理和动态更新。

  <img src="https://miro.medium.com/v2/resize:fit:700/1*cTPRDoBveeNbz9QAi5XJJg.png" alt="img" style="zoom:100%;float:left" />



### Spring Cloud Config 优缺点

> 有时候优点可以变为缺点，缺点也可以变为优点。

**优点**

1. **集中式管理：**Spring Cloud Config允许将配置文件集中存储在配置服务器上。这样，各个微服务实例可以通过配置服务器获取其运行所需的配置信息。
2. **版本控制：**配置文件可以与版本控制系统（如Git）集成，可以在不同环境中轻松管理和跟踪配置的变更。
3. **动态刷新：** Spring Cloud Config支持配置的动态刷新，这意味着在运行时修改配置文件后，应用程序无需重新启动即可获取新的配置。



**缺点**

1. **单点故障：**默认情况下，Config Server 是一个中心化的服务。如果配置服务器不可用，所有依赖它的微服务都将受到影响。
2. **安全性问题：** 配置文件中可能包含敏感信息，如数据库密码、API密钥等。



## Spring Cloud Config 具体操作

> 如果出现端口被占用请查看博文：https://blog.csdn.net/weixin_46709007/article/details/125140872

* 实现需求

  * 首先，我们创建配置文件，将配置文件部署在 `github` 上。
  * 然后，我们创建服务端操作，通过访问服务端读取在 `github` 上的配置文件。
  * 最后，我们创建客户端操作，通过配置服务端读取配置文件。

* 实现思路

  * Step-1：创建配置文件 `10-spring-cloud-config`
  * Step-2：创建配置服务端 `10-spring-cloud-config-server-10000`
  * Step-3：创建配置客户端 `10-spring-cloud-config-client-11000`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401152040652.png" alt="image-20240115204041536" style="zoom:100%;float:left" />

### 准备工作

我们准备工作有如下两个操作

1. 创建配置文件`10-spring-cloud-config` (`config-dev.yml、config-prod.yml、config-uat.yml`)
2. 修改 `hosts` 映射文件
3. 将配置文件 `10-spring-cloud-config` 上传至 `github`

* **Step-1：创建配置文件`10-spring-cloud-config`**
  * 其中以 `config-dev.yml` 配置文件为例，其他都是一样

```yaml
config:
  info: "master branch, 10-spring-cloud-config/config-dev.yml version=1"
```

* **Step-2：修改 `hosts` 映射文件**

```
# Spring Cloud Config
127.0.0.1 config10000.com
```

* **Step-3：将配置文件 `10-spring-cloud-config` 上传至 `github`**

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401152051723.png" alt="image-20240115205108695" style="zoom:100%;float:left" />



### 服务端操作

**实现步骤**

```tex
1. Step-1：导入 `pom.xml` 依赖
2. Step-2：修改 `application.yml` 文件
3. Step-3：创建主启动类
4. 测试
```

**Step-1：导入 `pom.xml` 依赖**

```xml
<dependencies>
    <!-- 公共依赖 -->
    <dependency>
        <groupId>cn.wickson.cloud</groupId>
        <artifactId>01-spring-cloud-common</artifactId>
        <version>1.0-SNAPSHOT</version>
        <exclusions>
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </exclusion>
        </exclusions>
    </dependency>

    <!-- Spring Cloud Config 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-server</artifactId>
    </dependency>

    <!-- Spring Cloud netflix 服务-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

**Step-2：修改 `application.yml` 文件**

```yaml
# 服务端口
server:
  port: 10000
# 应用名称
spring:
  application:
    name: spring-cloud-config-server
  cloud:
    config:
      server:
        git:
          uri: https://github.com/wicksonZhang/Spring-Cloud.git
          # 搜索目录
          search-paths:
            - 10-spring-cloud-config
      # 读取分支
      label: main


#--------------------------------- Eureka 配置 start ---------------------------------
eureka:
  instance:
    hostname: spring-cloud-config-server
    # 设置Eureka服务实例的唯一标识为 spring-cloud-config-server:10000
    instance-id: spring-cloud-config-server:10000
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

**Step-3：创建服务端主启动类**

```java
/**
 * Spring Cloud Config 服务端-启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-15
 */
@EnableEurekaClient
@EnableConfigServer
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }

}
```

**Step-4：测试**

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401152058888.gif" alt="动画" style="zoom:100%;float:left" />



**注意：我们这里可以配置一下读取规则**

* 具体配置细节参考官网配置：[Spring Cloud Config](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_quick_start)，我们选取如下配置

```yaml
# label: 分支（branch）
# application: 服务名
# profile: 环境（dev/test/prod）

/{label}/{application}-{profile}.yml
```



### 客户端操作

**实现步骤**

```
1. Step-1：导入 `pom.xml` 依赖
2. Step-2：修改 `application.yml` 文件
3. Step-3：创建主启动类
4. Step-4：创建控制类
5. Step-5：测试
```

**Step-1：导入 `pom.xml` 依赖**

```xml
<dependencies>
    <!-- 公共依赖 -->
    <dependency>
        <groupId>cn.wickson.cloud</groupId>
        <artifactId>01-spring-cloud-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <!-- Spring Cloud Config 依赖 -->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-config-client</artifactId>
    </dependency>

    <!-- Spring Cloud netflix 服务-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
</dependencies>
```

**Step-2：修改 `bootstrap.yml` 文件**

* 注意：我们这次使用的配置文件是 `bootstrap.yml` , 因为 `bootstrap.yml` 的优先级比 `application.yml` 高

```yaml
# 服务端口
server:
  port: 11000
# 应用名称
spring:
  application:
    name: spring-cloud-config-client
  cloud:
    # 配置文件读取地址：http://config10000.com/main/config-dev.yml
    # 配置文件读取规则：/main分支/配置文件名称+配置文件后缀.yml
    config:
      # 分支名称
      label: main
      # 配置文件名称
      name: config
      # 读取配置文件后缀
      profile: dev
      # 配置中心地址
      uri: http://localhost:10000


#--------------------------------- Eureka 配置 start ---------------------------------
eureka:
  instance:
    hostname: spring-cloud-config-client
    # 设置Eureka服务实例的唯一标识为 spring-cloud-config-client1:11000
    instance-id: spring-cloud-config-client1:11000
    # 设置Eureka客户端是否偏好使用IP地址注册到Eureka服务器，而不是使用主机名
    prefer-ip-address: true
  client:
    service-url:
      register-with-eureka: true
      fetch-registry: true
      # 设置与eureka server交互的地址查询服务和注册服务都需要依赖这个地址
      defaultZone: http://eureka3300.com:3300/eureka,http://eureka3400.com:3400/eureka
#--------------------------------- Eureka 配置  end  ---------------------------------

#--------------------------------- config 客户端配置暴露监控端点 start ---------------------------------
management:
  endpoints:
    web:
      exposure:
        include: "*"
#--------------------------------- config 客户端配置暴露监控端点 end   ---------------------------------
```

**Step-3：创建客户端主启动类**

```java
/**
 * Spring Cloud Config 客户端-启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-15
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudConfigClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigClientApplication.class, args);
    }

}
```

**Step-4：创建控制类**

```java
/**
 * 客户端控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-15
 */
@Slf4j
@Validated
@RestController
@RefreshScope
@RequestMapping("/config-client")
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config-info")
    public String getConfigInfo() {
        return configInfo;
    }

}
```

**Step-5：测试**

* 我们目前测试的是 `config-dev.yml`
* 访问地址：http://localhost:11000/config-client1/config-info

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401152122706.gif" alt="动画" style="float:left" />



### 动态更新

* 我们在客户端已经将动态更新配置好了，所以我们进行测试。
* 注意：我们更新完 `github` 配置文件之后，需要手动激活客户端配置。

```cmake
C:\Users\wicks>curl -X POST "http://localhost:11000/actuator/refresh"
["config.client.version","config.info"]
C:\Users\wicks>
```

![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401152159890.gif)

