package cn.wickson.cloud.stream.consumer2.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * OpenFeign 调用
 *
 * @author ZhangZiHeng
 * @date 2024-01-21
 */
@Component
@FeignClient(value = "SPRING-CLOUD-STREAM-WEBSOCKET", fallback = WebSocketFallback.class)
public interface ApiWebSocketFeign {

    /**
     * 消费者接收消息
     *
     * @param message 消息
     */
    @GetMapping("/websocket/consumer2ReceiveMessage/{message}")
    void consumer2ReceiveMessage(@PathVariable("message") String message);
}
