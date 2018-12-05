package com.fct.response;

import com.fct.common.MessageCode;

public class ResponseUtil {


    public static Response ok(String msg, Object date) {
    	Response message = new Response();
        message.setCode(MessageCode.SUCCESS);
        message.setMessage(msg);
        message.setData(date);
        return message;
    }

    public static Response ok(Object date) {
        return ok("操作成功", date);
    }

    public static Response ok() {
        return ok("操作成功", null);
    }

    public static Response error(Integer code, String msg) {
    	Response message = new Response();
        message.setCode(code);
        message.setMessage(msg);
        return message;
    }

    public static Response error(String msg) {
        return error(MessageCode.EXCEPTION_ERROR, msg);
    }

    public static Response error() {
        return error(MessageCode.EXCEPTION_ERROR, "操作失败");
    }

}
