package cn.wickson.cloud.stream.producer.controller;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.utils.ResultUtil;
import cn.wickson.cloud.stream.producer.service.IMessageProvider;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @HystrixCommand(fallbackMethod = "producerCircuitBreakerFallback",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间范围
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), //失败率达到多少后跳闸
            })
    @GetMapping(value = "/producer-message")
    public String producerMessage() {
//         int index = 1 / 0;
         return messageProvider.producerMessage();
    }

    public String producerCircuitBreakerFallback() {
        return "Fallback: Producer Timeout. Please try again later.";
    }

}
