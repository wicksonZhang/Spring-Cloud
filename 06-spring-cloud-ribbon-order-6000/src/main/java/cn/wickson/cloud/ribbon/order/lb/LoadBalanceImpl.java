package cn.wickson.cloud.ribbon.order.lb;

import org.springframework.cloud.client.ServiceInstance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认负载均衡算法
 *
 * @author ZhangZiHeng
 * @date 2024-01-05
 */
public class LoadBalanceImpl implements ILoadBalance {

    private AtomicInteger nextServerCyclicCounter;

    public LoadBalanceImpl() {
        this.nextServerCyclicCounter = new AtomicInteger(0);
    }

    private int incrementAndGetModulo(int modulo) {
        int current;
        int next;
        do {
            current = this.nextServerCyclicCounter.get();
            next = (current + 1) % modulo;
        } while (!this.nextServerCyclicCounter.compareAndSet(current, next));
        return next;
    }

    @Override
    public ServiceInstance instances(List<ServiceInstance> serviceInstances) {
        int index = incrementAndGetModulo(serviceInstances.size());
        return serviceInstances.get(index);
    }
}
