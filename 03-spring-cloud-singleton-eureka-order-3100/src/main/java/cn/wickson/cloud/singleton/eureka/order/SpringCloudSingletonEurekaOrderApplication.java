package cn.wickson.cloud.singleton.eureka.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 订单服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2023-12-28
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"cn.wickson.cloud"})
public class SpringCloudSingletonEurekaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSingletonEurekaOrderApplication.class, args);
    }

}
