package cn.wickson.cloud.openfeign.order.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenFeign 日志配置
 */
@Configuration
public class FeignConfig {

    /**
     * 1. `NONE`：默认的，不显示任何日志
     * 2. `Basic`：仅记录请求方法、URL、响应状态以及执行时间
     * 3. `Headers`：除了 `Basic` 中定义的信息之外，还有请求和响应头信息
     * 4. `Full`：除了 `Headers` 中定义的信息之外，还有请求和响应的正文以及元数据
     *
     * @return Logger.Level
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}
