package cn.wickson.cloud.singleton.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 服务端-单机节点注册中心
 *
 * @author ZhangZiHeng
 * @date 2023-12-28
 */
@EnableEurekaServer
@SpringBootApplication
public class SpringCloudSingletonEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSingletonEurekaServerApplication.class, args);
    }

}