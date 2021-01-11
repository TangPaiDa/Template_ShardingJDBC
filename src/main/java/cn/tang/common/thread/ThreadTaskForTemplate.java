package cn.tang.common.thread;

/**
 * @Auther: tangpd
 * @Date: 2021/1/6 15:46
 * @Description:
 */
public class ThreadTaskForTemplate implements Runnable{

    private int num;

    public ThreadTaskForTemplate(int num){
        this.num = num;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("东方不败");
        System.out.println(Thread.currentThread().getName() + "--" + num);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
