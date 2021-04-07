package com.huafagroup.generator.codegenerator.core;

import com.huafagroup.generator.codegenerator.model.Column;
import org.springframework.util.StringUtils;

public class TypeUtils {

    public static String getJavaType(Column dataType) {
        if (dataType.getDataType() == CommonDataType.REAL) {
            return getJavaType(dataType.getJdbcType());
        }
        return getJavaType(CommonDataType.getJdbcType(dataType.getDataType()));
    }

    public static String getJavaType(String jdbcType) {
        String lowerJdbcType = jdbcType.toLowerCase();
        String type = null;
        if (lowerJdbcType.equals("char") || lowerJdbcType.equals("varchar") || lowerJdbcType.equals("longvarchar")) {
            type = "String";
        }
        if (lowerJdbcType.equals("bit")) {
            type = "Boolean";
        }

        if (lowerJdbcType.equals("tinyint") || lowerJdbcType.equals("smallint")
                || lowerJdbcType.equals("integer")) {
            type = "Integer";
        }

        if (lowerJdbcType.equals("bigint")) {
            type = "Long";
        }

        if (lowerJdbcType.equals("float")) {
            type = "Float";
        }
        if (lowerJdbcType.equals("double")) {
            type = "Double";
        }
        if (lowerJdbcType.equals("numeric") || lowerJdbcType.equals("decimal")) {
            type = "BigDecimal";
        }

        if (lowerJdbcType.equals("date") || lowerJdbcType.equals("time") || lowerJdbcType.equals("timestamp")) {
            type = "Date";
        }
        if (lowerJdbcType.equals("blob") || lowerJdbcType.equals("binary") || lowerJdbcType.equals("varbinary") || lowerJdbcType.equals("longvarbinary")) {
            type = "byte[]";
        }
        if (StringUtils.isEmpty(type)) {
            throw new RuntimeException(lowerJdbcType);
        }
        return type;
    }
}
