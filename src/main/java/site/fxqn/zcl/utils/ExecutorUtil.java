package site.fxqn.zcl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by baowp on 2018/8/16.
 */
public class ExecutorUtil {

    private static final Logger logger= LoggerFactory.getLogger(ExecutorUtil.class);

    public static final ThreadPoolExecutor ScanDataExecutor = ExecutorGenerator.newThreadPoolExecutor(10, 15,
            600, 500, "scan-data-", new RejectedExecutionHandler(){

                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                    logger.info("ScanDataExecutor executor rejected Exception {}",r);
                    if (!e.isShutdown()) {
                        r.run();
                    }
                }
            });
    public static final ThreadPoolExecutor MQExecutor = ExecutorGenerator.newThreadPoolExecutor(6, 10,
            600, 500, "user-defined-", new RejectedExecutionHandler(){

                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                    logger.info("MQExecutor executor rejected Exception {}",r);
                    if (!e.isShutdown()) {
                        r.run();
                    }
                }
            });
}
