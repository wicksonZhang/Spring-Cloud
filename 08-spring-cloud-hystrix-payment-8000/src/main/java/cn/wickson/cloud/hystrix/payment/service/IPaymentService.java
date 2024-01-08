package cn.wickson.cloud.hystrix.payment.service;

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
    String paymentBySuccess();

    /**
     * 连接超时
     *
     * @return String
     */
    String paymentByTimeOut();

}
