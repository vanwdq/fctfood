package com.fct.common;


public interface MessageCode extends BaseMessageCode {

    int USER_NO_LOGIN = 10000;        //用户未登录
    int SKU_UNENOUGH = 10001;        //库存不足
    int USER_TAKE_LIMIT = 10002;    //领取达上限
    int USER_AUTH = 20000;           //用户未认证
    int USER_NAME_NOT = 20001;        //持卡人姓名必须和实名认证的姓名保持一致
    int USER_BANK_REPEAT = 20002;      //您已添加过该银行卡
    int NOT_ENOUGH_MONEY = 20003;       //提现金额不足
    int MORE_DRAW_MONEY = 20004;        //超过提现金额
    int DRAW_FAIL = 20005;              //提现失败
    int NO_USER = 200006;               //无此用户
    int USER_VERIFY=200007;             //已实名认证过


}
