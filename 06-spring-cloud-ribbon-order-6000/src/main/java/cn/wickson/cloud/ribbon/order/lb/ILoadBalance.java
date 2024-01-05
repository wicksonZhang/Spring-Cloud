package cn.wickson.cloud.ribbon.order.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * @author ZhangZiHeng
 * @date 2024-01-05
 */
public interface ILoadBalance {

    ServiceInstance instances(List<ServiceInstance> serviceInstances);

}
