package com.huafagroup.generator.codegenerator.model;

import java.util.Arrays;
import java.util.List;

public class Table {


    public List<String> getBaseColumns() {
        return baseColumns;
    }

    public void setBaseColumns(List<String> baseColumns) {
        this.baseColumns = baseColumns;
    }

    private List<String> baseColumns = Arrays.asList("id", "createBy", "createTime", "updateBy", "updateTime");


    public String getLowerBeanName() {
        return beanName.toLowerCase();
    }


    public void setLowerBeanName(String lowerBeanName) {
        this.lowerBeanName = lowerBeanName;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }


    public boolean isModulePrefix() {
        return modulePrefix;
    }

    public void setModulePrefix(boolean modulePrefix) {
        this.modulePrefix = modulePrefix;
    }

    private boolean modulePrefix;

    private String module;

    private String lowerBeanName;

    private String tableName;

    private String beanName;

    private String remark;

    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    private List<String> primaryKeys;

    private List<Column> columns;

    private boolean hasDate;

    private boolean hasBigdecimal;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.lowerBeanName = beanName.toLowerCase();
        this.beanName = beanName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public boolean isHasDate() {
        return hasDate;
    }

    public void setHasDate(boolean hasDate) {
        this.hasDate = hasDate;
    }

    public boolean isHasBigdecimal() {
        return hasBigdecimal;
    }

    public void setHasBigdecimal(boolean hasBigdecimal) {
        this.hasBigdecimal = hasBigdecimal;
    }


}
