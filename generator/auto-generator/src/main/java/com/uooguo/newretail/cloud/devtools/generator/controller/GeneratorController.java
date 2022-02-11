package com.uooguo.newretail.cloud.devtools.generator.controller;

import com.uooguo.newretail.cloud.devtools.generator.properties.DevtoolsProperties;
import com.uooguo.newretail.cloud.devtools.generator.service.GenerateService;
import com.uooguo.newretail.cloud.devtools.generator.service.TableService;
import com.uooguo.newretail.cloud.devtools.generator.task.GenerateTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码生成控制器
 *
 * @author Tiangel
 * @Date 2017年11月30日16:39:19
 */
@RestController
@Api(value = "/generator", tags = "代码生成器接口")
@RequestMapping("/generator")
public class GeneratorController {

    @Autowired
    private TableService tableService;

    @Autowired
    private GenerateService generateService;

    @Autowired
    private DevtoolsProperties devtoolsProperties;

    /**
     * 获取环境信息
     */
    @GetMapping("/env")
    @ApiOperation("获取代码生成器配置")
    public Object blackboard(String keyword) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("tables", tableService.getAllTables(keyword));
        hashMap.put("params", devtoolsProperties);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("code", 0);
        map.put("data", hashMap);
        map.put("msg", "操作成功");
        return map;
    }

    /**
     * 生成代码
     */
    @ApiOperation("生成代码")
    @PostMapping("/generate")
    public Object generate(GenerateTask generateTask) {
        generateService.generate(generateTask);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("code", 0);
        map.put("msg", "操作成功");
        return map;
    }
}
