package cn.wickson.cloud.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回客户端状态码枚举类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    /**
     * 成功状态码：1
     */
    SUCCESS(1, "成功");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码描述信息
     */
    private final String description;

}