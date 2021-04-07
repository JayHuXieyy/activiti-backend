package com.huafagroup.framework.aspectj;

import com.alibaba.fastjson.JSON;
import com.huafagroup.common.annotation.LocalLog;
import com.huafagroup.common.enums.HttpMethod;
import com.huafagroup.common.utils.ServletUtils;
import com.huafagroup.common.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * 操作日志记录处理
 *
 * @author huafagroup
 */
@Aspect
@Component
public class LocalLogAspect {
    private static final Logger log = LoggerFactory.getLogger(LocalLogAspect.class);

    // 配置织入点
    @Pointcut("@annotation(com.huafagroup.common.annotation.LocalLog)")
    public void logPointCut() {
    }

    /**
     * 处理前执行
     */
    @Before(value = "com.huafagroup.framework.aspectj.LocalLogAspect.logPointCut()")
    public void before(JoinPoint joinPoint) throws Exception {
        // 获得注解
        LocalLog controllerLog = getAnnotationLog(joinPoint);
        if (controllerLog == null) {
            return;
        }
        log.info(controllerLog.title() + "的方法开始时间--------------------->" + new Date());
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        String title = "";
        try {
            // 获得注解
            LocalLog controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            title = controllerLog.title();
            // *========本地日志=========*//
            // 请求参数
            String requestMethod = ServletUtils.getRequest().getMethod();
            if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
                String params = argsArrayToString(joinPoint.getArgs());
                log.info(title + "的请求参数为-------------->" + StringUtils.substring(params, 0, 2000));
            } else {
                Map<?, ?> paramsMap = (Map<?, ?>) ServletUtils.getRequest().getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                log.info(title + "的请求参数为-------------->" + StringUtils.substring(paramsMap.toString(), 0, 2000));
            }
            // 返回参数
            if (jsonResult != null) {
                log.debug(title + "的返回参数为-------------->" + JSON.toJSONString(jsonResult));
            }
            // 处理异常
            if (e != null) {
                log.info(title + "的异常信息为-------------->" + StringUtils.substring(e.getMessage(), 0, 2000));
            }
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        } finally {
            log.info(title + "的方法结束时间--------------------->" + new Date());
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private LocalLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(LocalLog.class);
        }
        return null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (!isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSON.toJSON(paramsArray[i]);
                    params.append(jsonObj.toString()).append(" ");
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse;
    }
}
