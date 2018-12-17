package com.food.fctfood.request;


import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("用户信息")
@Data
public class UserRq {
    @ApiModelProperty("邮箱地址")
    private String email;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("验证码")
    private String code;

    public static void main(String[] args) {
        UserRq userRq = new UserRq();
        userRq.setEmail("2191572579@qq.com");
        userRq.setCode("1234");
        userRq.setPassword("123456");
        System.out.println(JSON.toJSONString(userRq));
    }

}
