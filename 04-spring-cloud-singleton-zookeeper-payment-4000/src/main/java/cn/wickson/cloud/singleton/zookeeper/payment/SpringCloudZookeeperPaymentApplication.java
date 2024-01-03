package cn.wickson.cloud.singleton.zookeeper.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

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
