package cn.wickson.cloud.common.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 支付请求VO
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateReqVO {

    /**
     * 支付ID
     */
    @NotNull(message = "支付ID不为空")
    private Long id;

    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不为空")
    private BigDecimal amount;

}
