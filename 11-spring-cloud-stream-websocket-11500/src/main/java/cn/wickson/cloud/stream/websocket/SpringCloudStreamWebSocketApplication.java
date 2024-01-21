package cn.wickson.cloud.stream.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Spring Cloud Stream Websocket 启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@EnableHystrix // 启用 Hystrix 断路器
@EnableEurekaClient // 启用 Eureka 客户端
@EnableFeignClients // 启用 Feign 客户端
@SpringBootApplication
public class SpringCloudStreamWebSocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamWebSocketApplication.class, args);
    }

}
