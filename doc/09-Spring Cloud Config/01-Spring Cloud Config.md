# Spring Cloud Config

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下服务当中：
>
> 

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

  * 我们将

* 实现思路

  * Step-1：创建网关服务 `09-spring-cloud-gateway-server-9000`

* 代码结构



### 服务端操作







### 客户端操作

















