package cn.wickson.cloud.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 订单服务-微服务启用类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@SpringBootApplication(scanBasePackages = {"cn.wickson.cloud"})
public class SpringCloudOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudOrderApplication.class, args);
    }

}
