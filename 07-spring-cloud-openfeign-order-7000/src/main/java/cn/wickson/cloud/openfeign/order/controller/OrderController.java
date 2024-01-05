package cn.wickson.cloud.openfeign.order.controller;

import cn.wickson.cloud.openfeign.order.feign.IPaymentFeignService;
import cn.wickson.cloud.common.model.dto.PaymentRespDTO;
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
 * @date 2023-01-04
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private IPaymentFeignService paymentFeignService;

    /**
     * 获取订单
     * restTemplate.getForObject：返回对象为响应体中数据转化成的对象，基本上可以理解为 Json
     *
     * @param id id
     * @return ResultUtil
     */
    @GetMapping("/getPayment/{id}")
    public PaymentRespDTO getPaymentByObject(@PathVariable("id") Long id) {
        PaymentRespDTO paymentRespDTO = paymentFeignService.getPaymentById(id);
        log.debug("paymentRespDTO: " + paymentRespDTO);
        return paymentRespDTO;
    }
}
