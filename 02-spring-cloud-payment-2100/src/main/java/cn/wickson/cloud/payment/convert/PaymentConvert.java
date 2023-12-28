package cn.wickson.cloud.payment.convert;

import cn.wickson.cloud.common.model.dto.PaymentRespDTO;
import cn.wickson.cloud.common.model.entity.Payment;
import cn.wickson.cloud.common.model.vo.PaymentCreateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 转换VO
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Mapper
public interface PaymentConvert {

    PaymentConvert INSTANCE = Mappers.getMapper(PaymentConvert.class);

    /**
     * 转实体
     *
     * @param paymentVO
     * @return Payment
     */
    Payment toEntity(PaymentCreateReqVO paymentVO);

    /**
     * 转DTO
     *
     * @param payment
     * @return PaymentRespDTO
     */
    PaymentRespDTO toDTO(Payment payment);
}
