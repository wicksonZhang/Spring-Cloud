package cn.wickson.cloud.stream.consumer2.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消费者监听器
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
@Slf4j
@Component
@EnableBinding(Sink.class)
public class Consumer2MessageListener {

    @Value("${server.port}")
    private String serverPort;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    @StreamListener(Sink.INPUT)
    public void input(Message<String> message) {
        messagingTemplate.convertAndSend("/consumer2/receive-message", "消费者1接收到消息：" + message.getPayload());
        log.info("Server.Port:{} , Consumer1MessageListener receive message :{}", serverPort, message.getPayload());
    }

}
