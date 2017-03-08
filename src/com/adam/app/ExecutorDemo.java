package com.adam.app;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ExecutorDemo
{
    public static void main(String[] args) {
        int N = Runtime.getRuntime().availableProcessors();
        LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<Runnable>();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(N, 
                2*N, 
                60L, 
                TimeUnit.SECONDS,
                linkedBlockingQueue);
        
        CyclicBarrier barrier = new CyclicBarrier(2, new FinalTask());
        for (int i = 0; i < 10; ++i) {
            MyTask myTask = new MyTask(i, barrier);
            executor.execute(myTask);
        }
        
        executor.shutdown();
        
    }
    
    private static class MyTask implements Runnable {

        private int mId;
        private CyclicBarrier mBarrier;
        
        public MyTask(int id, CyclicBarrier barrier) {
            this.mId = id;
            this.mBarrier = barrier;
        }
        
        @Override
        public void run()
        {
            System.out.println("Task: " + this.mId);
            try
            {
                this.mBarrier.await();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (BrokenBarrierException e)
            {
                e.printStackTrace();
            }
        }
        
    }
    
    private static class FinalTask implements Runnable {

        @Override
        public void run()
        {
            System.out.println("Task completely");
        }
        
    }
}

/*
 * ===========================================================================
 * 
 * Revision history
 * 
 * ===========================================================================
 */
