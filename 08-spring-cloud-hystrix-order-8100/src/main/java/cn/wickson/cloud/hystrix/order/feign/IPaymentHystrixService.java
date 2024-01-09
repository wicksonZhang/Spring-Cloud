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
    @GetMapping("/payment/hystrix/paymentCircuitBreaker/{id}")
    public ResultUtil paymentCircuitBreaker(@PathVariable("id") Long id);

}
