package com.huafagroup.common.utils;

import java.util.LinkedHashMap;

/**
 * 查询参数枚举
 *
 * @author dong
 */
public enum SearchQueryEnum {
    NAME("name", "名称"),
    CODE("code", "编码"),
    CODE2("code2", "编码"),
    MODE("mode", "模式"),
    STATUS("status", "状态"),
    GET_NUM_STATUS("getNumStatus", "预约状态"),
    SEX("sex", "性别"),
    PHONE("phone", "手机号"),
    TITLE("title", "标题"),
    DEPT("dept", "部门"),
    TYPE("type", "类型"),
    ID_CARD("idCard", "身份证"),
    EVENT_NAME("eventName", "事项名称"),
    GET_NUM_DATE("getNumDate", "叫号时间"),
    CREATE_TIME("creatTime", "创建时间"),
    WINDOW("window", "窗口号"),
    STAFF("staff", "员工"),
    RESULT("result", "结果"),
    HANDLE_START_TIME("handleStartTime", "办理开始时间"),
    HANDLE_END_TIME("handleEndTime", "办理结束时间"),
    APPOINTMENT_DATE("appointmentDate", "预约日期"),
    FLOOR("floor", "楼层"),
    AREA("area", "区域"),
    TITLE_NAME("titleName", "新闻动态标题名称"),
    USER_NAME("userName", "用户名称"),
    ROLE_ID("roleId", "角色id");
    ;
    private final String value;
    private final String desc;
    private static final LinkedHashMap<String, String> map;

    static {
        map = new LinkedHashMap<>();
        for (SearchQueryEnum searchQueryEnum : SearchQueryEnum.values()) {
            map.put(searchQueryEnum.getValue(), searchQueryEnum.getDesc());
        }
    }

    SearchQueryEnum(String value, String desc) {
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
