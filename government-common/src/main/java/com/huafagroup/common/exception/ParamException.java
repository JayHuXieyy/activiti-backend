package com.huafagroup.common.exception;

public class ParamException extends RuntimeException {
    /*无参构造函数*/
    public ParamException() {
        super();
    }

    //用详细信息指定一个异常
    public ParamException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public ParamException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public ParamException(Throwable cause) {
        super(cause);
    }
}
