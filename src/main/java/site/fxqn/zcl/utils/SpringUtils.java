package site.fxqn.zcl.utils;
/*
    作者: kat-qian
    日期: 2018/6/1 18:21
*/

import lombok.Getter;
import org.springframework.context.ApplicationContext;

public class SpringUtils {

    @Getter
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 设置applicationContext
     * @param applicationContext
     */
    public static synchronized void setApplicationContext(ApplicationContext applicationContext) {
        if (APPLICATION_CONTEXT == null) {
            APPLICATION_CONTEXT = applicationContext;
        }
    }

    //通过name获取 Bean.
    public static Object getBean(String name) {
        return APPLICATION_CONTEXT.getBean(name);
    }


    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz) {
        return APPLICATION_CONTEXT.getBean(clazz);
    }


    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name, Class<T> clazz) {
        return APPLICATION_CONTEXT.getBean(name, clazz);
    }

}
