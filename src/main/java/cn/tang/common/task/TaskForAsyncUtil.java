package cn.tang.common.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * @Auther: tangpd
 * @Date: 2019/1/17 14:32
 * @Description: 异步任务
 */
@Component
public class TaskForAsyncUtil {

    /**
     * 功能描述: 使用@Async注解，设置任务执行为异步（可能是相当于多线程，先用着）
     * @param: []
     * @return: java.util.concurrent.Future<java.lang.Boolean>
     *          方法执行成功之后，返回 Future<V> 类型，用于判断是否任务执行完毕，里面可以是Object类型，传Boolean方便判断；
     * @auther: tangpd
     * @date: 2019/1/17 14:41
     */
    @Async
    public Future<Boolean> doTask1() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        System.out.println("任务1执行耗时：" + ( end - start ) + "毫秒");
        return new AsyncResult<>(true);
    }
    @Async
    public Future<Boolean> doTask2() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(2000);
        long end = System.currentTimeMillis();
        System.out.println("任务2执行耗时：" + ( end - start ) + "毫秒");
        return new AsyncResult<>(true);
    }
    @Async
    public Future<Boolean> doTask3() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread.sleep(3000);
        long end = System.currentTimeMillis();
        System.out.println("任务3执行耗时：" + ( end - start ) + "毫秒");
        return new AsyncResult<>(true);
    }
}
