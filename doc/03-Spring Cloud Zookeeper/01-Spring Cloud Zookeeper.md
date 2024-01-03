# Spring Cloud Zookeeper

> 本章节的代码：https://github.com/wicksonZhang/Spring-Cloud
>
> 我们只需要聚焦在如下2个服务当中（singleton-单机）：
>
> * [04-spring-cloud-singleton-zookeeper-order-4100](https://github.com/wicksonZhang/Spring-Cloud/tree/main/04-spring-cloud-singleton-zookeeper-order-4100)
>
> * [04-spring-cloud-singleton-zookeeper-payment-4000](https://github.com/wicksonZhang/Spring-Cloud/tree/main/04-spring-cloud-singleton-zookeeper-payment-4000)

## 基础概念

### Zookeeper 是什么？

* Zookeeper 是一个开源的分布式协调服务，旨在提供高度可靠性和高性能的分布式数据管理和协调。

* 在微服务体系中 `Zookeeper` 可以充当我们的服务注册中心，允许微服务在其上注册自身的位置信息，并且允许其他服务查询和发现这些注册服务。

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401021759126.png" alt="Apache_ZooKeeper_logo.svg" style="zoom:80%;float:left" />



### Zookeeper 优缺点

**优点**

1. **高可用性：** ZooKeeper采用了多副本机制，支持主备架构，确保了服务的高可用性。
2. **一致性和可靠性：** 提供了强一致性的数据模型，对数据的更新操作都是原子性的，能够确保数据的可靠性和一致性。
3. **轻量级和高性能：** ZooKeeper的设计注重了性能和效率，可以处理大规模的请求，并且响应速度很快。
4. **可扩展性：** 可以很容易地扩展集群，支持动态添加和删除节点，以适应不同规模和负载的需求。
5. **通用性：** 不仅可以作为服务注册中心，还可以用于分布式锁、队列、选举等场景，具有通用性。

**缺点**

1. **复杂性：** 在配置、部署和维护方面需要一定的专业知识，可能增加系统的复杂性。
2. **单点故障：** 尽管ZooKeeper的设计是高可用的，但如果整个ZooKeeper集群失效，会对整个系统造成严重影响。
3. **性能瓶颈：** 在某些高并发场景下，ZooKeeper可能成为性能瓶颈，对系统的性能产生影响。



### Zookeeper 解决了什么问题

**服务注册与发现**

* **问题**：购物车服务需要与其他服务进行通信，但是服务的 IP 地址和端口是动态变化的。
* **Zookeeper 提供的解决方案：** 购物车服务在启动时注册自己的 IP 地址和端口到 ZooKeeper 的节点中。其他服务需要调用购物车服务时，可以从 ZooKeeper 获取最新的购物车服务地址和端口信息，实现动态的服务发现和通信。

**服务的健康检查**

* **问题：** 当购物车服务出现故障时，其他服务需要知道购物车的服务状态。
* **Zookeeper 提供的解决方案：** ZooKeeper 可以监控购物车服务节点的健康状态，如果购物车服务不可用，ZooKeeper 可以及时更新节点信息，通知其他服务不要使用该服务或使用备用服务。

   

## 安装 `Zookeeper`

> `Zookeeper` 下载地址：[https://apache.org/dist/zookeeper/](https://gitee.com/link?target=https%3A%2F%2Fapache.org%2Fdist%2Fzookeeper%2F)

**安装环境**

* 操作系统：CentOS Linux release 7.9.2009 (Core)
* Zookeeper 版本：zookeeper-3.4.14

**安装步骤**

1. Step-1：上传安装包
2. Step-2：解压安装包
3. Step-3：修改配置文件

**具体实现**

* Step-1：上传安装包，直接将安装包上传至 `home` 盘符

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031026912.png" alt="image-20240103102654867" style="zoom:100%;float:left" />

* Step-2：解压安装包

  ```shell
  [root@localhost home]# tar -zxvf zookeeper-3.4.14.tar.gz
  ```

* Step-3：修改配置文件

  ```shell
  [root@localhost home]# cd zookeeper-3.4.14/conf/
  [root@localhost conf]# cp zoo_sample.cfg zoo.cfg
  [root@localhost conf]# ll
  总用量 16
  -rw-rw-r--. 1 2002 2002  535 3月   7 2019 configuration.xsl
  -rw-rw-r--. 1 2002 2002 2161 3月   7 2019 log4j.properties
  -rw-r--r--. 1 root root  922 1月   3 10:29 zoo.cfg
  -rw-rw-r--. 1 2002 2002  922 3月   7 2019 zoo_sample.cfg
  [root@localhost conf]# 
  ```

  

## 启动 `Zookeeper` 

> 本次搭建的环境是 `Zookeeper` 的单机环境

* 启动：./zkServer.sh start

```sh
[root@localhost zookeeper-3.4.14]# cd bin/
[root@localhost bin]# ./zkServer.sh start # 启动 Zookeeper
ZooKeeper JMX enabled by default
Using config: /home/zookeeper-3.4.14/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
[root@localhost bin]# 
```



* 停止：./zkServer.sh stop

```shell
[root@localhost bin]# ./zkServer.sh stop
ZooKeeper JMX enabled by default
Using config: /home/zookeeper-3.4.14/bin/../conf/zoo.cfg
Stopping zookeeper ... STOPPED
[root@localhost bin]# 
```



* 重启：./zkServer.sh restart

```shell
[root@localhost bin]# ./zkServer.sh restart
ZooKeeper JMX enabled by default
Using config: /home/zookeeper-3.4.14/bin/../conf/zoo.cfg
ZooKeeper JMX enabled by default
Using config: /home/zookeeper-3.4.14/bin/../conf/zoo.cfg
Stopping zookeeper ... no zookeeper to stop (could not find file /tmp/zookeeper/zookeeper_server.pid)
ZooKeeper JMX enabled by default
Using config: /home/zookeeper-3.4.14/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
[root@localhost bin]# 
```



* 状态：./zkServer.sh status

```sh
[root@localhost bin]# ./zkServer.sh status
ZooKeeper JMX enabled by default
Using config: /home/zookeeper-3.4.14/bin/../conf/zoo.cfg
Mode: standalone
[root@localhost bin]# 
```



* 进入节点：./zkCli.sh 

```shell
[root@localhost bin]# ./zkCli.sh 
Connecting to localhost:2181
2024-01-03 10:39:16,723 [myid:] - INFO  [main:Environment@100] - Client environment:zookeeper.version=3.4.14-4c25d480e66aadd371de8bd2fd8da255ac140bcf, built on 03/06/2019 16:18 GMT
2024-01-03 10:39:16,725 [myid:] - INFO  [main:Environment@100] - Client environment:host.name=localhost
2024-01-03 10:39:16,725 [myid:] - INFO  [main:Environment@100] - Client environment:java.version=1.8.0_171
2024-01-03 10:39:16,726 [myid:] - INFO  [main:Environment@100] - Client environment:java.vendor=Oracle Corporation
2024-01-03 10:39:16,726 [myid:] - INFO  [main:Environment@100] - Client environment:java.home=/home/java/jdk1.8.0_171/jre
2024-01-03 10:39:16,726 [myid:] - INFO  [main:Environment@100] - Client environment:java.class.path=/home/zookeeper-3.4.14/bin/../zookeeper-server/target/classes:/home/zookeeper-3.4.14/bin/../build/classes:/home/zookeeper-3.4.14/bin/../zookeeper-server/target/lib/*.jar:/home/zookeeper-3.4.14/bin/../build/lib/*.jar:/home/zookeeper-3.4.14/bin/../lib/slf4j-log4j12-1.7.25.jar:/home/zookeeper-3.4.14/bin/../lib/slf4j-api-1.7.25.jar:/home/zookeeper-3.4.14/bin/../lib/netty-3.10.6.Final.jar:/home/zookeeper-3.4.14/bin/../lib/log4j-1.2.17.jar:/home/zookeeper-3.4.14/bin/../lib/jline-0.9.94.jar:/home/zookeeper-3.4.14/bin/../lib/audience-annotations-0.5.0.jar:/home/zookeeper-3.4.14/bin/../zookeeper-3.4.14.jar:/home/zookeeper-3.4.14/bin/../zookeeper-server/src/main/resources/lib/*.jar:/home/zookeeper-3.4.14/bin/../conf:.:/home/java/jdk1.8.0_171/lib/dt.jar:/home/java/jdk1.8.0_171/lib/tools.jar
2024-01-03 10:39:16,726 [myid:] - INFO  [main:Environment@100] - Client environment:java.library.path=/usr/java/packages/lib/amd64:/usr/lib64:/lib64:/lib:/usr/lib
2024-01-03 10:39:16,726 [myid:] - INFO  [main:Environment@100] - Client environment:java.io.tmpdir=/tmp
2024-01-03 10:39:16,727 [myid:] - INFO  [main:Environment@100] - Client environment:java.compiler=<NA>
2024-01-03 10:39:16,727 [myid:] - INFO  [main:Environment@100] - Client environment:os.name=Linux
2024-01-03 10:39:16,731 [myid:] - INFO  [main:Environment@100] - Client environment:os.arch=amd64
2024-01-03 10:39:16,731 [myid:] - INFO  [main:Environment@100] - Client environment:os.version=3.10.0-1160.el7.x86_64
2024-01-03 10:39:16,731 [myid:] - INFO  [main:Environment@100] - Client environment:user.name=root
2024-01-03 10:39:16,731 [myid:] - INFO  [main:Environment@100] - Client environment:user.home=/root
2024-01-03 10:39:16,731 [myid:] - INFO  [main:Environment@100] - Client environment:user.dir=/home/zookeeper-3.4.14/bin
2024-01-03 10:39:16,732 [myid:] - INFO  [main:ZooKeeper@442] - Initiating client connection, connectString=localhost:2181 sessionTimeout=30000 watcher=org.apache.zookeeper.ZooKeeperMain$MyWatcher@4b9af9a9
2024-01-03 10:39:16,753 [myid:] - INFO  [main-SendThread(localhost:2181):ClientCnxn$SendThread@1025] - Opening socket connection to server localhost/127.0.0.1:2181. Will not attempt to authenticate using SASL (unknown error)
Welcome to ZooKeeper!
JLine support is enabled
2024-01-03 10:39:16,844 [myid:] - INFO  [main-SendThread(localhost:2181):ClientCnxn$SendThread@879] - Socket connection established to localhost/127.0.0.1:2181, initiating session
2024-01-03 10:39:16,885 [myid:] - INFO  [main-SendThread(localhost:2181):ClientCnxn$SendThread@1299] - Session establishment complete on server localhost/127.0.0.1:2181, sessionid = 0x10000196f360000, negotiated timeout = 30000

WATCHER::

WatchedEvent state:SyncConnected type:None path:null
[zk: localhost:2181(CONNECTED) 0] 
```



## Zookeeper 单机操作

* 实现需求

  * 我们将上一章节的 订单、支付微服务注册到 `Zookeeper` 当中

* 实现思路

  * Step-1：创建支付服务 `04-spring-cloud-singleton-zookeeper-payment-4000`
  * Step-2：创建订单服务 `04-spring-cloud-singleton-zookeeper-order-4100`

* 代码结构

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031709896.png" alt="image-20240103170907862" style="zoom:100%;float:left" />

  

### 创建支付服务

* 创建支付服务：`04-spring-cloud-singleton-zookeeper-payment-4000`
* 实现步骤
  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类
  * Step-4：编写业务类

* 导入 `pom.xml` 依赖

```xml
    <dependencies>
        <!-- 引入公共依赖 -->
        <dependency>
            <groupId>cn.wickson.cloud</groupId>
            <artifactId>01-spring-cloud-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- 引入 Zookeeper 组件 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
            <!-- 排除自带的 Zookeeper -->
            <exclusions>
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- 添加 zookeeper 3.4.14 版本 -->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.14</version>
        </dependency>    
    </dependencies>
```

* 修改 `application.properties` 文件

```properties
# 端口
server.port=4000
# 服务别名
spring.application.name=spring-cloud-zookeeper-payment-4000
# zookeeper 注册节点
spring.cloud.zookeeper.connect-string=192.168.1.15:2181
```

* 创建主启动类
  * **注意**：需要添加 @EnableDiscoveryClient ，用于向 consul 或者 zookeeper 作为注册中心注册微服务

```java
/**
 * 支付服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2024-01-03
 */
// 用于向 consul 或者 zookeeper 作为注册中心注册微服务
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudZookeeperPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudZookeeperPaymentApplication.class, args);
    }

}
```

* 编写业务类

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

    @GetMapping("/zk")
    public ResultUtil paymentZk() {
        return ResultUtil.success("Spring Cloud with zookeeper port: " + serverPort + ", UUID：" + UUID.randomUUID().toString());
    }

}
```



### 创建订单服务

* 创建订单服务：`04-spring-cloud-singleton-zookeeper-order-4100`

* 实现步骤
  * Step-1：导入 `pom.xml` 依赖
  * Step-2：修改 `application.properties` 文件
  * Step-3：创建主启动类
  * Step-4：编写业务类

* 导入 `pom.xml` 依赖

```xml
    <dependencies>
        <!-- 引入公共依赖 -->
        <dependency>
            <groupId>cn.wickson.cloud</groupId>
            <artifactId>01-spring-cloud-common</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>

        <!-- 引入 Zookeeper 组件 -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zookeeper-discovery</artifactId>
            <!-- 排除自带的 Zookeeper -->
            <exclusions>
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <!-- 添加 zookeeper 3.4.14 版本 -->
        <dependency>
            <groupId>org.apache.zookeeper</groupId>
            <artifactId>zookeeper</artifactId>
            <version>3.4.14</version>
        </dependency>
    </dependencies>
```

* 修改 `application.properties` 文件

```properties
# 端口
server.port=4100
# 服务别名
spring.application.name=spring-cloud-zookeeper-order-4100
# zookeeper 注册节点
spring.cloud.zookeeper.connect-string=192.168.1.15:2181
```

* 创建主启动类

```java
/**
 * 支付服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2024-01-03
 */
// 用于向 consul 或者 zookeeper 作为注册中心注册微服务
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudZookeeperOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudZookeeperOrderApplication.class, args);
    }

}
```

* 编写业务类

* `OrderController.java`

```java
/**
 * 订单服务-控制类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    public static final String PAYMENT_URL = "spring-cloud-zookeeper-payment-4000";

    @Resource
    private RestTemplate restTemplate;

    /**
     * 获取订单
     *
     * @return ResultUtil
     */
    @GetMapping("/getPayment/zk")
    public ResultUtil getPayment() {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/zk" , ResultUtil.class);
    }

}
```

* `RestTemplateController.java`

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



### 单元测试

#### 是否在 `Zookeeper` 注册节点

```sh
[zk: localhost:2181(CONNECTED) 36] ls /
[services, zookeeper]
[zk: localhost:2181(CONNECTED) 37] ls /services
[spring-cloud-zookeeper-payment-4000, spring-cloud-zookeeper-order-4100]
```



#### 查看支付服务信息

```sh
[zk: localhost:2181(CONNECTED) 38] ls /services/spring-cloud-zookeeper-payment-4000 
[d4569f56-631c-4e57-babc-cbdcf0f3ffe5]
[zk: localhost:2181(CONNECTED) 39] get /services/spring-cloud-zookeeper-payment-4000/d4569f56-631c-4e57-babc-cbdcf0f3ffe5
{
	"name": "spring-cloud-zookeeper-payment-4000",
	"id": "d4569f56-631c-4e57-babc-cbdcf0f3ffe5",
	"address": "localhost",
	"port": 4000,
	"sslPort": null,
	"payload": {
		"@class": "org.springframework.cloud.zookeeper.discovery.ZookeeperInstance",
		"id": "application-1",
		"name": "spring-cloud-zookeeper-payment-4000",
		"metadata": {}
	},
	"registrationTimeUTC": 1704262890376,
	"serviceType": "DYNAMIC",
	"uriSpec": {
		"parts": [
			{
				"value": "scheme",
				"variable": true
			},
			{
				"value": "://",
				"variable": false
			},
			{
				"value": "address",
				"variable": true
			},
			{
				"value": ":",
				"variable": false
			},
			{
				"value": "port",
				"variable": true
			}
		]
	}
}
```



#### 查看订单服务信息

```sh
[zk: localhost:2181(CONNECTED) 40] ls /services/spring-cloud-zookeeper-order-4100                                        
[e9fc060d-e1e5-4ef6-bd49-729405d998c6]
[zk: localhost:2181(CONNECTED) 41] get /services/spring-cloud-zookeeper-order-4100/e9fc060d-e1e5-4ef6-bd49-729405d998c6
{
	"name": "spring-cloud-zookeeper-order-4100",
	"id": "e9fc060d-e1e5-4ef6-bd49-729405d998c6",
	"address": "localhost",
	"port": 4100,
	"sslPort": null,
	"payload": {
		"@class": "org.springframework.cloud.zookeeper.discovery.ZookeeperInstance",
		"id": "application-1",
		"name": "spring-cloud-zookeeper-order-4100",
		"metadata": {}
	},
	"registrationTimeUTC": 1704262892614,
	"serviceType": "DYNAMIC",
	"uriSpec": {
		"parts": [
			{
				"value": "scheme",
				"variable": true
			},
			{
				"value": "://",
				"variable": false
			},
			{
				"value": "address",
				"variable": true
			},
			{
				"value": ":",
				"variable": false
			},
			{
				"value": "port",
				"variable": true
			}
		]
	}
}
```



#### 访问订单服务

* [http://localhost:4100/order/getPayment/zk - Error](http://localhost:4100/order/getPayment/zk)

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401031431496.png" alt="image-20240103143111463" style="zoom:100%;float:left" />

