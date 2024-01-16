package cn.wickson.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Spring Cloud Config 客户端-启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-16
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudConfigClient2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigClient2Application.class, args);
    }

}
