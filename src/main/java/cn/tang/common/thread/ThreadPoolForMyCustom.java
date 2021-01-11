package cn.tang.common.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: tangpd
 * @Date: 2021/1/6 15:46
 * @Description:
 */
public class ThreadPoolForMyCustom {

    /**
     * 创建单例，全局使用一个自定义线程池
     */
    private static volatile ThreadPoolExecutor myPool = null;

    private static final ThreadPoolForMyCustom MY_THREAD_POOL = new ThreadPoolForMyCustom();
    private ThreadPoolForMyCustom() { }
    public static ThreadPoolForMyCustom getThreadMyPool(){return MY_THREAD_POOL;}


    /**
     * 延时创建自定义线程池
     *  50 个核心线程
     *  50 个非核心线程
     *  缓冲队列可容纳 200 个任务
     *  线程存活有效期为1天
     */
    public static ThreadPoolExecutor getMyPool(){
        if (myPool == null){
            synchronized (ThreadPoolForMyCustom.class) {
                if (myPool == null) {
                    myPool = new ThreadPoolExecutor(50, 100, 1, TimeUnit.DAYS, new ArrayBlockingQueue<>(200));
                }
            }
        }
        return myPool;
    }

}
