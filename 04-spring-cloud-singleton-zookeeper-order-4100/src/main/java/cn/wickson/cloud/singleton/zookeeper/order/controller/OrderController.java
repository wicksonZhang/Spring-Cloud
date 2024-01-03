package cn.wickson.cloud.singleton.zookeeper.order.controller;

import cn.wickson.cloud.common.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 订单服务-控制类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    public static final String PAYMENT_URL = "http://spring-cloud-zookeeper-payment-4000";

    @Resource
    private RestTemplate restTemplate;

    /**
     * 获取订单
     *
     * @return ResultUtil
     */
    @GetMapping("/getPayment/zk")
    public ResultUtil getPayment() {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/zk" , ResultUtil.class);
    }

}
