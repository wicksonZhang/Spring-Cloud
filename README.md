# Spring Cloud

> 由于本次项目过多，所以 Spring Cloud 和 Spring Cloud Alibaba 分别对应了不同的代码仓库地址，具体如下：
>
> Spring Cloud Github：https://github.com/wicksonZhang/Spring-Cloud
>
> Spring Cloud Alibaba Github：https://github.com/wicksonZhang/Spring-Cloud-Alibaba



## 🐳背景

近年来，我在多家公司的工作经历让我观察到微服务正在迅猛发展。从我个人的经历来看，就连一些规模较小的公司都在不同程度上转向微服务架构。回想起2018 年，当时只需要掌握 `SSM`、`SpringBoot` 就可以出去找到一份工作。然而，随着时代的发展，这已经逐渐演变成了基本操作。

微服务使用在在合适的场景中犹如鲲鹏展翅，能够为业务带来巨大的灵活性和可扩展性。然而，微服务并非银弹。不正确地使用微服务架构就如洪水猛兽，可能会带来诸多问题。具体参考博文：https://dbaplus.cn/news-141-5678-1.html

因此，尽管微服务架构具有强大的优势，但在应用时需要谨慎并确保合理的设计和实施，以充分发挥其潜力，而不至于陷入不必要的困境。



## 🐯内容简介

本系列主要分为三个部分来介绍微服务。首先会介绍微服务的基础知识以及微服务解决了那些问题，然后会深入了解到 Spring Cloud 核心组件，最后会说明后起之秀 Spring Cloud Alibaba 的使用。

**微服务的基础知识**

* **微服务基础概念**：为什么是什么？主要解决了那些问题？主要的应用场景有哪些？
* **微服务技术架构选型**：如何选择对应的 Spring Boot、Spring Cloud 版本。
* **微服务技术实现**：我们以电商中的支付和订单两个服务为例子，模拟出微服务的实现。



**Spring Cloud 核心组件**

* **服务注册与发现**

  - [x] Spring Cloud Netflix-Eureka

  - [x] Apache Zookeeper

  - [x] HashiCorp Consul

* **服务调用**

  - [x] Spring Cloud Netflix-Ribbon

  - [x] Spring Cloud OpenFeign

* **服务熔断与限流**
  - [x] Spring Cloud Netflix-Hystrix

* **服务网关**
  - [x] Spring Cloud Gateway

* **服务配置**
  - [x] Spring Cloud Config

* **消息总线**
  - [x] Spring Cloud Bus

* **消息驱动**
  - [x] Spring Cloud Stream

* **链路追踪**
  - [x] Spring Cloud Sleuth



**Spring Cloud Alibaba**

* **基础知识**
  - [x] Spring Cloud Alibaba 

* **服务注册与发现**
  - [x] Spring CLoud Alibaba Nacos
* **服务熔断与限流**
  - [x] Spring Cloud Alibaba Sentinel
* **分布式事务**
  - [x] Spring Cloud Alibaba Seata



Spring Cloud Alibaba 与 Spring Cloud 之间的关系可以理解为 Spring Cloud Alibaba 是在 Spring Cloud 基础上提供的一套增强工具和中间件，以满足在阿里巴巴等场景下的分布式系统需求。




## 🐼内置案例

我们将第二部分（Spring Cloud）、第三部分（Spring Cloud Alibaba）学完之后会实现一个综合案例，对所学的知识进行整合实现一个具体的案例。

### Spring Cloud 内置案例

* 需要完成的需求：当生产者产生消息之后，两个消费者会通过消息通道监听到对应的消息。
* 使用到的技术栈：Eureak、Gateway、Hystrix、Open Feign、Stream、RibbitMQ、Sleuth、WebSocket、Vue。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402051148969.gif" alt="image" style="zoom:100%;float:left" />



### Spring Cloud Alibaba 内置案例

* 需要完成的需求如下：
  1. 通过 Business 下单，调用订单服务、仓储服务。
  2. 仓储服务：对给定的商品扣除仓储数量。
  3. 订单服务：根据采购需求创建订单。
  4. 帐户服务：从用户帐户中扣除余额
* 使用到的技术栈：Nacos、Sentinel、Seata、Gateway、Open Feign、Vue。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041747170.gif" alt="202402041747170-images" style="zoom:100%;float:left" />



## 参考博文

上诉的内容主要参考了 [尚硅谷SpringCloud框架开发教程(SpringCloudAlibaba微服务分布式架构丨springcloud)](https://www.bilibili.com/video/BV18E411x7eT/?spm_id_from=333.337.search-card.all.click&vd_source=8c87bde4b88d9c9613014acf272109fc) 