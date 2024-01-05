package cn.wickson.cloud.openfeign.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OpenFeign 主启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-05
 */
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"cn.wickson.cloud"})
public class SpringCloudOpenFeignOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudOpenFeignOrderApplication.class, args);
    }

}
