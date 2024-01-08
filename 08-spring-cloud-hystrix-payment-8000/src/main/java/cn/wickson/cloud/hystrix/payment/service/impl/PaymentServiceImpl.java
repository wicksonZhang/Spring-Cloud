package cn.wickson.cloud.hystrix.payment.service.impl;

import cn.wickson.cloud.hystrix.payment.service.IPaymentService;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 支付服务-应用服务实现类
 *
 * @author ZhangZiHeng
 * @date 2024-01-08
 */
@Service
public class PaymentServiceImpl implements IPaymentService {

    /**
     * 成功
     *
     * @return String
     */
    @Override
    public String paymentBySuccess() {
        return "ThreadPool：" + Thread.currentThread().getName() + ", payment service success";
    }

    /**
     * 连接超时
     *
     * @return String
     */
    @Override
    public String paymentByTimeOut() {
        int timeNumber = 3;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "ThreadPool：" + Thread.currentThread().getName() + ", payment service timeout：" + timeNumber;
    }

}
