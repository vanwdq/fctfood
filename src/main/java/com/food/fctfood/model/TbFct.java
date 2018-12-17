package com.food.fctfood.model;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tb_fct")
@Data
public class TbFct {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //总额度
    private BigDecimal fctTotal;
    //已经投资的额度
    private BigDecimal voteTotal;
    //剩余额投资额度
    private BigDecimal remainTotal;
    private BigDecimal rate;
    private Date createTime;
    private Date updateTime;


}
