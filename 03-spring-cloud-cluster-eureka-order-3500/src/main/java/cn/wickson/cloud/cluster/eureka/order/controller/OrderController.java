package cn.wickson.cloud.cluster.eureka.order.controller;

import cn.wickson.cloud.common.model.entity.Payment;
import cn.wickson.cloud.common.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    public static final String PAYMENT_URL = "http://SPRING-CLOUD-CLUSTER-EUREKA-PAYMENT";

    @Resource
    private RestTemplate restTemplate;

    /**
     * 调用支付信息
     *
     * @param payment 支付信息
     * @return ResultUtil
     */
    @PostMapping("/create")
    public ResultUtil create(@RequestBody Payment payment) {
        return restTemplate.postForObject(PAYMENT_URL + "/payment/create", payment, ResultUtil.class);
    }

    /**
     * 获取订单
     *
     * @param id id
     * @return ResultUtil
     */
    @GetMapping("/getPayment/{id}")
    public ResultUtil getPayment(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/getById/" + id, ResultUtil.class);
    }

}
