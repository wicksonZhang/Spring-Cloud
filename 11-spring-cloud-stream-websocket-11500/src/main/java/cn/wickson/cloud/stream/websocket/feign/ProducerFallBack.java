package cn.wickson.cloud.stream.websocket.feign;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.exception.UserOperationException;

/**
 * @author ZhangZiHeng
 * @date 2024-01-21
 */
public class ProducerFallBack implements ApiProducerFeign{
    @Override
    public String producerMessage() {
        throw UserOperationException.getInstance(ResultCodeEnum.SYSTEM_ERROR);
    }
}
