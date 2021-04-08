package com.huafagroup.common.utils;

import java.util.LinkedHashMap;

/**
 * 查询参数枚举
 *
 * @author dong
 */
public enum ModulePageTypeEnum {
    PARTY("0", "党组织"),
    PRODUCTION("1", "生产服务"),
    LIFE("2", "生活服务"),
    ;
    private final String value;
    private final String desc;
    private static final LinkedHashMap<String, String> map;

    static {
        map = new LinkedHashMap<>();
        for (ModulePageTypeEnum searchQueryEnum : ModulePageTypeEnum.values()) {
            map.put(searchQueryEnum.getValue(), searchQueryEnum.getDesc());
        }
    }

    ModulePageTypeEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    public static LinkedHashMap<String, String> getMap() {
        return map;
    }
}
