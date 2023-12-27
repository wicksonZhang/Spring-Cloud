package cn.wickson.cloud.common.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 支付实体类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment implements Serializable {

    /**
     * 支付ID
     */
    private Long id;

    /**
     * 支付金额
     */
    private String amount;

}
