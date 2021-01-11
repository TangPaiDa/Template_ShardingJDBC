package cn.tang.common.thread;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther: tangpd
 * @Date: 2021/1/7 15:36
 * @Description:
 */
public class TestMyCustomPool {

    public static void main(String[] args) throws InterruptedException {
        //获取自定义线程池
        ThreadPoolExecutor mypool = ThreadPoolForMyCustom.getMyPool();

        //模拟任务，放入到线程池中执行
        Long startTime = System.currentTimeMillis();
        for (int i = 0; i < 15; i++) {
            try {
                mypool.execute(new ThreadTaskForTemplate(i));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("已经超出承受范围，稍后重试*********************************(i)" + i);
            }
        }
        System.out.println("用时**************************（）" + (System.currentTimeMillis() - startTime));
    }

}
