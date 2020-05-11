package com.uooguo.newretail.cloud.devtools.generator;

import com.uooguo.newretail.cloud.devtools.generator.properties.DevtoolsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author Tiangel
 * @date 2018-3-13
 **/
@SpringBootApplication
@EnableConfigurationProperties(DevtoolsProperties.class)
public class GeneratorApplication {

    public static void main(String[] args){
        SpringApplication.run(GeneratorApplication.class,args);
    }
}
