package cn.wickson.cloud.cluster.eureka.server1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 服务端1-集群节点注册中心
 *
 * @author ZhangZiHeng
 * @date 2024-01-02
 */
@EnableEurekaServer
@SpringBootApplication
public class SpringCloudClusterEurekaServer1Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudClusterEurekaServer1Application.class, args);
    }

}
