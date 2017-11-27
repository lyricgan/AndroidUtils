package com.lyric.android.app.test.pool;

import java.util.LinkedList;
import java.util.List;

/**
 * 线程池
 * @author lyricgan
 * @time 2016/1/27 17:01
 */
public final class ThreadPool {
    // 线程池中默认线程的个数为5
    private static int sWorkerNum = 5;
    // 工作线程
    private WorkThread[] workThreads;
    // 任务队列，作为一个缓冲，List线程不安全
    private final List<Runnable> taskQueue = new LinkedList<>();

    private static ThreadPool threadPool;

    // 创建具有默认线程个数的线程池
    private ThreadPool() {
        this(5);
    }

    // 创建线程池,workerNum为线程池中工作线程的个数
    private ThreadPool(int workerNum) {
        ThreadPool.sWorkerNum = workerNum;
        workThreads = new WorkThread[workerNum];
        for (int i = 0; i < workerNum; i++) {
            workThreads[i] = new WorkThread();
            workThreads[i].start();// 开启线程池中的线程
        }
    }

    // 单态模式，获得一个默认线程个数的线程池
    public static ThreadPool getThreadPool() {
        return getThreadPool(ThreadPool.sWorkerNum);
    }

    // 单态模式，获得一个指定线程个数的线程池,sWorkerNum(>0)为线程池中工作线程的个数
    // sWorkerNum<=0创建默认的工作线程个数
    public static ThreadPool getThreadPool(int workerNum) {
        if (threadPool == null) {
            threadPool = new ThreadPool(workerNum);
        }
        return threadPool;
    }

    // 执行任务,其实只是把任务加入任务队列，什么时候执行有线程池管理器觉定
    public void addTask(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue. notifyAll();
        }
    }

    // 销毁线程池,该方法保证在所有任务都完成的情况下才销毁所有线程，否则等待任务完成才销毁
    public void destroy() {
        while (!taskQueue.isEmpty()) {// 如果还有任务没执行完成，就先睡会吧
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 工作线程停止工作，且置为null
        for (int i = 0; i < sWorkerNum; i++) {
            workThreads[i].stopWorker();
            workThreads[i] = null;
        }
        threadPool=null;
        taskQueue.clear();// 清空任务队列
    }

    /**
     * 内部类，工作线程
     */
    private class WorkThread extends Thread {
        // 该工作线程是否有效，用于结束该工作线程
        private boolean isRunning = true;

        /*
         * 关键所在，如果任务队列不空，则取出任务执行，若任务队列空，则等待
         */
        @Override
        public void run() {
            Runnable runnable = null;
            while (isRunning) {// 注意，若线程无效则自然结束run方法，该线程就没用了
                synchronized (taskQueue) {
                    while (isRunning && taskQueue.isEmpty()) {// 队列为空
                        try {
                            taskQueue.wait(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!taskQueue.isEmpty())
                        runnable = taskQueue.remove(0);// 取出任务
                }
                if (runnable != null) {
                    runnable.run();// 执行任务
                }
                runnable = null;
            }
        }

        // 停止工作，让该线程自然执行完run方法，自然结束
        public void stopWorker() {
            isRunning = false;
        }
    }
}
