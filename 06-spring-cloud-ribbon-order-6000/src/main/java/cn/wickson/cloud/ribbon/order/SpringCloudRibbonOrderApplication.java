package cn.wickson.cloud.ribbon.order;

import cn.wickson.cloud.ribbon.rule.MySelfRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;

/**
 * 订单服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2024-01-04
 */
@EnableDiscoveryClient
@SpringBootApplication(scanBasePackages = {"cn.wickson.cloud"})
@RibbonClient(name = "SPRING-CLOUD-CLUSTER-EUREKA-PAYMENT", configuration = MySelfRule.class)
public class SpringCloudRibbonOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudRibbonOrderApplication.class, args);
    }

}

