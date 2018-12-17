package com.food.fctfood.common;

/**
 * Created by thinkpad on 2018/4/26.
 */
public class CacheKey {

    /** 前台类目下商品id集合 **/
    public static final String MENU_ITEMS = "menuItemIds:{menuId}";
    
    /** 短信缓存 */
    public static final String SMS_CODE_REG = "sms_code:reg:";
    public static final String SMS_CODE_VALID = "sms_code:valid:";
    public static final String SMS_CODE_COUNT = "sms_code:count:";
    
    public static final String USER_SESSION_KEY = "session_mt_user::";

    public static final String USER_EMAIL = "email_user::";
    
    /**用户过期时间，30天**/
    public static final int USER_LOGIN_EXPIRE = 2592000;
}
