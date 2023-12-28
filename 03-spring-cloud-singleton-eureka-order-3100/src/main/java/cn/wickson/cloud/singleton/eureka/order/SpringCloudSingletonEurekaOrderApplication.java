package cn.wickson.cloud.singleton.eureka.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 *
 * @author ZhangZiHeng
 * @date 2023-12-28
 */
@EnableEurekaClient
@SpringBootApplication
public class SpringCloudSingletonEurekaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudSingletonEurekaOrderApplication.class, args);
    }

}
