package cn.wickson.cloud.consul.payment.controller;

import cn.wickson.cloud.common.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * 支付服务-控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-03
 */
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/consul")
    public ResultUtil paymentConsul() {
        return ResultUtil.success("Spring Cloud with consul port: " + serverPort + ", UUID：" + UUID.randomUUID().toString());
    }

}

