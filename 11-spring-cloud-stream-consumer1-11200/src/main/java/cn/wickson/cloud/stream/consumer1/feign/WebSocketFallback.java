package cn.wickson.cloud.stream.consumer1.feign;

import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.exception.UserOperationException;
import org.springframework.stereotype.Component;

/**
 * @author ZhangZiHeng
 * @date 2024-01-21
 */
@Component
public class WebSocketFallback implements ApiWebSocketFeign {

    @Override
    public void consumer1ReceiveMessage(String message) {
        throw UserOperationException.getInstance(ResultCodeEnum.SYSTEM_ERROR);
    }

}
