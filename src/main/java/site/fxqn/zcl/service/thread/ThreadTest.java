package site.fxqn.zcl.service.thread;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class ThreadTest {
    @Resource
    private ThreadPoolTaskExecutor scanThreadPool;

    public static void main(String[] args) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间

        LocalDateTime dt = LocalDateTime.now();
        System.out.println(dt);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm:ss");
        System.out.println(dtf.format(dt));
    };

    public static  void  print(){

        for(int i = 0; i<=50; i++){
            System.out.println(i);
        }

    }
}
