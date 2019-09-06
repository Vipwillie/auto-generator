package com.uooguo.newretail.cloud.devtools.generator.task;

import lombok.Data;

/**
 * @author Tiangel
 * @date 2018-4-3
 **/
@Data
public class GenerateTask {

    private String projectLocation;

    private String tablePrefix;

    private String packageName;

    private String[] tableNames;

    private String[] entityNames;

    private String module;

    private String developer;

    private Boolean onlyGenerateEntity;
}
