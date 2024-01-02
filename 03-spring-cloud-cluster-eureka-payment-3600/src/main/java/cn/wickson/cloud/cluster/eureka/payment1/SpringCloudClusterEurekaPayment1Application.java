package cn.wickson.cloud.cluster.eureka.payment1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 支付服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@EnableEurekaClient
@SpringBootApplication(scanBasePackages = {"cn.wickson.cloud"})
public class SpringCloudClusterEurekaPayment1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudClusterEurekaPayment1Application.class, args);
    }

}
