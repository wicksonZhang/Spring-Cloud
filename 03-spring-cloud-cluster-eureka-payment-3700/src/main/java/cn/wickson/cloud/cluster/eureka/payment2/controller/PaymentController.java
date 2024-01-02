package cn.wickson.cloud.cluster.eureka.payment2.controller;

import cn.wickson.cloud.cluster.eureka.payment2.service.IPaymentService;
import cn.wickson.cloud.common.model.dto.PaymentRespDTO;
import cn.wickson.cloud.common.model.vo.PaymentCreateReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 支付服务-控制类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Resource
    private IPaymentService paymentService;

    /**
     * 创建支付订单
     *
     * @param paymentVO 订单信息
     */
    @PostMapping(value = "/create")
    public void create(@RequestBody PaymentCreateReqVO paymentVO) {
        /* Step-1：创建支付订单 */
        this.paymentService.create(paymentVO);
    }

    /**
     * 根据订单id获取支付订单
     *
     * @param id 订单信息
     */
    @GetMapping(value = "/getById/{id}")
    public PaymentRespDTO getById(@PathVariable("id") Long id) {
        /* Step-1： 获取支付订单 */
        return this.paymentService.getById(id);
    }

}
