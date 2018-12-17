package com.food.fctfood.common;


public interface MessageCode extends BaseMessageCode {

    int INVALID_CODE = 20000;//Verification code expiration
    int MORE_SEND = 20006;//Send too fast, please send 60 seconds later
    int WRONG_CODE = 20001;//Error in Verification Code Input
    int NO_ADDRESS = 20002; //The btc address has reached the upper limit"
    int WRONG_USER = 20003; //ERROR Incorrect username or password
    int NO_VOTE = 20004; //With no investment quota
    int USER_REGISTER = 20005; //User Registered
    int NO_TOKEN = 20007; //no token
    int TOKEN_EXPIRE=20008;//token exprie
    int NO_MONEY=20009;//Not enough amount
    int WAONR_CODE=20010;//NOT right code

}
