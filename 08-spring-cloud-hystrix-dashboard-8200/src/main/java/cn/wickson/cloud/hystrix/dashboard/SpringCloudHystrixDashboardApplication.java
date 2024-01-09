package cn.wickson.cloud.hystrix.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * hystrix-dashboard 启动类
 *
 * @author ZhangZiHeng
 * @date 2024-01-08
 */
@EnableHystrixDashboard
@SpringBootApplication(scanBasePackages = "cn.wickson.cloud")
public class SpringCloudHystrixDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudHystrixDashboardApplication.class, args);
    }

}
