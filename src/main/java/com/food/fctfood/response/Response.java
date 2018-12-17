package com.food.fctfood.response;

import java.io.Serializable;

public class Response<T> implements Serializable {


    private static final long serialVersionUID = 1L;
    private Integer code;//1成功 0异常
    private String message;
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
