package com.food.fctfood.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

@Component
@Data
public class EmailConfig {
    @Value("${spring.mail.account}")
    private String account;//登录用户名
    @Value("${spring.mail.pass}")
    private String pass;        //登录密码
    @Value("${spring.mail.from}")
    private String from;        //发件地址
    @Value("${spring.mail.host}")
    private String host;        //服务器地址
    @Value("${spring.mail.port}")
    private String port;        //端口
    @Value("${spring.mail.protocol}")
    private String protocol; //协议

}
