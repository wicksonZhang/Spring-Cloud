package cn.wickson.cloud.hystrix.order.controller;

import cn.wickson.cloud.common.utils.ResultUtil;
import cn.wickson.cloud.hystrix.order.feign.IPaymentHystrixService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 订单服务-控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-08
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IPaymentHystrixService paymentHystrixService;

    @GetMapping("/getPaymentSuccess")
    public ResultUtil getPaymentSuccess() {
        return paymentHystrixService.paymentBySuccess();
    }

    @GetMapping("/getPaymentTimeOut")
    public ResultUtil getPaymentTimeOut() {
        return paymentHystrixService.paymentByTimeOut();
    }

    @GetMapping("/getPaymentCircuitBreaker/{id}")
    public ResultUtil getPaymentCircuitBreaker(@PathVariable("id") Long id) {
        return paymentHystrixService.paymentCircuitBreaker(id);
    }

}
