package cn.wickson.cloud.common.exception;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import lombok.Getter;

/**
 * 前端用户操作错误异常类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Getter
public class UserOperationException extends RuntimeException {

    /**
     * 异常代码
     */
    private final ResultCodeEnum code;

    /**
     * 异常代码描述
     */
    private final String description;

    /**
     * 构造器：有参数的构造器
     */
    public UserOperationException(final ResultCodeEnum code) {
        this.code = code;
        this.description = code.getDescription();
    }

    /**
     * 构造器：有参数的构造器
     */
    public UserOperationException(final ResultCodeEnum code, final String message) {
        this.code = code;
        this.description = String.format(code.getDescription() + "{%s}", message);
    }

    /**
     * 格式化为字符串
     *
     * @return
     */
    @Override
    public String toString() {
        return "UserOperationException{" +
                "code=" + code.getCode() +
                ", message='" + description + '\'' +
                '}';
    }

    /**
     * 获取类实例
     *
     * @param code
     * @return
     */
    public static UserOperationException getInstance(final ResultCodeEnum code) {
        return new UserOperationException(code);
    }

    /**
     * 获取类实例
     *
     * @param code
     * @return
     */
    public static UserOperationException getInstance(final ResultCodeEnum code, final String message) {
        return new UserOperationException(code, message);
    }

}





