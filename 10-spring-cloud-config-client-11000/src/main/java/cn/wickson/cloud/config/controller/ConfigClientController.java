package cn.wickson.cloud.config.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 客户端控制类
 *
 * @author ZhangZiHeng
 * @date 2024-01-15
 */
@Slf4j
@Validated
@RestController
@RefreshScope
@RequestMapping("/config-client")
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config-info")
    public String getConfigInfo() {
        return configInfo;
    }

}
