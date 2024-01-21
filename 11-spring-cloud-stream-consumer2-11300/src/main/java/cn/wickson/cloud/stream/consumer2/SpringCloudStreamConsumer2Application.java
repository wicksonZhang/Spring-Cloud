package cn.wickson.cloud.stream.consumer2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Spring Cloud Stream 消费者1启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@EnableHystrix // 启用 Hystrix 断路器
@EnableEurekaClient // 启用 Eureka 客户端
@EnableFeignClients // 启用 Feign 客户端
@SpringBootApplication
public class SpringCloudStreamConsumer2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamConsumer2Application.class, args);
    }
}
