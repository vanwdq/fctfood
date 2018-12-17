package com.food.fctfood.util;

import java.math.BigDecimal;

public class ConfigUtil {


    public static BigDecimal countBd(int type, BigDecimal amount) {
        if (type == 0) {
            return amount.multiply(new BigDecimal("100000"));
        } else {
            return amount.multiply(new BigDecimal("1000"));
        }
    }

}
