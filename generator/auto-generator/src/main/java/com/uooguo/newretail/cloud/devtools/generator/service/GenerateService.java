package com.uooguo.newretail.cloud.devtools.generator.service;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.BeetlTemplateEngine;
import com.uooguo.newretail.cloud.devtools.generator.task.GenerateTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tiangel
 * @date 2018-4-3
 **/
@Slf4j
@Service
public class GenerateService {

    private GlobalConfig globalConfig;

    private DataSourceConfig dataSourceConfig;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    private StrategyConfig strategyConfig;

    private PackageConfig packageConfig;

    private EntityNameRewriter entityNameRewriter;

    @PostConstruct
    public void init() {

        globalConfig = new GlobalConfig();
        //定义数据库日期转换类型
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setFileOverride(true);
        globalConfig.setActiveRecord(false);
        // XML 二级缓存
        globalConfig.setEnableCache(false);
        // XML ResultMap
        globalConfig.setBaseResultMap(true);
        // XML columList
        globalConfig.setBaseColumnList(true);
        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");

        // 数据源配置
        dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        //数据库字段类型与实体类之间的类型转换
/*        dataSourceConfig.setTypeConvert(new MySqlTypeConvert() {
            // 自定义数据库表字段类型转换【可选】
            public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                log.info("转换类型：{}", fieldType);
                if (fieldType.toLowerCase().contains("tinyint(1)")) {
                    return DbColumnType.BOOLEAN;
                }
                // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                return super.processTypeConvert(globalConfig, fieldType);
            }
        });*/
        dataSourceConfig.setDriverName(dataSourceProperties.getDriverClassName());
        dataSourceConfig.setUsername(dataSourceProperties.getUsername());
        dataSourceConfig.setPassword(dataSourceProperties.getPassword());
        dataSourceConfig.setUrl(dataSourceProperties.getUrl());

        // 策略配置
        strategyConfig = new StrategyConfig();
        // 表名生成策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        // 需要生成的表
        // 自定义 mapper 父类
        strategyConfig.setSuperMapperClass("tk.mybatis.mapper.common.Mapper");
        // 自定义 service 父类
        strategyConfig.setSuperServiceClass(null);
        // 自定义 service 实现类父类
        strategyConfig.setSuperServiceImplClass(null);
        // 自定义 controller 父类
//        strategyConfig.setSuperControllerClass("com.uooguo.newretail.cloud.framework.base.controller.BaseController");
        // 【实体】是否为lombok模型（默认 false）
        strategyConfig.setEntityLombokModel(true);
        // 为true时使用@restControllerStyle
        strategyConfig.setRestControllerStyle(true);
        // Boolean类型字段是否移除is前缀处理
        strategyConfig.setEntityBooleanColumnRemoveIsPrefix(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
//        strategyConfig.setEntityBuilderModel(true);
        // 自定义需要填充的字段
        List<TableFill> tableFillList = new ArrayList<>();
        tableFillList.add(new TableFill("gmt_create", FieldFill.INSERT));
        tableFillList.add(new TableFill("gmt_modified", FieldFill.INSERT_UPDATE));
        strategyConfig.setTableFillList(tableFillList);

        packageConfig = new PackageConfig();
        packageConfig.setController("controller");
        packageConfig.setEntity("entity");
        packageConfig.setMapper("mapper");
        packageConfig.setService("service");
        packageConfig.setServiceImpl("service.impl");

        entityNameRewriter = new EntityNameRewriter();

    }


    /**
     * 生成代码
     *
     * @param generateTask
     */
    public void generate(GenerateTask generateTask) {
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setTemplateEngine(new BeetlTemplateEngine());
        //只生成实体类
        if (generateTask.getOnlyGenerateEntity() != null && generateTask.getOnlyGenerateEntity()) {
            TemplateConfig templateConfig = new TemplateConfig();
            templateConfig.setXml(null);
            templateConfig.setController(null);
            templateConfig.setMapper(null);
            templateConfig.setService(null);
            templateConfig.setServiceImpl(null);
            autoGenerator.setTemplate(templateConfig);
        } else {
            // 关闭默认 xml 生成，调整生成 至 根目录
            TemplateConfig templateConfig = new TemplateConfig();
            templateConfig.setXml(null);
            autoGenerator.setTemplate(templateConfig);
            entityNameRewriter.setFileOutConfigList(Collections.singletonList(new FileOutConfig("/templates/btl/mapper.xml.btl") {
                // 自定义输出文件目录
                @Override
                public String outputFile(TableInfo tableInfo) {
                    String path = generateTask.getProjectLocation() + "/src/main/resources/mapper/";
                    File dir = new File(path);
                    if (!dir.exists()) {
                        boolean result = dir.mkdirs();
                        if (result) {
                            System.out.println("创建目录： [" + path + "]");
                        }
                    }
                    return path + tableInfo.getMapperName() + ".xml";
                }
            }));
        }

        // 全局配置
        globalConfig.setOutputDir(generateTask.getProjectLocation() + "/src/main/java");
        globalConfig.setAuthor(generateTask.getDeveloper());
        // 此处可以修改为您的表前缀
        strategyConfig.setTablePrefix(generateTask.getTablePrefix());
        strategyConfig.setInclude(generateTask.getTableNames());
        // 包配置
        packageConfig.setParent(generateTask.getPackageName());
        packageConfig.setModuleName(generateTask.getModule());

        // 执行生成
        autoGenerator.setCfg(entityNameRewriter);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setGlobalConfig(globalConfig);
        autoGenerator.setPackageInfo(packageConfig);

        autoGenerator.execute();
    }

}
