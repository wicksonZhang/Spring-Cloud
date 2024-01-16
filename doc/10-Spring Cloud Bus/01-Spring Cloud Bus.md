# Spring Cloud Bus

## 基础概念

### Spring Cloud Bus 解决了什么问题？

​		Spring Cloud Bus 主要解决了分布式系统配置的动态刷新问题。例如，服务 `serviceA` 和服务 `serviceB` 都有一个名为 `application.properties` 的配置文件，例如数据库连接信息、日志级别、特性开关等。当这些配置发生变化时，需要通知所有相关的服务实例更新配置，以确保它们使用最新的配置信息。



### Spring Cloud Bus 是什么？

​		Spring Cloud Bus 是 Spring Cloud 中的一个组件，用于实现分布式系统中的消息总线功能，一般与 `Spring Cloud Config` 结合进行使用。它允许在微服务架构中通过消息代理（消息队列）实现服务之间的通信与写作，主要功能和特性如下：

* **配置刷新：**`Spring Cloud Bus` 主要用于解决分布式系统中的配置动态刷新问题。当配置中心的配置发生变化是无须重启服务进行刷新，而是通过 `Spring Cloud Bus` 进行消息广播通知相关的微服务实例。
* **消息代理：**`Spring Cloud Bus` 使用消息代理（`RabbitMQ`、`Kafka`）作为传输工具。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401161059844.png" alt="image-20240116105916800" style="zoom:90%;float:left" />



### Spring Cloud Bus 优缺点

> 优缺点可能就是我们上面所说的一些特性

**优点**

1. **配置动态刷新**：Spring Cloud Bus 的主要优势是解决了分布式系统中的配置动态刷新问题。它允许在不重启微服务的情况下，通过消息广播的方式将最新的配置信息传递给所有相关的微服务实例。
2. **事件传播：** 除了配置刷新，Spring Cloud Bus 还支持自定义事件的传播。
3. **消息代理支持：** Spring Cloud Bus 使用消息代理（如 `RabbitMQ`、Kafka 等）作为传输工具，确保消息的可靠性和一致性。



**缺点**

1. **引入复杂性：** 在小规模和简单的应用中，引入 Spring Cloud Bus 可能会显得过于复杂。
2. **依赖消息代理：** Spring Cloud Bus 的实现依赖消息代理，如 `RabbitMQ` 或 Kafka。



## 安装 `RabbitMQ`

> 本次由于 `Spring Cloud Config` 需要只是消息代理作为传输工具，所以需要安装 `RabbitMQ`.
>
> 安装环境：Windows 11

**安装步骤**

```tex
1. Step-1：安装 Erlang
2. Step-2：安装 RabbitMQ
```

**Step-1：安装 Erlang**

* 下载地址：http://erlang.org/download/otp_win64_21.3.exe

**Step-1：安装 `RabbitMQ`**

* 下载地址：https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.7.14/rabbitmq-server-3.7.14.exe



## 启动 `RabbitMQ`

**操作步骤**

```tex
1. Step-1：进入 RabbitMQ 安装目录, 执行启动命令
2. Step-2：访问RabbitMQ
```

**Step-1：进入 `RabbitMQ` 安装目录，执行启动命令**

* 启动命令：`rabbitmq-plugins enable rabbitmq_management`

```
D:\software\RabbitMQ Server\rabbitmq_server-3.7.14\sbin>rabbitmq-plugins enable rabbitmq_management
Enabling plugins on node rabbit@DESKTOP-KRH17BU:
rabbitmq_management
The following plugins have been configured:
  rabbitmq_management
  rabbitmq_management_agent
  rabbitmq_web_dispatch
Applying plugin configuration to rabbit@DESKTOP-KRH17BU...
The following plugins have been enabled:
  rabbitmq_management
  rabbitmq_management_agent
  rabbitmq_web_dispatch

started 3 plugins.

D:\software\RabbitMQ Server\rabbitmq_server-3.7.14\sbin>
```

* 执行完命令之后会出现如下图形化界面

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401161140812.png" alt="image-20240116114011757" style="zoom:100%;float:left" />



**Step-2：访问 `RabbitMQ`**

* 访问地址：http://localhost:15672
* 账号和密码：guest、guest

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401161143507.png" alt="image-20240116114307458" style="zoom:100%;float:left" />



## 具体操作

  











