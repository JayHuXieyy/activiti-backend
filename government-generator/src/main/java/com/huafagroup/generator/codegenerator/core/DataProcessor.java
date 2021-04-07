package com.huafagroup.generator.codegenerator.core;

import com.huafagroup.generator.codegenerator.model.Column;
import com.huafagroup.generator.codegenerator.model.DictItem;
import com.huafagroup.generator.codegenerator.model.Table;

import java.sql.*;
import java.util.*;

public class DataProcessor {

    private Connection connection;

    public DataProcessor(Connection con) {
        connection = con;
    }

    //主键、创建人、创建时间、修改人、修改时间，在处理request参数校验时允许为空
    private List<String> allowNullColumns = Arrays.asList("id", "createBy", "createTime", "updateBy", "updateDate", "remark");


    public List<DictItem> getDictItem(String code) {

        List<DictItem> dictitemList;
        String sql = "SELECT dt.id as dictId,dt.code as dictCode,dt.text as dictText," +
                "di.sort as itemSort," +
                "di.text as itemText,di.value as itemValue " +
                "FROM dictionary_item di  " +
                "join dictionary_type dt " +
                "on dt.id=di.type_id " +
                "and trim(dt.code)=trim(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, code);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                dictitemList = new ArrayList<DictItem>();
                while (resultSet.next()) {
                    DictItem dictItem = new DictItem();
                    dictItem.setDictCode(resultSet.getString("dictCode"));
                    dictItem.setDictId(resultSet.getString("dictId"));
                    dictItem.setDictText(resultSet.getString("dictText"));
                    dictItem.setItemSort(resultSet.getInt("itemSort"));
                    dictItem.setItemText(resultSet.getString("itemText"));
                    dictItem.setItemValue(resultSet.getString("itemValue"));
                    dictitemList.add(dictItem);
                }
                return dictitemList;
            }


        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 表预处理
     *
     * @param tableInfos
     */
    public void prepareProcessTableInfos(List<Table> tableInfos) {
        try {
            for (Table table : tableInfos) {
                //设置主键列表
                List<String> primaryKeys = new ArrayList<>();
                ResultSet keysSet = connection.getMetaData().getPrimaryKeys(null, null, table.getTableName());
                while (keysSet.next()) {
                    String primaryKey = keysSet.getString("COLUMN_NAME");
                    primaryKeys.add(primaryKey);
                }
                table.setPrimaryKeys(primaryKeys);
                ResultSet tableSet = connection.getMetaData().getTables(connection.getCatalog(), null, table.getTableName(), null);
                while (tableSet.next()) {
                    table.setRemark(tableSet.getString("REMARKS"));
                }
                //设置模块名
                int index = 0;
//                int index = table.getTableName().indexOf('_');
//                if (index > 0) {
//                    String module = table.getTableName().substring(0, index);
//                    table.setModule(module.toLowerCase());
//                    table.setModulePrefix(true);
//                }

                //优化模块化的类命名规则

                table.setBeanName(StringUtils.underLineToCamel(StringUtils.toUpperCaseFirst(
                        table.getTableName().substring(index, table.getTableName().length()).toLowerCase())));
                table.setLowerBeanName(table.getBeanName().toLowerCase());


                prepareProcessColumns(table);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {


        }

    }

    /**
     * 列预处理
     * id
     *
     * @param table
     */
    public void prepareProcessColumns(Table table) {
        List<Column> columns = table.getColumns();

        for (Column column : columns) {
//            List<DictItem> itemlist = getDictItem(column.getColumn().toLowerCase());
//            column.setDictitemList(itemlist);
//            if (itemlist.size() != 0) {
//                column.setHasDictItem(true);
//            } else {
//                column.setHasDictItem(false);
//            }
            String lowerProperty = StringUtils.underLineToCamel(column.getColumn().toLowerCase());

            column.setLowerProperty(lowerProperty);
            column.setProperty(StringUtils.toUpperCaseFirst(StringUtils.underLineToCamel(lowerProperty)));
            if (allowNullColumns.contains(column.getLowerProperty())) {
                column.setAllowNull(true);
            }
            if (table.getPrimaryKeys().contains(column.getLowerProperty())) {
                column.setAllowNull(true);
            }

        }

    }

    public List<Table> convertToTableInfos(Map<String, Map<String, Map<String, Object>>> tables) throws Exception {
        List<Table> tableInfos = new ArrayList<>();
        Table table = null;
        Column column = null;
        List<Column> columns = null;
        for (Map.Entry<String, Map<String, Map<String, Object>>> e : tables.entrySet()) {
            table = new Table();
            columns = new ArrayList<>();
            table.setColumns(columns);
            tableInfos.add(table);
            String tableName = e.getKey();
            table.setTableName(tableName);

            Map<String, Map<String, Object>> rows = e.getValue();
            for (Map.Entry<String, Map<String, Object>> row : rows.entrySet()) {
                column = new Column();
                column.setColumn(row.getKey());
                Map<String, Object> rowInfo = row.getValue();
                String jdbcType = (String) rowInfo.get("jdbcType");
                column.setJdbcType(jdbcType);
                column.setRemark((String) rowInfo.get("remark"));
                column.setDataType((int) rowInfo.get("dataType"));
                column.setMaxLength((int) rowInfo.get("length"));
                int isnull = (int) rowInfo.get("isnull");
                if (isnull == 0) {
                    column.setAllowNull(false);
                } else {
                    column.setAllowNull(true);
                }

                String javaType = TypeUtils.getJavaType(column);
                column.setType(javaType);
                if ("Date".equals(javaType)) {
                    table.setHasDate(true);
                } else if ("BigDecimal".equals(javaType)) {
                    table.setHasBigdecimal(true);
                }
                columns.add(column);
            }
        }
        return tableInfos;
    }


    public Map<String, Map<String, Map<String, Object>>> getTableInfo(String tableNamePattern) {
        Map<String, Map<String, Map<String, Object>>> tables = new HashMap<>();
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getColumns(connection.getCatalog(), null, tableNamePattern, null);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");

                String colName = rs.getString("COLUMN_NAME");
                String jdbcType = rs.getString("TYPE_NAME");
                if (jdbcType.equals("DATETIME")) {
                    jdbcType = "DATE";
                }
                if (jdbcType.equals("INT")) {
                    jdbcType = "INTEGER";
                }
                if (jdbcType.equals("VARCHAR2")) {
                    jdbcType = "VARCHAR";
                }
                if (jdbcType.equals("NUMBER")) {
                    jdbcType = "DECIMAL";
                }

                Integer dataType = rs.getInt("DATA_TYPE");
                String remarks = rs.getString("REMARKS");
                Map<String, Map<String, Object>> table = tables.get(tableName);
                if (table == null) {
                    table = new HashMap<>();
                    tables.put(tableName, table);
                }
                Map<String, Object> row = new HashMap<>();
                row.put("jdbcType", jdbcType);
                row.put("remark", remarks);
                row.put("dataType", dataType);
                row.put("length", rs.getInt("COLUMN_SIZE"));
                row.put("isnull", rs.getInt("NULLABLE"));
                table.put(colName, row);
            }
            for (Map.Entry<String, Map<String, Map<String, Object>>> e : tables.entrySet()) {
                System.out.println(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {

        }
        return tables;
    }

    public List<Table> getTableInfos(String tableNamePattern) {
        Map<String, Map<String, Map<String, Object>>> tables = getTableInfo(tableNamePattern);

        try {
            List<Table> tableInfos = convertToTableInfos(tables);

            prepareProcessTableInfos(tableInfos);
            return tableInfos;
        } catch (Exception e) {
            return null;
        }

    }

    public static void main(String[] args) {

    }
}
