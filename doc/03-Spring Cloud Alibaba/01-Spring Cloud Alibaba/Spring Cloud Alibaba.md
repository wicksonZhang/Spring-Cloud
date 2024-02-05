# Spring Cloud `Alibaba`

## 前言

我们在上一个章节中已经完成了 *`Spring Cloud`* 微服务相关组件的学习，目前我们针对微服务的另一种解决方案 *`Spring Cloud Alibaba`* 进行学习，目前 `Alibaba` 微服务体系使用还是较多的。

如下是上一章 Spring Cloud 的文档和代码

* Spring Cloud 代码：https://github.com/wicksonZhang/Spring-Cloud
* Spring Cloud 文档：https://github.com/wicksonZhang/Spring-Cloud/tree/main/doc



## Spring Cloud `Alibaba` 是什么

> 官方文档：https://spring.io/projects/spring-cloud-alibaba/
>
> 中文文档：https://github.com/alibaba/spring-cloud-alibaba/blob/2022.x/README-zh.md

`Spring Cloud Alibaba`是由阿里巴巴公司与 Spring 社区合作开发的微服务框架的衍生项目。*`Spring Cloud Alibaba`* 在继承了 *`Spring Cloud`* 的优势基础上，融入了一些与阿里巴巴技术栈紧密集成的组件，以满足该生态系统中的特定需求。以下是具体的组件介绍：

1. **Nacos：** 作为注册中心和配置中心，`Nacos` 提供了动态服务发现、配置管理和服务管理的平台。相较于 *`Spring Cloud`* 中可能需要结合 `Spring Cloud Eureka` 和 `Spring Cloud Config` 才能实现服务发现和配置管理，`Nacos` 简化了这一过程。
2. **Sentinel：** `Sentinel` 是一款开源的流量控制和服务熔断的库，具备实时流量控制、服务熔断以及系统负载保护等功能。鉴于 `Spring Cloud Hystrix` 已停止更新，`Sentinel` 成为了替代方案，为微服务提供了稳定性和可用性的保障。
3. **RocketMQ：** 作为分布式消息中间件，`RocketMQ` 用于在微服务架构中进行异步通信和事件驱动。它为 *`Spring Cloud Alibaba`* 提供了可靠的消息传递机制，促进了微服务之间的协同工作。
4. **Seata：** `Seata` 是一款开源的分布式事务解决方案，专注于解决微服务架构中的分布式事务一致性问题。通过集成 `Seata`，*`Spring Cloud Alibaba`* 提供了全局事务的协调和管理，确保了数据的一致性。
5. **Dubbo：** `Dubbo` 是一款高性能、轻量级的开源 `RPC` 框架，用于微服务之间的远程调用。在 *`Spring Cloud Alibaba`* 中，集成了 `Dubbo`，使得微服务之间的远程通信更为便捷高效。

通过这些组件的集成，*`Spring Cloud Alibaba`* 为开发者提供了更加完善和与阿里巴巴技术栈高度兼容的微服务解决方案。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401221738441.png" alt="spring-cloud-alibaba-img-ca9c0e5c600bfe0c3887ead08849a03c" style="zoom:100%;float:left" />



## 项目初始化

> **项目地址：**https://github.com/wicksonZhang/Spring-Cloud-Alibaba.git
>
> 我们的项目是从 `Spring Cloud` 体系中进行迁移出来的，所以大部分还是使用到了以前的公共模块和依赖。

* **项目结构**

  <img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401231523789.png" alt="image-20240123152302753" style="zoom:100%;float:left" />

* **pom.xml**

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
  
      <!--父项目基本信息-->
      <groupId>cn.wickson.cloud.alibaba</groupId>
      <artifactId>Spring-Cloud-Alibaba</artifactId>
      <version>1.0-SNAPSHOT</version>
      <packaging>pom</packaging>
  
      <!--子项目基本信息-->
      <modules>
          <module>01-spring-cloud-alibaba-common</module>
      </modules>
  
      <!-- 统一管理jar包版本 -->
      <properties>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          <maven.compiler.source>1.8</maven.compiler.source>
          <maven.compiler.target>1.8</maven.compiler.target>
          <spring.boot.version>2.4.2</spring.boot.version>
          <spring.cloud.version>2020.0.1</spring.cloud.version>
          <spring.cloud.alibaba.version>2021.1</spring.cloud.alibaba.version>
          <junit.version>4.12</junit.version>
          <log4j.version>1.2.17</log4j.version>
          <lombok.version>1.16.18</lombok.version>
          <hutool.version>5.8.12</hutool.version>
          <mapstruct.version>1.5.3.Final</mapstruct.version>
      </properties>
  
      <!--项目依赖包统一管理-->
      <dependencyManagement>
          <dependencies>
              <!--spring boot 2.4.2-->
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-dependencies</artifactId>
                  <version>${spring.boot.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
  
              <!--spring cloud 2020.0.1 -->
              <dependency>
                  <groupId>org.springframework.cloud</groupId>
                  <artifactId>spring-cloud-dependencies</artifactId>
                  <version>${spring.cloud.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
  
              <!--spring cloud alibaba 2021.1 -->
              <dependency>
                  <groupId>com.alibaba.cloud</groupId>
                  <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                  <version>${spring.cloud.alibaba.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
  
              <dependency>
                  <groupId>junit</groupId>
                  <artifactId>junit</artifactId>
                  <version>${junit.version}</version>
              </dependency>
  
              <dependency>
                  <groupId>log4j</groupId>
                  <artifactId>log4j</artifactId>
                  <version>${log4j.version}</version>
              </dependency>
  
              <!--hutool工具依赖包-->
              <dependency>
                  <groupId>cn.hutool</groupId>
                  <artifactId>hutool-all</artifactId>
                  <version>${hutool.version}</version>
              </dependency>
  
              <dependency>
                  <groupId>org.projectlombok</groupId>
                  <artifactId>lombok</artifactId>
                  <version>${lombok.version}</version>
                  <optional>true</optional>
              </dependency>
  
              <!--MapStruct依赖包-->
              <dependency>
                  <groupId>org.mapstruct</groupId>
                  <artifactId>mapstruct</artifactId>
                  <version>${mapstruct.version}</version>
              </dependency>
  
              <!--mapstruct-processor依赖包-->
              <dependency>
                  <groupId>org.mapstruct</groupId>
                  <artifactId>mapstruct-processor</artifactId>
                  <version>${mapstruct.version}</version>
              </dependency>
          </dependencies>
  
      </dependencyManagement>
  
      <build>
          <plugins>
              <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
                  <configuration>
                      <addResources>true</addResources>
                  </configuration>
              </plugin>
          </plugins>
      </build>
  
  </project>
  
  ```

  