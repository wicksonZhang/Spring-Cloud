package cn.wickson.cloud.stream.producer.controller;

import cn.wickson.cloud.stream.producer.service.IMessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 生产者控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@Slf4j
@Validated
@RestController
public class ProducerController {

    @Resource
    private IMessageProvider messageProvider;

    @Resource
    private SimpMessagingTemplate messagingTemplate;



    @MessageMapping(value = "/from-client")
    public void sendMessage() {
        messagingTemplate.convertAndSend("/producer/sendMessage", messageProvider.sendMessage());
    }

}
