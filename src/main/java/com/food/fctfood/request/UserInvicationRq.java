package com.food.fctfood.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@ApiModel("用户投资请求")
@Data
public class UserInvicationRq {
    @ApiModelProperty("币种类型 0 btc 1 eth")
    private int type;
    @ApiModelProperty("币种数额")
    private BigDecimal amount;
}
