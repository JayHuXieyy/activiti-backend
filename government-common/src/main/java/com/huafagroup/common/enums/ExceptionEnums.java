package com.huafagroup.common.enums;

public enum ExceptionEnums {

    SUCCESS(0, "成功"),
    ERROR(1, "错误"),

    NOT_FIND_DEVICE(2, "未找到对应的设备");

    private final int code;
    private final String message;

    /**
     * Constructor.
     */
    private ExceptionEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }


    /**
     * Get the value.
     *
     * @return the value
     */
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
