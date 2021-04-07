package com.huafagroup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动程序
 *
 * @author huafagroup
 */
@EnableCaching
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableRetry
@EnableAsync
public class GovernmentApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(GovernmentApplication.class, args);
        System.out.println("------------------------------------  (♥◠‿◠)ﾉﾞ  系统启动成功   ლ(´ڡ`ლ)ﾞ  ------------------------------------");
    }
}
