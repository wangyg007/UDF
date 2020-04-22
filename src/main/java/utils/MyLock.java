package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author wangyg
 * @time 2020/4/9 9:13
 * @note
 **/
public class MyLock {

    private volatile boolean isLocked=false;
    private volatile int lockCnt=0;
    private Thread ownerThread=null;

    public synchronized boolean tryLock() throws InterruptedException {

        Thread thread = Thread.currentThread();
        if (isLocked && ownerThread != thread){
            wait();
        }
        isLocked=true;
        lockCnt++;
        ownerThread=thread;
        return true;
    }

    public synchronized void release(){
        Thread thread = Thread.currentThread();
        if (ownerThread==thread){
            lockCnt--;
            if (lockCnt==0){
                isLocked=false;
                notify();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        final MyLock lock = new MyLock();
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        executorService.submit(new Runnable() {

            public void run() {
                try {
                    boolean res = lock.tryLock();
                    System.out.println(Thread.currentThread().getName()+" res:"+res);
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
//                    lock.release();
                }
            }
        });

        executorService.submit(new Runnable() {

            public void run() {
                try {
                    boolean res = lock.tryLock();
                    System.out.println(Thread.currentThread().getName()+" res:"+res);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        System.out.println(lock.tryLock());
        Thread.sleep(5000);
        lock.release();

    }

}
