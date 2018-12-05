package com.fct.controller;


import com.alibaba.fastjson.JSON;
import com.fct.common.MessageCode;
import com.fct.response.Response;
import com.fct.response.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.UUID;
import java.util.concurrent.Callable;

@Controller
@RequestMapping(value = "/test", produces = "text/html;charset=UTF-8")
@Api(value = "/test", description = "测试")
public class TestController {


    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @RequestMapping(value = "getToken", method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("获得token")
    public Callable<String> addBankCard() {
        return () -> {
            Response<String> message = ResponseUtil.ok();
            try {
                message.setData(UUID.randomUUID().toString().replace("-", ""));
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                message.setCode(MessageCode.EXCEPTION_ERROR);
            }
            return JSON.toJSONString(message);
        };
    }

}
