package cn.wickson.cloud.openfeign.order.feign;

import cn.wickson.cloud.common.model.dto.PaymentRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 远程调用支付服务接口
 *
 * @author ZhangZiHeng
 * @date 2024-01-05
 */
@Component
@FeignClient(value = "SPRING-CLOUD-CLUSTER-EUREKA-PAYMENT")
public interface IPaymentFeignService {

    @GetMapping("/payment/getById/{id}")
    public PaymentRespDTO getPaymentById(@PathVariable("id") Long id);

}
