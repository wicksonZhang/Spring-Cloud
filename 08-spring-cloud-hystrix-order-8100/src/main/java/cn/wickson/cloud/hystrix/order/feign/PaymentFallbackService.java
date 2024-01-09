package cn.wickson.cloud.hystrix.order.feign;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.utils.ResultUtil;
import org.springframework.stereotype.Component;

/**
 * @author ZhangZiHeng
 * @date 2024-01-09
 */
@Component
public class PaymentFallbackService implements IPaymentHystrixService {

    @Override
    public ResultUtil paymentBySuccess() {
        return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResultUtil paymentByTimeOut() {
        return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResultUtil paymentCircuitBreaker(Long id) {
        return ResultUtil.failure(ResultCodeEnum.SYSTEM_ERROR);
    }
}
