package cn.tang.common.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @Auther: tangpd
 * @Date: 2019/1/17 13:33
 * @Description: 定时任务
 */
@Component
@Configuration
public class TaskForTimingUtil implements SchedulingConfigurer {

    private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("HH:mm:ss");

    //使用crom表达式做定时
    @Scheduled(cron = "5 57 13 * * 4")
    public void testScheduled() {
        System.out.println("//////////////现在时间///////////////" + DATEFORMAT.format(new Date()));
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "my-schedule");
                }
            }));
    }

}
