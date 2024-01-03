package cn.wickson.cloud.consul.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 支付服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2024-01-03
 */
@EnableDiscoveryClient
@SpringBootApplication
public class SpringCloudConsulPayment {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConsulPayment.class, args);
    }

}
