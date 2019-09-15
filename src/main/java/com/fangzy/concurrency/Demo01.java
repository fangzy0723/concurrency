package com.fangzy.concurrency;

import java.util.concurrent.*;

/**
 * 使用CountDownLatch、Semaphore进行代码控制并发
 * 这个demo不是线程安全的
 */
public class Demo01 {

    private static  int threadTotal = 200;
    private static  int clientTotal = 5000;
    private static  int count = 0;

    public static void main(String[] args) throws Exception {

        ExecutorService executorService = Executors.newCachedThreadPool();
        //总用户请求
        CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        //同时并发的用户请求
        Semaphore semaphore = new Semaphore(threadTotal);

        for (int i = 0; i < clientTotal; i++) {
            Thread.sleep(2000);
            executorService.execute(()->{
                try{
                    //获取许可，只有获取到许可才可以往下执行
                    semaphore.acquire();
                    add();
                    //执行完释放许可
                    semaphore.release();
                    Thread.sleep(2000);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                //线程数减1，减到0，主线程才会往下执行

                countDownLatch.countDown();
            });
        }
        //等待，当计数减到0时主线程才继续执行
        countDownLatch.await();
        System.out.println(count);
        executorService.shutdown();
    }

    /**
     * 这个方法不是线程安全的
     */
    private static void add(){
        System.out.println("--");
        count++;
    }
}
