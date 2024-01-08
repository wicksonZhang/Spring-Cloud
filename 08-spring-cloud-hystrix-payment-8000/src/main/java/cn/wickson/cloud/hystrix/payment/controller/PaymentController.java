package cn.wickson.cloud.hystrix.payment.controller;

import cn.wickson.cloud.hystrix.payment.service.IPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Value("${server.port}")
    private Integer serverPort;

    @GetMapping("/hystrix/success")
    public String paymentSuccess() {
        return paymentService.paymentBySuccess();
    }

    @GetMapping("/hystrix/timeOut")
    public String paymentTimeOut() {
        return paymentService.paymentByTimeOut();
    }


}
