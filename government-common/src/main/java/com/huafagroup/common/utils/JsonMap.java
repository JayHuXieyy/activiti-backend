package com.huafagroup.common.utils;

public class JsonMap {
    public JsonMap(int state, String message) {
        this.state = state;
        this.message = message;
    }

    public JsonMap(int state, String message, Object result) {
        this.state = state;
        this.message = message;
        this.result = result;
    }

    private int state;            //状态码，默认0为成功
    private String message;        //信息
    private Object result;        //结果集

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

}
