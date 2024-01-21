package cn.wickson.cloud.stream.websocket.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 远程调用生产者服务接口
 *
 * @author ZhangZiHeng
 * @date 2024-01-21
 */
@Component
@FeignClient(value = "SPRING-CLOUD-STREAM-PRODUCER", fallback = ProducerFallBack.class)
public interface ApiProducerFeign {

    /**
     * 生产者生产消息
     *
     * @return String
     */
    @GetMapping("/producer/producer-message")
    String producerMessage();

}
