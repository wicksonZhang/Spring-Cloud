package cn.wickson.cloud.common.model.dto;

import cn.wickson.cloud.common.utils.ResultUnpacked;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRespDTO implements ResultUnpacked, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 支付ID
     */
    private Long id;

    /**
     * 支付金额
     */
    private String amount;

    /**
     * 请求端口
     */
    private int port;

    /**
     * 获取类实例
     *
     * @return
     */
    public static PaymentRespDTO getInstance() {
        return new PaymentRespDTO();
    }

}
