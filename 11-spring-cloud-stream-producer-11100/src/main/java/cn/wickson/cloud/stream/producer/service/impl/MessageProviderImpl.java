package cn.wickson.cloud.stream.producer.service.impl;

import cn.hutool.core.lang.UUID;
import cn.wickson.cloud.stream.producer.service.IMessageProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import javax.annotation.Resource;

/**
 * 消息生产者-实现类
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@Slf4j
@EnableBinding(Source.class) // 定义消息的推送管道
public class MessageProviderImpl implements IMessageProvider {

    // 消息发送管道
    @Resource
    private MessageChannel output;

    @Override
    public String producerMessage() {
        String uuid = UUID.randomUUID().toString();
        output.send(MessageBuilder.withPayload(uuid).build());
        return "生产者生产一条消息：" + uuid;
    }
}
