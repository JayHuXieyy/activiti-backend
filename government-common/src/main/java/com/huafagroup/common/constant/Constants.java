package com.huafagroup.common.constant;

/**
 * 通用常量信息
 *
 * @author huafagroup
 */
public class Constants {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 刷新令牌
     */
    public static final String REFRESHTOKEN = "RefreshToken";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = "sub";

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";


    /**
     * 数字常量
     */
    public static final Integer status_0 = 0;


    /**
     * 数字常量
     */
    public static final Integer status_1 = 1;


    /**
     * 数字常量
     */
    public static final Integer status_2 = 2;

    /**
     * 队列 redis key
     */
    public static final String CALL_QUEUE = "call_queue:";

    /**
     * 登陆用户手机号redis key
     */
    public static final String LOGIN_KEY = "login_phone:";
    /**
     * 忘记密码手机号redis key
     */
    public static final String FORGET_KEY = "forget_phone:";
    /**
     * 华发短信平台accessToken
     */
    public static final String SMS_ACCESS_TOKEN = "sms_access_token:";

    /**
     * 华发短信平台accessToken
     */
    public static final String SMS_REFRESH_TOKEN = "sms_refresh_token:";

}
