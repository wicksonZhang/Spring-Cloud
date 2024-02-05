# Spring Cloud `Alibaba Seata`

> 代码地址：https://github.com/wicksonZhang/Spring-Cloud-Alibaba

## 基础概念

### Seata 解决了什么问题

Spring Cloud Alibaba Seata 主要解决了 **分布式事务一致性** 的问题。

例如在一个电商平台中，涉及到 订单服务 和 库存服务 两个微服务。在用户下单的时候，订单服务需要扣减库存。这会涉及到两个不同的服务之间的操作，因此需要保证在订单创建的过程中，需要保证库存的扣减是一个原子操作，要么全部成功，要么全部失败。

总体而言，Seata通过提供分布式事务管理的功能，解决了在这种复杂环境下可能出现的事务 **一致性和可靠性** 的问题。



### Seata 是什么？

> 官网地址：https://seata.apache.org/zh-cn/
>
> GIthub地址：https://github.com/apache/incubator-seata
>
> Seata 大白话介绍：https://www.51cto.com/article/704718.html

**Spring Cloud Alibaba Seata** 是一套分布式事务解决方案，它致力于解决分布式事务的 **一致性** 和 **可靠性** 问题。如下是 Seata 的核心组件：

* **TC（Transaction Coordinator）- 事务协调器：** 负责全局事务的协调和控制，协调多个参与者的分支事务的执行。
* **TM（Transaction Manager）- 事务管理器：**管理全局事务的开始、提交和回滚，与 TC 协作，负责事务的边界控制。
* **RM（Resource Manager）- 资源管理器：**管理分支事务的资源，与 TM 协作，负责事务的实际操作，如数据库的操作。
* **AT（AT mode, Automatic Two-Phase Commit） - 自动两阶段提交模式：** 通过预留资源和提交阶段，实现全局事务的一致性。

<img src="https://user-images.githubusercontent.com/68344696/145942191-7a2d469f-94c8-4cd2-8c7e-46ad75683636.png" alt="image" style="zoom:50%;float:left" />



## Seata Server

我们如果使用 `Seata` 还需如下的一些配置来帮助我们解决问题：

* **Seata Server：**这是 Seata 的服务端组件，用于协调和管理分布式事务。
* **配置配置文件：**在 Seata Server 的配置文件（`application.yml`）中，指定 Nacos 作为注册中心。
* **创建 Nacos 配置文件：**在 Nacos 配置 Seata 相关的配置文件。
* **创建数据库：**Seata需要通过数据源代理来拦截数据库的操作，以实现分布式事务。



### Seata Server 下载

本次在官网中推荐使用的 Seata 稳定版本是 1.8.0 ，所以具体安装信息如下：

* 下载地址：https://github.com/apache/incubator-seata/releases/download/v1.8.0/seata-server-1.8.0.zip

* 参考博文：https://blog.csdn.net/letterss/article/details/134269404



### 修改配置文件

* 文件地址：`seata-server-1.8.0\seata\conf\application.yml`

```yaml
seata:
  config:
    # support: nacos, consul, apollo, zk, etcd3
    # 使用nacos作为配置中心
    type: nacos
    nacos:
      # Nacos 注册中心地址
      server-addr: http://192.168.10.20:8001
      # Nacos 命名空间配置
      namespace: fc1c8c25-c514-4430-9197-3148654af6ab
      # Nacos 分组信息配置
      group: SEATA_DEV_GROUP
      username: nacos
      password: nacos
      # Nacos中 的配置文件名称
      data-id: spring-cloud-alibaba-seata-server.properties
  registry:
    # support: nacos, eureka, redis, zk, consul, etcd3, sofa
    # Nacos 作为注册中心
    type: nacos
    nacos:
      application: seata-server
      server-addr: http://192.168.10.20:8001
      group: SEATA_DEV_GROUP
      namespace: fc1c8c25-c514-4430-9197-3148654af6ab
      # 此处注意,这的值要和 Nacos 配置文件 service.vgroupMapping.seata_tx_group 的值一样
      cluster: default
      username: nacos
      password: nacos
  #store:
    # support: file 、 db 、 redis
    #mode: db
#  server:
#    service-port: 8091 #If not configured, the default is '${server.port} + 1000'
  security:
    secretKey: SeataSecretKey0c382ef121d778043159209298fd40bf3850a017
    tokenValidityInMilliseconds: 1800000
    ignore:
      urls: /,/**/*.css,/**/*.js,/**/*.html,/**/*.map,/**/*.svg,/**/*.png,/**/*.jpeg,/**/*.ico,/api/v1/auth/login
```



### 创建 Nacos 配置文件

* 在 `Nacos` 中的 **配置管理 -> 配置列表** 添加配置 `spring-cloud-alibaba-seata-server.properties`

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402021752657.png" alt="image-20240202175220610" style="zoom:100%;float:left" />

* 配置信息如下

```properties
# service.vgroupMapping.seata_tx_group 和前面的配置 seata.registry.nacos.cluster 的值是一样的
service.vgroupMapping.seata_tx_group=default
#这里的地址需要配置成seata所在服务器的地址
service.default.grouplist=127.0.0.1:8091
service.enableDegrade=false
service.disableGlobalTransaction=false
#此处对于数据存储使用的是数据库存储所以需要配置数据库的连接信息
store.mode=db
store.db.datasource=druid
store.db.dbType=mysql
#数据库驱动如果是mysql8使用这个，否则使用com.mysql.jdbc.Driver 
store.db.driverClassName=com.mysql.cj.jdbc.Driver 
store.db.url=jdbc:mysql://192.168.10.16:3306/seata?useUnicode=true&rewriteBatchedStatements=true
store.db.user=root
store.db.password=P@ssw0rd
store.db.minConn=5
store.db.maxConn=30
#此处有四张表的配置，所以需要在数据库中执行对应的SQL创建表
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
 
#Transaction rule configuration, only for the server
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
server.distributedLockExpireTime=10000
server.xaerNotaRetryTimeout=60000
server.session.branchAsyncQueueSize=5000
server.session.enableBranchAsyncRemove=false
 
#Transaction rule configuration, only for the client
client.rm.asyncCommitBufferLimit=10000
client.rm.lock.retryInterval=10
client.rm.lock.retryTimes=30
client.rm.lock.retryPolicyBranchRollbackOnConflict=true
client.rm.reportRetryCount=5
client.rm.tableMetaCheckEnable=true
client.rm.tableMetaCheckerInterval=60000
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.rm.sagaBranchRegisterEnable=false
client.rm.sagaJsonParser=fastjson
client.rm.tccActionInterceptorOrder=-2147482648
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
client.tm.defaultGlobalTransactionTimeout=60000
client.tm.degradeCheck=false
client.tm.degradeCheckAllowTimes=10
client.tm.degradeCheckPeriod=2000
client.tm.interceptorOrder=-2147482648
client.undo.dataValidation=true
client.undo.logSerialization=jackson
client.undo.onlyCareUpdateColumns=true
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.undo.compress.enable=true
client.undo.compress.type=zip
client.undo.compress.threshold=64k
 
#For TCC transaction mode
tcc.fence.logTableName=tcc_fence_log
tcc.fence.cleanPeriod=1h
 
#Log rule configuration, for client and server
log.exceptionRate=100
 
#Metrics configuration, only for the server
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
 
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableTmClientBatchSendRequest=false
transport.enableRmClientBatchSendRequest=true
transport.enableTcServerBatchSendResponse=false
transport.rpcRmRequestTimeout=30000
transport.rpcTmRequestTimeout=30000
transport.rpcTcRequestTimeout=30000
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
transport.serialization=seata
transport.compressor=none
```



### 创建 Seata 数据库

* 数据库文件：`seata\script\server\db` 并导入相关数据库脚本

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401301754221.gif" alt="image" style="zoom:100%;float:left" />

* 在上面的基础上在创建一个 `undo_log` 表
  * seata中默认使用的是`AT模式`，该模式需求每个客户端库内都存在一张`undo_log`表，用于回滚事务时临时记录数据。

```sql
CREATE TABLE `undo_log` (
  `branch_id` bigint(20) NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(128) NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int(11) NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`),
  KEY `ix_log_created` (`log_created`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AT transaction mode undo table';
```



### 启动 Seata Server

* 启动脚本：`\seata\bin\seata-server.bat`
* 访问：http://localhost:7091

![image-20240130175846064](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401301758111.png)

* **验证是否成功**

![image-20240131105359172](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202401311053235.png)

* **服务管理 --> 服务列表**

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402021753798.png" alt="image-20240202175355757" style="zoom:100%;float:left" />



## 案例实现

> 案例来源：https://seata.apache.org/zh-cn/docs/v1.8/user/quickstart
>
> 参考案例：https://github.com/apache/incubator-seata-samples/tree/master/seata-spring-boot-starter-samples
>
> 本章节代码：https://github.com/wicksonZhang/Spring-Cloud-Alibaba
>
> 1. 01-spring-cloud-alibaba-common
> 2. 04-spring-cloud-alibaba-seata-business-2800
> 3. 04-spring-cloud-alibaba-seata-storage-2900
> 4. 04-spring-cloud-alibaba-seata-order-3000
> 5. 04-spring-cloud-alibaba-seata-account-3100
> 6. 04-spring-cloud-alibaba-seata-web-3200
> 7. 05-spring-cloud-alibaba-gateway-server-9527

### 环境选择

```tex
操作系统：Windows 11
Nacos: 2.0.1
Seata: 1.8.0
SpringBoot: 2.6.3
SpringCloud: 2021.0.1
SpringCloudAlibaba: 2021.0.1.0
```



### 实现需求

用户购买商品的业务逻辑。整个业务逻辑由4个微服务提供支持：

- 业务服务：通过 Business 下单，调用订单服务、仓储服务。
- 仓储服务：对给定的商品扣除仓储数量。
- 订单服务：根据采购需求创建订单。
- 帐户服务：从用户帐户中扣除余额。

<img src="https://seata.apache.org/zh-cn/assets/images/architecture-6bdb120b83710010167e8b75448505ec.png" alt="Architecture" style="zoom:100%;" />



### 实现结果

* 我们测试的结果分为两种情况：正常情况、异常情况
  * 正常情况：当使用 @GlobalTransaction 进行分布式事务控制。
  * 异常情况：当不使用 @GlobalTransaction 进行分布式事务控制。

#### 正常情况

> 正常情况下我们使用的是 @GlobalTransaction 进行控制分布式事务。我们分别从正常和异常两种情况进行测试

当正常通过 Business 下单，调用订单服务、仓储服务。明显看到 库存服务、账户服务、订单服务 分别产生了如下数据。

* **库存服务：**每日坚果的库存由 **30** 减少到了 **20**。
* **订单服务**：产生了一条新的订单
* **账户服务：**用户ID为 2 的用户，用于余额从 **170.5** 减少到了 **70.5**。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041350704.gif" alt="image" style="zoom:100%;float:left" />



当我们再次下单，如果 **当前账户余额不足** 的情况下 **是否会产生订单信息、库存是否为减少？**。

* 从如下结果可以看到 **库存服务、账户服务、订单服务** 数据并没有减少。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041400206.gif" alt="image" style="zoom:100%;float:left" />



#### 异常情况

> 异常情况：当不使用 @GlobalTransaction 进行分布式事务控制。我们分别从 **正常** 和 **异常** 两种情况进行测试

**正常通过 Business 下单，调用订单服务、仓储服务**，明显看到 库存服务、账户服务、订单服务 分别产生了如下数据：

* **库存服务：**每日坚果的库存由 **20** 减少到了 **19**。
* **订单服务**：产生了一条新的订单。
* **账户服务：**用户ID为 2 的用户，用于余额从 **70.5** 减少到了 **69.5**。
* 只要代码不出问题还是可以正常运行的。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041413149.gif" alt="image" style="zoom:80%;float:left" />



当我们再次下单，如果 **当前账户余额不足** 的情况下 **是否会产生订单信息、库存是否为减少？**

* 问题已经复现了，当我们出现 **当前账户余额不足** 的情况下，依旧把我们的库存给减少了。
* **库存服务：**每日坚果 的库存从 19 减少到了 9。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041422531.gif" alt="image" style="zoom:80%;float:left" />



### 代码结构

> 由于本次是综合练习，所以准备使用 **DDD（领域驱动设计）** 进行开发。但为了避免代码过多，精简了一些代码。

本次使用到的项目如下：

```tex
1. 01-spring-cloud-alibaba-common
2. 04-spring-cloud-alibaba-seata-business-2800
3. 04-spring-cloud-alibaba-seata-storage-2900
4. 04-spring-cloud-alibaba-seata-order-3000
5. 04-spring-cloud-alibaba-seata-account-3100
6. 04-spring-cloud-alibaba-seata-web-3200
7. 05-spring-cloud-alibaba-gateway-server-9527
```

我们以订单服务 **04-spring-cloud-alibaba-seata-order-3000** 简单介绍一下代码结构

* 具体信息参考代码：[04-spring-cloud-alibaba-seata-order-3000](https://github.com/wicksonZhang/Spring-Cloud-Alibaba/tree/main/04-spring-cloud-alibaba-seata-order-3000/src/main/java/cn/wickson/cloud/alibaba/seata/order)

```java
├─src
│  ├─main
│  │  ├─java
│  │  │  └─cn.wickson.cloud.alibaba.seata.order
│  │  │     ├─app.service # 应用服务层、应用服务抽象类
│  │  │     │  └─impl	  # 应用服务实现类
│  │  │     ├─config	  # 配置类
│  │  │     ├─controller  # 控制类
│  │  │     ├─convert     # 转换类
│  │  │     ├─feign		  # 远程调用
│  │  │     │  └─fallback
│  │  │     ├─mapper	  # mapper 类
│  │  │     ├─model		  # 实体模型类
│  │  │     │  └─entity
│  │  │     └─repository  # 仓库类信息
│  │  │        └─impl
│  │  └─resources
│  │      └─mapper
```



### 数据库配置

我们本次创建三个数据库，具体如下：

* order数据库：[order.sql](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041714369.sql)

* storage数据库：[storage.sql](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041716233.sql)
* order 数据库：[account.sql](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041717521.sql)



### Nacos 配置

* 点击下载 Nacos 配置信息：[Nacos配置信息](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041444042.zip)

![image-20240204144345958](https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041443025.png)



### Seata-Server 配置

* 我们在 **Seata Server - 修改配置文件** 章节中已经配置完成。



### 实现步骤

> 关于 **代码结构** 中我已经给出了相关的代码案例，我们这里之说一下核心的调用流程代码。

* **Business Service：**当我们调用 Business 服务的接口如下，如下代码只做了三件事情：
  * Step-1：进行了基本的参数校验。
  * Step-2：通过 OpenFeign 调用 Stock 库存服务减少库存。
  * Step-3：通过 OpenFeign 调用 Order 订单服务创建订单。

```java
@Service
public class BusinessAppServiceImpl extends AbstractBusinessAppService implements IBusinessAppService {

    /**
     * 采购商品
     *
     * @param businessVO
     */
    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public void purchase(final BusinessVO businessVO) {
        /* Step-1: 参数校验 */
        ParamFormatUtil.formatParam(businessVO);

        /* Step-2: 减少库存，调用库存信息 */
        StockDTO stockDTO = BusinessConvert.INSTANCE.toStockDTO(businessVO);
        this.delStock(stockDTO);

        /* Step-3、创建订单 */
        OrderDTO orderDTO = BusinessConvert.INSTANCE.toOrderDTO(businessVO);
        this.createOrder(orderDTO);
    }

}
```



* **Stock Service：**库存服务需要减少库存信息，核心代码如下：

```java
@Service
public class StockServiceImpl extends AbstractStockAppService implements IStockService {

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    public StockDTO deduct(StockDTO stockDTO) {
        /* Step-1: 参数校验 */
        ParamFormatUtil.formatParam(stockDTO);

        /* Step-2: Stock validated */
        Stock stock = this.validateUpdateParam(stockDTO);

        /* Step-3: 减少库存 */
        stock.setCount(stock.getCount() - stockDTO.getCount());
        stockRepository.updateById(stock);

        return StockConvert.INSTANCE.toDTO(stock);
    }

}

```



* **Order Service：** 订单服务主要是 创建订单 和 扣减账户余额，核心代码如下：

```java
@Service
public class OrderAppServiceImpl extends AbstractOrderAppService implements IOrderAppService {

    @Override
    @Transactional(isolation= Isolation.REPEATABLE_READ,rollbackFor = Exception.class)
    public void create(final OrderDTO orderDTO) {
        /* Step-1: 参数校验 */
        ParamFormatUtil.formatParam(orderDTO);

        /* Step-2: 减少账户余额 */
        AccountDTO accountDTO = OrderConvert.INSTANCE.toAccountDTO(orderDTO);
        this.delAccount(accountDTO);

        /* Step-3： 创建订单 */
        Order order = OrderConvert.INSTANCE.toOrderDO(orderDTO);
        order.setOrderNo(UUID.randomUUID().toString());
        this.orderRepository.save(order);
    }
    
}
```



* **Account Service：**账户服务主要用于扣减账户余额，核心代码如下：

```java
@Service
public class AccountServiceImpl implements IAccountService {

    @Resource
    private IAccountRepository accountRepository;

    @Override
    @Transactional(isolation= Isolation.REPEATABLE_READ,rollbackFor = Exception.class)
    public void debit(AccountDTO accountDTO) {
        Long userId = accountDTO.getUserId();
        Account account = accountRepository.lambdaQuery().eq(Account::getUserId, userId).one();
        if (ObjUtil.isNull(account)) {
            throw UserOperationException.getInstance(ResultCodeEnum.SEATA_ACCOUNT_NULL_POINT_EXCEPTION);
        }
        BigDecimal amount = account.getAmount();
        BigDecimal accountAmount = accountDTO.getAmount();
        if (amount.compareTo(accountAmount) < 0) {
            throw UserOperationException.getInstance(ResultCodeEnum.SEATA_ACCOUNT_INSUFFICIENT_BALANCE);
        }
        account.setAmount(amount.subtract(accountAmount));
        accountRepository.updateById(account);
    }

}
```



### 单元测试

#### 前端参数校验

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041507979.gif" alt="image" style="zoom:100%;float:left" />



#### 库存不足校验

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041510912.gif" alt="image" style="zoom:100%;float:left" />



#### 余额不足校验

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041710594.gif" alt="image" style="zoom:80%;float:left" />



#### 商品编号与商品名称不对应校验

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041516824.gif" alt="image" style="zoom:100%;float:left" />



#### 服务降级

* 当我们将订单服务宕机掉：这时并没有打印出错误页面，而是报的订单创建失败。

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041523294.gif" alt="image" style="zoom:100%;float:left" />



#### 服务限流

我们针对订单服务进行限流，如下时具体的配置信息：

* Sentinel 界面配置

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041707263.png" alt="image-20240204170714224" style="zoom:80%;float:left" />

* 测试结果

<img src="https://cdn.jsdelivr.net/gh/wicksonZhang/static-source-cdn/images/202402041708570.gif" alt="image" style="zoom:80%;float:left" />

