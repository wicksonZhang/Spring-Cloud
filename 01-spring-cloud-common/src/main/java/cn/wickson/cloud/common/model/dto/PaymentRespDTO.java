package cn.wickson.cloud.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 支付订单-返回DTO
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRespDTO {

    /**
     * 支付ID
     */
    private Long id;

    /**
     * 支付金额
     */
    private String amount;

}
