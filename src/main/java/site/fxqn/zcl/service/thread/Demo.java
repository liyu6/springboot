package site.fxqn.zcl.service.thread;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;

public class Demo {
    @Resource
    private ThreadPoolTaskExecutor scanThreadPool;

    public   void  print(){
            scanThreadPool.execute(()->{
                for(int i = 0; i<=50; i++){
                    System.out.println(i);
                }
            }
        );
    }
}
