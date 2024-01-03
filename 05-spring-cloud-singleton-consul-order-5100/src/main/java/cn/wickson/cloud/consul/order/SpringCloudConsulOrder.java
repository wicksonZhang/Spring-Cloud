package cn.wickson.cloud.consul.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 订单服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2024-01-03
 */
// 用于向 consul 或者 zookeeper 作为注册中心注册微服务
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudConsulOrder {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConsulOrder.class, args);
    }

}
