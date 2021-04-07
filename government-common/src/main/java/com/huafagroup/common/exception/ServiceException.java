package com.huafagroup.common.exception;

import com.huafagroup.common.enums.ExceptionEnums;
import com.huafagroup.common.enums.IEnums;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1401593546385403720L;
    private int code = ExceptionEnums.ERROR.getCode();
    private String message = ExceptionEnums.ERROR.getMessage();
    private IEnums enums;

    public ServiceException() {
        super();
        this.setMessage(message);
    }

    public ServiceException(String message) {
        super(message);
        this.setMessage(message);
    }

    public ServiceException(String message, int code) {
        super(message);
        this.setMessage(message);
        this.setCode(code);
    }

    public ServiceException(Throwable cause) {
        super(cause);
        this.setMessage(cause.getMessage());
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
        this.setMessage(message);
    }

    public ServiceException setCode(int code) {
        this.code = code;
        return this;
    }

    public ServiceException setMessage(String message) {
        this.message = message;
        return this;
    }

    public ServiceException setIEnums(IEnums enums) {
        this.enums = enums;
        this.setMessage(enums.getMessage());
        this.setCode(enums.getCode());
        return this;
    }

    public ServiceException(IEnums enums) {
        super(enums.getMessage());
        this.enums = enums;
        this.setMessage(enums.getMessage());
        this.setCode(enums.getCode());
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public IEnums getEnums() {
        return enums;
    }
}
