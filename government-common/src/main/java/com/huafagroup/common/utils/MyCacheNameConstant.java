package com.huafagroup.common.utils;

import org.springframework.cache.annotation.Cacheable;

/**
 * @author jay
 */
public class MyCacheNameConstant {

    /**
     * 获取第三方办事指南详情缓存名称常量
     */
    public static final String THIRD_PARTY_GUIDE_DETAILS = "getThirdPartyGuideDetails";

    /**
     * 获取第三方办事指南列表详情常量
     */
    public static final String THIRD_PARTY_GUIDE_LIST = "getBusinessList";
}
