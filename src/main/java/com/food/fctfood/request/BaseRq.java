package com.food.fctfood.request;


import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("分页查询")
@Data
public class BaseRq {

    private Integer pageIndex = 0;
    private Integer pageRow = 10;

}
