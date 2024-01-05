package cn.wickson.cloud.ribbon.order.controller;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 订单服务-控制类
 *
 * @author ZhangZiHeng
 * @date 2023-01-04
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
     * 获取订单
     * restTemplate.getForObject：返回对象为响应体中数据转化成的对象，基本上可以理解为 Json
     *
     * @param id id
     * @return ResultUtil
     */
    @GetMapping("/getPayment/{id}")
    public ResultUtil getPaymentByObject(@PathVariable("id") Long id) {
        return restTemplate.getForObject(PAYMENT_URL + "/payment/getById/" + id, ResultUtil.class);
    }

    /**
     * 获取订单
     * restTemplate.getForEntity：返回的对象为 ResponseEntity 对象，包含一些重要信息
     *
     * @param id id
     * @return ResultUtil
     */
    @GetMapping("/getPayment/entity/{id}")
    public ResultUtil getPaymentByEntity(@PathVariable("id") Long id) {
        ResponseEntity<ResultUtil> entity = restTemplate.getForEntity(PAYMENT_URL + "/payment/getById/" + id, ResultUtil.class);
        if (entity.getStatusCode().is2xxSuccessful()) {
            return entity.getBody();
        } else {
            return ResultUtil.failure(ResultCodeEnum.FAILURE);
        }
    }

}
