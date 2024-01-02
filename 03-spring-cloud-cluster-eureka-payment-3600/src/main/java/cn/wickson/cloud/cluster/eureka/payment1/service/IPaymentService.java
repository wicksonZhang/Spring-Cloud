package cn.wickson.cloud.cluster.eureka.payment1.service;

import cn.wickson.cloud.common.model.dto.PaymentRespDTO;
import cn.wickson.cloud.common.model.vo.PaymentCreateReqVO;

/**
 * 支付服务-应用服务类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
public interface IPaymentService {

    /**
     * 创建支付订单信息
     *
     * @param paymentVO 支付info
     */
    void create(PaymentCreateReqVO paymentVO);

    /**
     * 返回指定信息
     *
     * @param id 订单id
     * @return PaymentRespDTO
     */
    PaymentRespDTO getById(Long id);
}
