package com.food.fctfood.response;

import com.food.fctfood.model.TbUserInvication;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserInvication {
    private Integer totalPage;
    private List<TbUserInvication> tbUserInvications = new ArrayList<>();

}
