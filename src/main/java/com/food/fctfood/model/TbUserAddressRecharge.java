package com.food.fctfood.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tb_user_address_recharge")
@Data
public class TbUserAddressRecharge {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "type")
    private int type;

    @Column(name = "address")
    private String address;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "amount")
    private BigDecimal amount;


}
