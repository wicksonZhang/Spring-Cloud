package cn.wickson.cloud.stream.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 网关-启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-20
 */
@EnableEurekaClient
@SpringBootApplication
public class SpringCloudStreamGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamGatewayApplication.class, args);
    }

}
