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
    SUCCESS(1, "成功"),
    /**
     * 失败状态码
     */
    FAILURE(0, "失败"),

    /**
     * 参数错误码：1001-1999
     */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),
    PARAM_REQUEST_DATA_FORMAT_INVALID(1005, "请求参数的数据格式错误"),
    PARAM_VALIDATED_FAILURE(1006, "参数校验失败"),

    /* 系统错误码：9001-9999 */
    SYSTEM_LACK_OF_CODE_ERROR(9997, "缺少代码错误"),
    USER_REQUEST_METHOD_INVALID(9998, "请求方法无效"),
    SYSTEM_ERROR(9999, "系统繁忙，请稍后重试...");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 状态码描述信息
     */
    private final String description;

}