package com.food.fctfood.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_address")
@Data
public class TbAddress {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "type")
    private int type;

    @Column(name = "address")
    private String address;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "is_allot")
    private int isAllot;

}
