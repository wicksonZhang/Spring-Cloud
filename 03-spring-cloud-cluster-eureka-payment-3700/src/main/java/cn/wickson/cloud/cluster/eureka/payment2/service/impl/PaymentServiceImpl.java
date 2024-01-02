package cn.wickson.cloud.cluster.eureka.payment2.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.wickson.cloud.cluster.eureka.payment2.convert.PaymentConvert;
import cn.wickson.cloud.cluster.eureka.payment2.service.IPaymentService;
import cn.wickson.cloud.common.model.dto.PaymentRespDTO;
import cn.wickson.cloud.common.model.entity.Payment;
import cn.wickson.cloud.common.model.vo.PaymentCreateReqVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 支付服务-应用服务实现类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Service
public class PaymentServiceImpl implements IPaymentService {

    private static final List<Payment> LIST = new ArrayList<>();

    /**
     * 创建支付信息
     *
     * @param paymentVO 支付info
     */
    @Override
    public void create(final PaymentCreateReqVO paymentVO) {
        Payment payment = PaymentConvert.INSTANCE.toEntity(paymentVO);
        LIST.add(payment);
    }

    /**
     * 返回支付信息
     *
     * @param id 订单id
     * @return PaymentRespDTO
     */
    @Override
    public PaymentRespDTO getById(final Long id) {
        if (CollUtil.isEmpty(LIST)) {
            return new PaymentRespDTO();
        }

        PaymentRespDTO paymentRespDTO = new PaymentRespDTO();
        for (Payment payment : LIST) {
            Long paymentId = payment.getId();
            if (paymentId.equals(id)) {
                paymentRespDTO = PaymentConvert.INSTANCE.toDTO(payment);
                paymentRespDTO.setPort(3700);
                break;
            }
        }
        return paymentRespDTO;
    }
}
