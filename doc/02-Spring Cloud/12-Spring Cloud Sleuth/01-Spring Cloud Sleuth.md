# Spring Cloud Sleuth

> 本章节主要是基于 `Sleuth` 的分布式链路追踪，所以还是基于以往的案例进行开发。

## Sleuth 基础概念

> 官网地址：https://spring.io/projects/spring-cloud-sleuth/

​		*Spring Cloud Sleuth* 是 `Spring Cloud` 生态中的一个组件，主要解决了分布式链路追踪的问题。`Spring Cloud Sleuth` 可以在多个服务之间进行跟踪链路的请求和调用链。 

​		*Spring Cloud Sleuth* 中的关键功能如下：

* **唯一标识符(Trace Id)：**为每个请求生成唯一的标识符，用于跟踪请求的整个生命周期。
* **跨服务追踪：** 将唯一标识符添加到请求的头部，使得在不同服务之间传播时能够保持唯一性。
* **调用链：** 根据唯一标识符构建请求的调用链，显示请求从一个服务到另一个服务的流程。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401221152101.png" alt="trace-id" style="zoom:1000%;float:left" />



## Sleuth 安装启动

* 下载地址：https://repo1.maven.org/maven2/io/zipkin/zipkin-server/
* 启动命令： java -jar zipkin-server-2.15.0-exec.jar
* 访问地址：http://localhost:9411/zipkin/



## Sleuth 链路监控展现

* 我们基于上一章 `Spring Cloud Stream` 案例进行展示

* 我们分别在服务 添加如下依赖和配置：

  * `11-spring-cloud-stream-consumer1-11200`
  * `11-spring-cloud-stream-consumer2-11300`
  * `11-spring-cloud-stream-producer-11100`
  * `11-spring-cloud-stream-websocket-11500` 

* pom.xml 依赖

  ```xml
  <!-- sleuth+zipkin 依赖 -->
  <dependency>
  	<groupId>org.springframework.cloud</groupId>
  	<artifactId>spring-cloud-starter-zipkin</artifactId>
  </dependency>
  ```

* yml 配置，我么以 `spring-cloud-stream-consumer1` 为例

  ```yml
  spring:
    application:
      name: spring-cloud-stream-consumer1
    zipkin:
      base-url: http://localhost:9411
    sleuth:
      sampler:
        probability: 1
  ```

* 测试结果

![动画](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401221430305.gif)

