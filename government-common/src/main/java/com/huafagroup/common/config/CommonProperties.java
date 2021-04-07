package com.huafagroup.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonProperties {
    @Value("${h5WxAppId:}")
    private String h5WxAppId;          // 微信H5授权ID
    @Value("${h5WxAppSecret:}")
    private String h5WxAppSecret;      // 微信H5授权密匙
    @Value("${redirectDomain:}")         // 公众号重定向域名
    private String redirectDomain;

    public String getRedirectDomain() {
        return redirectDomain;
    }

    public void setRedirectDomain(String redirectDomain) {
        this.redirectDomain = redirectDomain;
    }

    public String getH5WxAppId() {
        return h5WxAppId;
    }

    public void setH5WxAppId(String h5WxAppId) {
        this.h5WxAppId = h5WxAppId;
    }

    public String getH5WxAppSecret() {
        return h5WxAppSecret;
    }

    public void setH5WxAppSecret(String h5WxAppSecret) {
        this.h5WxAppSecret = h5WxAppSecret;
    }
}