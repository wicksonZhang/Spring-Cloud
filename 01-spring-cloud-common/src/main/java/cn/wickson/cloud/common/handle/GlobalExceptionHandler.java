package cn.wickson.cloud.common.handle;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.exception.UserOperationException;
import cn.wickson.cloud.common.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * 全局异常统一处理类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 系统异常处理
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ResultUtil handleThrowable(Throwable e, HttpServletRequest request) {
        log.error("requestUrl：{}，系统内部异常", request.getRequestURI(), e);
        return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 自定义用户操作异常处理
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(UserOperationException.class)
    public ResultUtil handleUserOperationException(UserOperationException e, HttpServletRequest request) {
        log.error("requestUrl：{}，用户操作异常{code={}，message={}}", request.getRequestURI(), e.getCode().getCode(),
                e.getDescription(), e);
        return ResultUtil.failure(e.getCode(), e.getDescription());
    }


    /**
     * 请求参数校验异常处理方式一
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResultUtil handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.error("requestUrl：{}，参数校验失败", request.getRequestURI(), e);
        System.out.println(e.getMessage());
        return ResultUtil.failure(ResultCodeEnum.PARAM_REQUEST_DATA_FORMAT_INVALID);
    }

    /**
     * 请求参数校验异常处理方式二
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultUtil handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error("requestUrl：{}，参数校验失败", request.getRequestURI(), e);
        ResultCodeEnum codeEnum = ResultCodeEnum.PARAM_VALIDATED_FAILURE;
        String msg = this.messageFormat(codeEnum.getDescription(), e.getBindingResult().getFieldErrors());
        return ResultUtil.failure(codeEnum, msg);
    }

    /**
     * 请求参数校验异常处理方式三
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(BindException.class)
    public ResultUtil handleBindException(BindException e, HttpServletRequest request) {
        log.error("requestUrl：{}，参数校验失败", request.getRequestURI(), e);
        ResultCodeEnum codeEnum = ResultCodeEnum.PARAM_IS_INVALID;
        String msg = this.messageFormat(codeEnum.getDescription(), e.getFieldErrors());
        return ResultUtil.failure(ResultCodeEnum.PARAM_IS_INVALID, msg);
    }

    /**
     * 请求参数校验异常处理方式四
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultUtil handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        log.error("requestUrl：{}，参数校验失败", request.getRequestURI(), e);
        ResultCodeEnum codeEnum = ResultCodeEnum.PARAM_IS_BLANK;
        String msg = codeEnum.getDescription() + "[" + e.getMessage() + "]";
        return ResultUtil.failure(codeEnum, msg);
    }

    /**
     * 将validator数据校验异常信息格式化处理
     */
    private String messageFormat(String topic, List<FieldError> fieldErrorList) {
        StringBuilder msg = new StringBuilder();
        msg.append(topic);
        msg.append("：[");
        fieldErrorList.forEach(fieldError -> {
            msg.append(fieldError.getField());
            msg.append("=");
            msg.append(fieldError.getDefaultMessage());
            msg.append(" ");
        });
        msg.append("]");
        return msg.toString();
    }

}
