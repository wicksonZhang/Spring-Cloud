package cn.wickson.cloud.cluster.eureka.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 订单服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2024-01-02
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudClusterEurekaOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudClusterEurekaOrderApplication.class, args);
    }

}
