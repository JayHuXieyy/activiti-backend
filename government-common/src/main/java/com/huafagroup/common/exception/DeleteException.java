package com.huafagroup.common.exception;

public class DeleteException extends RuntimeException {
    /*无参构造函数*/
    public DeleteException() {
        super();
    }

    //用详细信息指定一个异常
    public DeleteException(String message) {
        super(message);
    }

    //用指定的详细信息和原因构造一个新的异常
    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    //用指定原因构造一个新的异常
    public DeleteException(Throwable cause) {
        super(cause);
    }
}
