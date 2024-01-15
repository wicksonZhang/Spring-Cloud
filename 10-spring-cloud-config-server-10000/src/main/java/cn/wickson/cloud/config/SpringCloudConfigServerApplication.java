package cn.wickson.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Spring Cloud Config 服务端-启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-15
 */
@EnableEurekaClient
@EnableConfigServer
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigServerApplication.class, args);
    }

}