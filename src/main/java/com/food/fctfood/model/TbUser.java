package com.food.fctfood.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "tb_user")
@Data
public class TbUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "email")
    @NotNull(message = "邮件地址不能为空")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "create_time")
    private Date createTime;


}
