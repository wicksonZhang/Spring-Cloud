package cn.wickson.cloud.stream.producer.service;

/**
 * 消息生产者
 *
 * @author ZhangZiHeng
 * @date 2024-01-19
 */
public interface IMessageProvider {

    /**
     * 生产者生产消息
     *
     * @return String
     */
    String producerMessage();

}
