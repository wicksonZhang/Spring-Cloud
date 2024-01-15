package cn.wickson.cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Spring Cloud Config服务-启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-15
 */
@EnableConfigServer
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudConfigApplication.class, args);
    }

}
