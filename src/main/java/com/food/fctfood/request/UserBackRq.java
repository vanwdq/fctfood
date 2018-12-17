package com.food.fctfood.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@ApiModel("用户信息")
@Data
public class UserBackRq {
    @ApiModelProperty("用户名")
    private String account;
    @ApiModelProperty("密码")
    private String password;


}
