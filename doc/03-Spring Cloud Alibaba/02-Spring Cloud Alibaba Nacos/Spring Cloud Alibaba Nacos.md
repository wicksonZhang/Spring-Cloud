#  Spring Cloud Alibaba Nacos

> 代码地址：https://github.com/wicksonZhang/Spring-Cloud-Alibaba

## 基础概念

### Nacos 解决了什么问题

Nacos 主要解决了微服务中服务注册与发现和服务配置问题，在 `Spring Cloud` 中如果需要实现服务注册与发现和配置需要使用到 `Eureka` 、`Spring Cloud Config` 和 `Spring Cloud Bus` 。但现在只需要使用一个 `Nacos` 就可以解决问题。

* **服务注册与发现**

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231110918.png" alt="service-discovery-0257df233735d6e39adc8e8a0fd27f86" style="zoom:33%;float:left" />

* **服务配置**

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231106290.png" alt="service-config" style="zoom: 22.5%;float:left" />



### Nacos 是什么

> 官网地址：https://nacos.io/
>
> Github地址：https://github.com/alibaba/nacos

Nacos：`Dynamic Naming and Configuration Service` 的首字母简称，一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台。它由阿里巴巴开源团队维护，旨在为现代微服务架构提供一套集成的解决方案。



### 注册中心对比

以下是 `Nacos` 相对于 `Eureka` 的一些优点：

- **动态配置管理：** `Nacos` 不仅提供了服务注册与发现的功能，还集成了动态配置管理。
- **健康检查更灵活：** `Nacos` 提供了更灵活的健康检查机制，支持不同的健康检查方式和策略。
- **更强大的集群管理：** `Nacos` 在集群管理方面更为强大，支持多数据中心、多命名空间的配置管理和服务发现。

| 特性 / 注册中心    | Nacos                         | Eureka                                | Zookeeper                      | Consul                         |
| ------------------ | ----------------------------- | ------------------------------------- | ------------------------------ | ------------------------------ |
| **服务发现**       | ✔ 支持                        | ✔ 支持                                | ✔ 支持                         | ✔ 支持                         |
| **配置管理**       | ✔ 支持                        | ✖ 不支持（需结合Spring Cloud Config） | ✖ 不支持（可结合其他配置中心） | ✖ 不支持（可结合其他配置中心） |
| **多数据中心支持** | ✔ 支持                        | ✔ 支持                                | ✖ 不支持                       | ✔ 支持                         |
| **动态 DNS 服务**  | ✔ 支持                        | ✖ 不支持                              | ✖ 不支持                       | ✖ 不支持                       |
| **健康检查**       | ✔ 支持                        | ✖ 不支持（需结合Spring Cloud Health） | ✔ 支持                         | ✔ 支持                         |
| **事件监听机制**   | ✔ 支持                        | ✔ 支持                                | ✔ 支持                         | ✔ 支持                         |
| **支持协议**       | HTTP、DNS、gRPC等             | HTTP                                  | 客户端/服务器模型，可扩展协议  | HTTP，支持DNS                  |
| **一致性协议**     | CP + AP                       | AP（强调可用性和分区容忍性）          | CP 协议（强一致性）            | CP + AP协议                    |
| **生态系统整合**   | Spring Cloud Alibaba 生态系统 | Spring Cloud 生态系统                 | 社区广泛使用，适用于多种场景   | Spring Cloud 生态系统          |
| **开发维护**       | 阿里巴巴维护，活跃的社区      | Netflix 维护，社区逐渐减少            | Apache 维护，社区广泛使用      | HashiCorp 维护，社区活跃       |



## Nacos 安装启动

> 下载地址：https://github.com/alibaba/nacos
>
> 下载版本：nacos-server-2.0.1.zip

* 下载完成之后，解压到指定目录：

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231402963.png" alt="image-20240123140212936" style="zoom:100%;float:left" />



* 启动命令如下，如果看到了对应图标且不报错说明启动成功
  * 默认启动是集群模式：startup.cmd
  * 如果需要指定为单机模式：startup.cmd -m standalone

```cmake
D:\software\nacos\bin>startup.cmd -m standalone
"nacos is starting with standalone"
```



* 访问地址：http://localhost:8848/nacos ，用户名和密码都是 nacos

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231409649.png" alt="image-20240123140904614" style="zoom:100%;float:left" />



## Nacos 单机操作

> 代码地址如下：
>
> * [02-spring-cloud-alibaba-nacos-consumer-2300](https://github.com/wicksonZhang/Spring-Cloud-Alibaba/tree/main/02-spring-cloud-alibaba-nacos-consumer-2300)
> * [02-spring-cloud-alibaba-nacos-producer1-2100](https://github.com/wicksonZhang/Spring-Cloud-Alibaba/tree/main/02-spring-cloud-alibaba-nacos-producer1-2100)
> * [02-spring-cloud-alibaba-nacos-producer2-2200](https://github.com/wicksonZhang/Spring-Cloud-Alibaba/tree/main/02-spring-cloud-alibaba-nacos-producer2-2200)

### 实现需求

```
1. 当用户访问消费者服务接口时，消费者服务会利用相应的路由将请求分派给特定的服务提供者。
2. 服务提供者则会根据配置中心的设定，检索相关内容，并将消息传递回消费者服务。
```

### 实现结果

* 访问地址：http://localhost:2300/consumer/getProducer

![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231550837.gif)

### 实现步骤

```
1. 创建 提供者1 服务，并将其注册到Nacos中。
2. 创建 提供者2 服务，并将其注册到Nacos中。
3. 创建 消费者 服务，并将其注册到Nacos中，通过 Nacos 的负载均衡将消费者的请求转发到服务提供者。
```

* 相关代码结构

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231552005.png" alt="image-20240123155245973" style="zoom:100%;float:left" />

### 服务提供者1

**实现步骤**

```tex
Step-1: 创建 服务提供者1 02-spring-cloud-alibaba-nacos-producer1-2100
Step-2: 导入 pom.xml 依赖
Step-3: 创建 bootstrap.yml、application.yml
Step-4: 创建启动类 SpringCloudAlibabaProducer1Application
Step-5: 创建控制类 Producer1Controller
```



**Step-2: 导入 pom.xml 依赖**

```xml
<dependencies>
    <!-- 引入公共依赖 common -->
    <dependency>
        <groupId>cn.wickson.cloud.alibaba</groupId>
        <artifactId>01-spring-cloud-alibaba-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <!-- Spring Cloud alibaba nacos -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>

    <!-- Spring Cloud alibaba config -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
</dependencies>
```



**Step-3: 创建 bootstrap.yml、application.yml**

* `bootstrap.yml`： 主要用于应用程序的引导阶段，会优先被加载。

```yml
# ------------------------- 应用端口 -------------------------
server:
  port: 2100

spring:
  application:
    name: spring-cloud-alibaba-nacos-producer
  cloud:
# -------------------------------- nacos地址 ---------------------------------
    nacos:
      discovery:
        server-addr: localhost:8848 # nacos 服务注册中心地址
      config:
        server-addr: localhost:8848 # nacos 服务配置中心地址
        file-extension: yaml
# -------------------------------- nacos地址 ---------------------------------

#--------------------------------- 客户端配置暴露监控端点 start ---------------------------------
management:
  endpoints:
    web:
      exposure:
        include: "*"
#--------------------------------- 客户端配置暴露监控端点 end   ---------------------------------
```

* `application.yml`：主要用于应用程序的主配置文件，如数据库连接、服务端口。

```yml
spring:
  profiles:
    active: dev # 开发环境
```



**Step-4: 创建启动类 SpringCloudAlibabaProducer1Application**

```java
/**
 * Spring Cloud Alibaba 生产者启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-23
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudAlibabaProducer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaProducer1Application.class, args);
    }

}
```



**Step-5: 创建控制类 Producer1Controller**

```java
@Slf4j
@Validated
@RestController
@RefreshScope // 支持 Nacos 动态刷新配置
@RequestMapping("/producer")
public class Producer1Controller {

    @Value("${server.port}")
    private Integer serverPort;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping(value = "/nacos")
    public String getProducerConfig() {
        return "The producer1 server.port: " + serverPort + " registered to nacos.The producer1 config：" + configInfo;
    }

}
```



### 服务提供者2

**实现步骤**

```tex
Step-1: 创建 服务提供者1 02-spring-cloud-alibaba-nacos-producer2-2200
Step-2: 导入 pom.xml 依赖
Step-3: 创建 bootstrap.yml、application.yml
Step-4: 创建启动类 SpringCloudAlibabaProducer2Application
Step-5: 创建控制类 Producer2Controller
```

**操作与服务提供者保持一致**



### 服务消费者

**实现步骤**

```tex
Step-1: 创建 服务消费者 02-spring-cloud-alibaba-nacos-consumer-2300
Step-2: 导入 pom.xml 依赖
Step-3: 创建 application.yml
Step-4: 创建启动类 SpringCloudAlibabaConsumerApplication
Step-5: 创建控制类 ConsumerController
Step-6: 创建 RestTemplate 配置类
```

**Step-2: 导入 pom.xml 依赖**

```xml
<dependencies>
    <!-- 公共依赖 common -->
    <dependency>
        <groupId>cn.wickson.cloud.alibaba</groupId>
        <artifactId>01-spring-cloud-alibaba-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <!-- Spring Cloud alibaba Nacos 注册与发现 -->
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    </dependency>

</dependencies>
```

**Step-3: 创建 `application.yml`**

```yml
# ------------------------- 应用端口 -------------------------
server:
  port: 2300

spring:
  application:
    name: spring-cloud-alibaba-nacos-consumer
  cloud:
# -------------------------------- nacos地址 ---------------------------------
    nacos:
      discovery:
        server-addr: localhost:8848 # nacos 服务注册中心地址
# -------------------------------- nacos地址 ---------------------------------

# ------------------------------- 服务提供者路由 -------------------------------
service-url:
  nacos-user-service: http://spring-cloud-alibaba-nacos-producer
# ------------------------------- 服务提供者路由 -------------------------------
```

**Step-4: 创建启动类 SpringCloudAlibabaConsumerApplication**

```java
/**
 * Spring Cloud Alibaba Consumer 启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-23
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud.alibaba")
public class SpringCloudAlibabaConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudAlibabaConsumerApplication.class, args);
    }

}
```

**Step-5: 创建控制类 ConsumerController**

```java
/**
 * 消费者控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service-url.nacos-user-service}")
    private String serverUrl;

    @GetMapping("/getProducer")
    public String getProducer() {
        return restTemplate.getForObject(serverUrl + "/producer/nacos", String.class);
    }

}
```

**Step-6: 创建 RestTemplate 配置类**

```java
/**
 * RestTemplateConfig 用作负载均衡配置
 *
 * @author ZhangZiHeng
 * @date 2024-01-23
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



### 服务注册中心

当 服务提供者1、服务提供者2、服务消费者 启动时，查看 服务管理 --> 服务列表

![image-20240123163334025](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231633076.png)



### 服务配置中心

由于我们需要通过在服务提供者中读取 `config.info` 的配置文件信息

* **注意：Data ID 配置规则如下：**

```tex
Data ID = ${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
```

![image-20240123163538760](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231635810.png)

* 我们需要在 **配置管理 --> 配置列表 --> +** 中创建并配置如下内容

![image-20240123163647874](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231636929.png)



## Nacos 配置说明

在实际开发场景中，我们需要针对不同的开发环境采用不同的系统环境配置，如下是Nacos的领域模型。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241040438.jpeg" alt="nacos_data_model" style="zoom:80%;float:left" />

`Namespace` 命名空间、`Group` 分组、`Service/Data Id` 这些都是为了进⾏归类管理，把服务和配置⽂件进⾏归类。

| 名词      | 描述                                                         |
| --------- | ------------------------------------------------------------ |
| Namespace | 用于隔离配置和服务的独立区域。例如开发环境、测试环境、生产环境。 |
| Group     | 对同一个服务的不同实例进行逻辑上的分组。例如，我们有两个项目都需要使用我们的 Nacos。我们可以在同一个 NameSpace 中通过 Group 将他们隔开。 |
| Service   | 一个具体的应用或服务实例，可通过服务注册进行管理。例如，对应具体的应用，如订单服务。 |
| DataId    | 用于唯一标识一个配置项，与服务名、分组和命名空间一起使用。例如，订单服务对应的配置文件名称。 |

### NameSpace

`NameSpace：` 命名空间，针对不同的环境进行隔离。在 `Nacos` 中 `NameSpace` 的默认空间是 `public`。

* 在配置文件中指定命名空间的配置如下，我们指定 `dev` 的命名空间：

```yaml
# 参数配置中心命名空间
spring.cloud.nacos.config.namespace=571e7679-ab13-4608-abf4-e1a3618398e5
```

* 我们需要在 **命名空间 --> 新建命名空间** 中创建并配置，其中我创建 `dev` 命名空间为例子。

![202401241132100](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241135000.png)

![image-20240124113241282](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241132316.png)



### Group

Group：一个 `NameSpace` 下有多个 Group ，其中 Group 默认为 `DEFAULT_GROUP` ，可以将不同的微服务划分到不同的组。也可以将不同的项目以不同的组进行区分。

* 在配置文件中指定组名的配置如下，我们指定 `NACOS_DEV_GROUP` 组。

```yaml
# 参数配置中心组别名称
spring.cloud.nacos.config.group=NACOS_DEV_GROUP
```

* 我们需要在 **配置管理 --> 配置列表** 中创建，在 `dev` 命名空间下，其中我创建 `NACOS_DEV_GROUP` 组例子。

![image-20240124114259379](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241142433.png)

![image-20240124114637879](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241146935.png)

![image-20240124114940090](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241149130.png)



### Data ID

Data ID：对应一个服务的配置与服务名、分组和命名空间一起使用。

* 在配置文件中指定 Data ID 配置如下。

```yaml
# ${spring.application.name}-${spring.profiles.active}.${spring.cloud.nacos.config.file-extension}
spring-cloud-alibaba-nacos-producer-dev.yaml
```

* 我们需要在 **配置管理 --> 配置列表** 中创建，与创建 Group 时一起创建，具体的配置规则如下

![image-20240124114637879](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241146935.png)

![image-20240124114940090](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241149130.png)



### 测试结果

我们以上诉 **`Naocs`单机操作**案例，重新修改上述 **服务提供者1**、**服务提供者2** 的 yml 配置文件

* `application.yml`

```yaml
spring:
  profiles:
    active: dev # 开发环境
```

* `bootstrap.yml`

```yaml
spring:
  application:
    name: spring-cloud-alibaba-nacos-producer
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 # nacos 服务注册中心地址
      config:
        server-addr: localhost:8848 # nacos 服务配置中心地址
        file-extension: yaml
        namespace: 571e7679-ab13-4608-abf4-e1a3618398e5 # 指定命名空间为 dev
        group: NACOS_DEV_GROUP # 指定组名为 NACOS_DEV_GROUP
```

* 访问地址：http://localhost:2300/consumer/getProducer

![image-20240124141843160](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241418212.png)



## Nacos 集群操作

集群模式：主要的作用还是为了保高可用，即使其中的一台 `Nacos` 宕机也不会影响项目的正常使用。

### 实现需求

```tex
1. 搭建三台 Nacos 集群，实现相互通讯。
2. 并采用两种方式进行测试：ip直连模式、Nginx代理转发
```

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241642148.jpeg" alt="deployDnsVipMode.jpg" style="zoom:100%;float:left" />

### 实现结果

* **ip直连模式：** 在配置文件中直接配置三个 `Nacos` 的 `ip` 地址.

![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401250930220.gif)

* Nginx代理转发

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401251045177.png" alt="image-20240125104510104" style="zoom:100%;float:left" />



### 实现步骤

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401251046992.png" alt="image-20240125104623955" style="zoom:100%;float:left" />

```tex
1. 创建 MySQL 服务：192.168.10.16
2. 创建 Nacos1 服务：192.168.10.17, 配置 Nacos 集群服务
3. 创建 Nacos2 服务：192.168.10.18, 配置 Nacos 集群服务
4. 创建 Nacos3 服务：192.168.10.19, 配置 Nacos 集群服务
6. 实现 ip 直连模式
7. 实现 Nginx 代理转发模式
```



### 创建 MySQL 服务

* 由于我们单机版中使用的是 Nacos 自带的数据库，但是在集群版中这是不行的，所以让三台服务连接外部数据库
* 数据文件  [nacos-mysql.sql](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241737759.sql) 将数据库导入到 自己创建的数据库 `nacos_config` 中，具体操作如下。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401241742602.gif" alt="动画" style="zoom:80%;float:left" />



### 创建 Nacos 服务

> 下载地址：https://github.com/alibaba/nacos
>
> 下载版本：nacos-server-2.0.1.tar.gz

* **注意：我们三台配置都是一样的，我只以其中 192.168.10.17 为例子**

#### 修改 `cluster.conf` 文件

```sh
[root@localhost conf]# pwd
/home/nacos/conf
[root@localhost conf]# cp cluster.conf.example cluster.conf
[root@localhost conf]# ll
总用量 88
-rw-r--r--. 1  502 games  1224 4月  19 2021 1.4.0-ipv6_support-update.sql
-rw-r--r--. 1  502 games  8489 4月  29 2021 application.properties
-rw-r--r--. 1  502 games  6515 4月  19 2021 application.properties.example
-rw-r--r--. 1 root root    670 1月  24 17:47 cluster.conf
-rw-r--r--. 1  502 games   670 3月  18 2021 cluster.conf.example
-rw-r--r--. 1  502 games 31156 4月  29 2021 nacos-logback.xml
-rw-r--r--. 1  502 games 10660 4月  19 2021 nacos-mysql.sql
-rw-r--r--. 1  502 games  8795 4月  19 2021 schema.sql
[root@localhost conf]# vi cluster.conf
#
# Copyright 1999-2018 Alibaba Group Holding Ltd.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#it is ip
#example
# 我们修改如下配置，变成我们配置的集群服务
192.168.10.17:8848
192.168.10.18:8848
192.168.10.19:8848
[root@localhost conf]#
```

#### 修改 `application.yml` 文件

```sh
[root@localhost conf]# pwd
/home/nacos/conf
[root@localhost conf]# vi application.properties

### 在 properties文件中添加如下数据库连接
spring.datasource.platform=mysql
db.num=1
db.url.0=jdbc:mysql://192.168.10.16:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=P@ssw0rd
```

#### 启动 Nacos

```sh
[root@localhost nacos]# cd bin/
[root@localhost bin]# ll
总用量 20
-rwxr-xr-x. 1 502 games  954 5月  14 2020 shutdown.cmd
-rwxr-xr-x. 1 502 games  951 3月  18 2021 shutdown.sh
-rwxr-xr-x. 1 502 games 3340 4月  19 2021 startup.cmd
-rwxr-xr-x. 1 502 games 4923 4月  19 2021 startup.sh
[root@localhost bin]# cp startup.sh startup.sh.back
[root@localhost bin]# ./startup.sh
/home/java/jdk1.8.0_171/bin/java  -server -Xms2g -Xmx2g -Xmn1g -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=320m -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/home/nacos/logs/java_heapdump.hprof -XX:-UseLargePages -Dnacos.member.list= -Djava.ext.dirs=/home/java/jdk1.8.0_171/jre/lib/ext:/home/java/jdk1.8.0_171/lib/ext -Xloggc:/home/nacos/logs/nacos_gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M -Dloader.path=/home/nacos/plugins/health,/home/nacos/plugins/cmdb -Dnacos.home=/home/nacos -jar /home/nacos/target/nacos-server.jar  --spring.config.additional-location=file:/home/nacos/conf/ --logging.config=/home/nacos/conf/nacos-logback.xml --server.max-http-header-size=524288
nacos is starting with cluster
nacos is starting，you can check the /home/nacos/logs/start.out
[root@localhost bin]# netstat -tunlp | grep 8848
tcp6       0      0 :::8848                 :::*                    LISTEN      1377/java 
```

#### 开放端口

* 在 Linux 中开放端口命令如下

```sh
[root@localhost bin]# firewall-cmd --zone=public --add-port=8848/tcp --permanent
success
[root@localhost bin]# firewall-cmd --reload
success
[root@localhost bin]#
```

* **注意事项：**[nacos伪集群启动成功，但是服务注册不上的问题](https://blog.csdn.net/kangxiaoyanl/article/details/132177499)

```shell
[root@localhost bin]# firewall-cmd --zone=public --add-port=9849/tcp --permanent
success
[root@localhost bin]# firewall-cmd --reload
success
[root@localhost bin]#
```



### ip 直连模式

**ip直连：**上诉我们已将搭建好了三台 `Nacos` 服务器信息，我们只需要在 `yml` 配置文件中修改 `Nacos` 服务注册地址即可，具体如下：

```yaml
server:
  port: 2200

spring:
  application:
    # 应用名称
    name: spring-cloud-alibaba-nacos-producer
  cloud:
    nacos:
      discovery:
        # nacos 服务注册中心地址
        server-addr: 192.168.10.17:8848,192.168.10.18:8848,192.168.10.19:8848
      config:
        # nacos 服务配置中心地址
        server-addr: 192.168.10.17:8848,192.168.10.18:8848,192.168.10.19:8848 
        file-extension: yaml
        # 指定命名空间为 dev
        namespace: fc1c8c25-c514-4430-9197-3148654af6ab 
        # 指定组名为 NACOS_DEV_GROUP
        group: NACOS_DEV_GROUP 
```



### Nginx 代理转发

**Nginx代理转发：**本质上就是通过配置了一个 负载均衡 分别路由到不同的 Nacos 中。

* **Nginx 配置如下**

```nginx
# 默认配置就是轮询策略
upstream nacos_server {
   server 192.168.10.17:8848;
   server 192.168.10.18:8848;
   server 192.168.10.19:8848;
}
 
server{
   
    # 监听端口
    listen 8001;
    # 匹配请求中的host值
    server_name nacos_server;
 
    location / {
        #nginx的主机地址
        proxy_set_header Host $http_host;
  
        #用户端真实的IP，即客户端IP
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
 
        # 配置代理服务器
        proxy_pass http://nacos_server;
    }
 
}
```

* **bootstrap.yml** 配置文件如下

```yaml
server:
  port: 2200

spring:
  application:
    # 应用名称
    name: spring-cloud-alibaba-nacos-producer
  cloud:
    nacos:
      discovery:
        # nacos 服务注册中心地址
        server-addr: 192.168.10.20:8001
      config:
        # nacos 服务配置中心地址
        server-addr: 192.168.10.20:8001
        file-extension: yaml
        # 指定命名空间为 dev
        namespace: fc1c8c25-c514-4430-9197-3148654af6ab 
        # 指定组名为 NACOS_DEV_GROUP
        group: NACOS_DEV_GROUP 
```





