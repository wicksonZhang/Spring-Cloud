package cn.wickson.cloud.hystrix.dashboard.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.exception.UserOperationException;
import cn.wickson.cloud.common.utils.ResultUtil;
import cn.wickson.cloud.hystrix.dashboard.service.IPaymentService;
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
    public ResultUtil paymentBySuccess() {
        return ResultUtil.success("ThreadPool：" + Thread.currentThread().getName() + ", payment service success");
    }

    /**
     * 连接超时
     *
     * @return String
     */
    @Override
    public ResultUtil paymentByTimeOut() {
        int timeNumber = 3;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ResultUtil.success("ThreadPool：" + Thread.currentThread().getName() + ", payment service timeout：" + timeNumber);
    }

    @Override
    public ResultUtil paymentCircuitBreaker(final Long id) {
        if (id < 0) {
            throw UserOperationException.getInstance(ResultCodeEnum.PARAM_IS_INVALID);
        }
        String serialNumber = IdUtil.simpleUUID();
        return ResultUtil.success("ThreadPool：" + Thread.currentThread().getName() + ", serialNumber：" + serialNumber);
    }
}
