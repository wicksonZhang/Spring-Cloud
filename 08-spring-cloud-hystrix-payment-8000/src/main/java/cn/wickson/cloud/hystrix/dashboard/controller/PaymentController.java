package cn.wickson.cloud.hystrix.dashboard.controller;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.utils.ResultUtil;
import cn.wickson.cloud.hystrix.dashboard.service.IPaymentService;
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
 * 支付微服务-控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-08
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private IPaymentService paymentService;

    @GetMapping("/hystrix/success")
    public ResultUtil paymentSuccess() {
        return paymentService.paymentBySuccess();
    }

    @GetMapping("/hystrix/timeOut")
    public ResultUtil paymentTimeOut() {
        return paymentService.paymentByTimeOut();
    }

    @HystrixCommand(fallbackMethod = "paymentCircuitBreakerFallback",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间范围
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), //失败率达到多少后跳闸
            })
    @GetMapping("/hystrix/paymentCircuitBreaker/{id}")
    public ResultUtil paymentCircuitBreaker(@PathVariable("id") Long id) {
        return paymentService.paymentCircuitBreaker(id);
    }

    public ResultUtil paymentCircuitBreakerFallback(@PathVariable("id") Long id) {
        return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR, "Fallback: Payment Timeout. Please try again later.");
    }


}
