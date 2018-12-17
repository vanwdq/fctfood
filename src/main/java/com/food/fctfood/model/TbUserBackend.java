package com.food.fctfood.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "tb_user_backend")
@Data
public class TbUserBackend {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "account")
    @NotNull(message = "用户名不能为空")
    private String account;

    @Column(name = "password")
    @NotNull(message = "密码不能为空")
    private String password;

    @Column(name = "create_time")
    private Date createTime;


}
