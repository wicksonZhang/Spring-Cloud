package cn.wickson.cloud.hystrix.order.feign;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.utils.ResultUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Feign远程调用接口支付接口
 *
 * @author ZhangZiHeng
 * @date 2024-01-09
 */
@Component
@FeignClient(value = "SPRING-CLOUD-HYSTRIX-PAYMENT", fallback = PaymentFallbackService.class)
public interface IPaymentHystrixService {

    /**
     * 成功
     *
     * @return String
     */
    @GetMapping("/payment/hystrix/success")
    public ResultUtil paymentBySuccess();

    /**
     * 连接超时
     *
     * @return String
     */
    @GetMapping("/payment/hystrix/timeOut")
    public ResultUtil paymentByTimeOut();

    /**
     * 服务熔断
     *
     * @return
     */
//    @HystrixCommand(fallbackMethod =
//            "paymentCircuitBreakerFallback", commandProperties = {
//            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"), //是否开启断路器
//            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"), //请求次数
//            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"), //时间范围
//            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"), //失败率达到多少后跳闸
//    })
    @GetMapping("/payment/hystrix/paymentCircuitBreaker/{id}")
    public ResultUtil paymentCircuitBreaker(@PathVariable("id") Long id);

    /**
     * 降级处理方法，与 @HystrixCommand 中指定的 fallbackMethod 名称一致
     */
    default ResultUtil paymentCircuitBreakerFallback(){
        return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR, "Fallback: Payment Timeout. Please try again later.");
    }

}
