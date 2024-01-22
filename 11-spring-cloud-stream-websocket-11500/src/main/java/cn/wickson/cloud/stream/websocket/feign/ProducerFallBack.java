package cn.wickson.cloud.stream.websocket.feign;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.exception.UserOperationException;
import org.springframework.stereotype.Component;

/**
 * @author ZhangZiHeng
 * @date 2024-01-21
 */
@Component
public class ProducerFallBack implements ApiProducerFeign {
    @Override
    public String producerMessage() {
        throw UserOperationException.getInstance(ResultCodeEnum.SYSTEM_ERROR);
    }
}
