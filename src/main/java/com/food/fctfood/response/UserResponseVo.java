package com.food.fctfood.response;

import lombok.Data;

import java.util.List;

@Data
public class UserResponseVo {
    private String token;
    private List<UserDetailVo> userDetailVos;
}
