package cn.wickson.cloud.gateway.filter;

import cn.hutool.json.JSONUtil;
import cn.wickson.cloud.common.enums.ResultCodeEnum;
import cn.wickson.cloud.common.utils.ResultUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 用户认证-过滤器类
 */
@Component
public class UserAuthenticationFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String username = request.getQueryParams().getFirst("username");
        if (username == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
            ResultUtil result = ResultUtil.failure(ResultCodeEnum.TOKEN_ISNULL_ERROR);
            DataBuffer dataBuffer = response.bufferFactory().wrap(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Flux.just(dataBuffer));
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}