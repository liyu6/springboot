package site.fxqn.zcl;
/*
    作者: kat-qian
    日期: 2018/5/30 17:11
*/

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import site.fxqn.zcl.utils.SpringUtils;

@SpringBootApplication
@Slf4j
@PropertySource({
        "classpath:config/application.properties",
        "classpath:config/db.properties",
        "classpath:config/shiro.properties",
//        "classpath:config/activemq.properties",
        "classpath:config/sms.properties",
        "classpath:config/biz.properties"
})
public class ZCLApplication {

    public static void main(String[] args) {
        Logger logger= LoggerFactory.getLogger(ZCLApplication.class);
        ApplicationContext applicationContext = SpringApplication.run(ZCLApplication.class);
        SpringUtils.setApplicationContext(applicationContext);
        logger.info("程序启动");
    }


}
