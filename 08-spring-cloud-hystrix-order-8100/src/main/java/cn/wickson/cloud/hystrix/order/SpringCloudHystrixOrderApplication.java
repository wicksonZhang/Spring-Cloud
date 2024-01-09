package cn.wickson.cloud.hystrix.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 订单服务-启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-08
 */
@EnableHystrix // 启用 Hystrix 断路器
@EnableEurekaClient // 启用 Eureka 客户端
@EnableFeignClients // 启用 Feign 客户端
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SpringCloudHystrixOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudHystrixOrderApplication.class, args);
    }

}

