package com.huafagroup.common.utils;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SignWeiXinUtil {

    /**
     * 获取微信access_token
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public static String getWebAccessToken(String code, String appid, String appSecret) {
        try {
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code&appid=" + appid + "&secret=" + appSecret + "&code="
                    + code;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().clear();
            restTemplate.getMessageConverters().add(new FastJsonHttpMessageConverter());
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * 获取用户个人信息（UnionID机制）
     * 无subscribe
     */
    @SuppressWarnings({"unchecked", "deprecation"})
    public static String getUserInfo(String accessToken, String openId) {
        try {
            String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().clear();
            restTemplate.getMessageConverters().add(new FastJsonHttpMessageConverter());
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
