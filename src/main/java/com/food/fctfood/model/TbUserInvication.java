package com.food.fctfood.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tb_user_invication")
@Data
public class TbUserInvication {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int type;
    private String address;
    private Date createTime;
    private BigDecimal amount;
    private int userId;
    private BigDecimal fctBd;

}
