package cn.wickson.cloud.stream.producer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket通讯参数配置类
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册stomp端点，并将端点映射到特定的路径
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册一个断点，用于握手的地址
        registry.addEndpoint("/msgServer")
                // 允许websocket跨域
                .setAllowedOrigins("*")
                // 启用websocket备选方案（浏览器不支持的话就会启动）
                .withSockJS();
    }

    /**
     * 消息发送或广播的前缀的设置
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 客户端订阅消息的前缀，用于客户端订阅，进行消息广播发送的前缀，可对下面如“/producer”等地址将会推送相关的消息
        registry.enableSimpleBroker("/producer");
        // 客户端与服务端交互的前缀，前端发送信息给后端的前缀
        registry.setApplicationDestinationPrefixes("/server");
    }


}
