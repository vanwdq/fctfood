package com.food.fctfood.response;


import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("用户详情")
public class UserDetailVo {

    private int type;
    private String address;
    private BigDecimal amount;
}
