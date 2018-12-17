package com.food.fctfood.response;

import com.food.fctfood.model.TbUser;
import com.food.fctfood.model.TbUserAddress;
import lombok.Data;

import java.io.Serializable;


@Data
public class UserInfoVo implements Serializable {
  private TbUser tbUser;
  private TbUserAddress tbUserAddress;

   public UserInfoVo() {
   }

   public UserInfoVo(TbUser tbUser) {
      this.tbUser = tbUser;
   }

   public UserInfoVo(TbUserAddress tbUserAddress) {
      this.tbUserAddress = tbUserAddress;
   }

   public UserInfoVo(TbUser tbUser, TbUserAddress tbUserAddress) {
      this.tbUser = tbUser;
      this.tbUserAddress = tbUserAddress;
   }
}
