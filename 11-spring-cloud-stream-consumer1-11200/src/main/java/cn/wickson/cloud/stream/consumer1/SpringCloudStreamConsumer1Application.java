package cn.wickson.cloud.stream.consumer1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Spring Cloud Stream 消费者1启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@EnableEurekaClient
@SpringBootApplication
public class SpringCloudStreamConsumer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamConsumer1Application.class, args);
    }
}
