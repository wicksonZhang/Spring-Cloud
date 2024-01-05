package cn.wickson.cloud.common.handle;

import cn.hutool.json.JSONUtil;
import cn.wickson.cloud.common.utils.ResultUnpacked;
import cn.wickson.cloud.common.utils.ResultUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 数据结果统一格式全局处理类
 *
 * @author ZhangZiHeng
 * @date 2023-12-27
 */
@ControllerAdvice(
        basePackages = {
                "cn.wickson.cloud.payment.controller",
                "cn.wickson.cloud.order.controller",
                "cn.wickson.cloud.cluster.eureka.order.controller",
                "cn.wickson.cloud.cluster.eureka.payment1.controller",
                "cn.wickson.cloud.cluster.eureka.payment2.controller",
                "cn.wickson.cloud.singleton.zookeeper.payment.controller",
                "cn.wickson.cloud.singleton.zookeeper.order.controller",
                "cn.wickson.cloud.singleton.consul.payment.controller",
                "cn.wickson.cloud.singleton.consul.order.controller",
                "cn.wickson.cloud.ribbon.order.controller",
                "cn.wickson.cloud.openfeign.order.controller"
        })
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    /**
     * 是否支持advice功能
     *
     * @return 返回值：true=表示开启， false=表示关闭
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * 处理response的具体业务方法
     */
    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 校验Controller层传递过来的值是否为String类结构的数据，为真则转成Json格式，以保持统一格式返回客户端
        if (body instanceof String) {
            return JSONUtil.toJsonStr(ResultUtil.success(body));
        }

        // 防止重复包裹的问题出现
        if (body instanceof ResultUtil) {
            return body;
        }

        // 如果body实现 ResultUnpacked 接口类，则不需要返回统一结果封装
        if (body instanceof ResultUnpacked) {
            return body;
        }

        // 返回结果值给客户端
        return ResultUtil.success(body);
    }

}