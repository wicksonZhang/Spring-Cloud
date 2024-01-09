package cn.wickson.cloud.hystrix.dashboard.service;

import cn.wickson.cloud.common.utils.ResultUtil;

/**
 * 支付服务-应用服务类
 *
 * @author ZhangZiHeng
 * @date 2024-01-08
 */
public interface IPaymentService {

    /**
     * 成功
     *
     * @return String
     */
    ResultUtil paymentBySuccess();

    /**
     * 连接超时
     *
     * @return String
     */
    ResultUtil paymentByTimeOut();

    /**
     * 服务熔断
     *
     * @param id
     * @return
     */
    ResultUtil paymentCircuitBreaker(Long id);
}
