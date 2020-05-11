package com.uooguo.newretail.cloud.devtools.generator.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 代码生成配置
 *
 * @author Tiangel
 * @date 2017-12-13-下午4:53
 */
@Data
@Component
@ConfigurationProperties(prefix = "newretail.devtools")
public class DevtoolsProperties {

    /**
     * 项目路径
     */
    private String projectLocation;

    /**
     * 开发者
     */
    private String developer;

    /**
     * 表的前缀
     */
    private String tablePrefix;

}
