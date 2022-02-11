package com.uooguo.newretail.cloud.devtools.generator.service;

import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 获取数据库所有的表
 *
 * @author Tiangel
 * @date 2017-12-04-下午1:37
 */
@Service
public class TableService {

    @Value("${spring.datasource.db-name}")
    private String dbName;

    /**
     * 获取当前数据库所有的表信息
     */
    public List<Map<String, Object>> getAllTables(String keyword) {
        String sql = "select TABLE_NAME as tableName,TABLE_COMMENT as tableComment from information_schema.`TABLES` where TABLE_SCHEMA = '" + dbName + "'";
        if (StringUtils.hasText(keyword)) {
            sql += " and TABLE_NAME like '%" + keyword + "%'";
        }
        return SqlRunner.db().selectList(sql);
    }
}
