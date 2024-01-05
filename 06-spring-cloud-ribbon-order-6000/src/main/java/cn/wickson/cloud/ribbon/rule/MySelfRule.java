package cn.wickson.cloud.ribbon.rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义规则
 *
 * @author ZhangZiHeng
 * @date 2024-01-05
 */
@Configuration
public class MySelfRule {

    /**
     * 自定义随机规则
     *
     * @return IRule
     */
    @Bean
    public IRule myRule() {
        return new RandomRule();
    }

}
