package cn.wickson.cloud.stream.websocket.controller;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.utils.ResultUtil;
import cn.wickson.cloud.stream.websocket.feign.ApiProducerFeign;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * WebSocket控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    private ApiProducerFeign apiProducerFeign;

    /**
     * 生产消息
     */
    @MessageMapping(value = "/from-client")
    public void producerMessage() {
        messagingTemplate.convertAndSend("/producer/producer-message", apiProducerFeign.producerMessage());
    }

    /**
     * 消费者1消费消息
     *
     * @param message 消息
     */
    @GetMapping(value = "/consumer1ReceiveMessage/{message}")
    public void consumer1ReceiveMessage(@PathVariable("message") String message) {
        messagingTemplate.convertAndSend("/consumer1/receive-message", message);
    }

    /**
     * 消费者2消费消息
     *
     * @param message 消息
     */
    @GetMapping(value = "/consumer2ReceiveMessage/{message}")
    public void consumer2ReceiveMessage(@PathVariable("message") String message) {
        messagingTemplate.convertAndSend("/consumer2/receive-message", message);
    }

}
