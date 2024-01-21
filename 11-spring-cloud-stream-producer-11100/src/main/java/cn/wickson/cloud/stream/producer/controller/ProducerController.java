package cn.wickson.cloud.stream.producer.controller;

import cn.wickson.cloud.stream.producer.service.IMessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/producer")
public class ProducerController {

    @Resource
    private IMessageProvider messageProvider;

    @GetMapping(value = "/producer-message")
    public String producerMessage() {
         return messageProvider.producerMessage();
    }

}
