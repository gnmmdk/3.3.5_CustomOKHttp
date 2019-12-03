package com.kangjj.okhttp.library;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library
 * @CreateDate: 2019/12/3 15:29
 */
public class Dispatcher2 {
    private int maxRequests = 64;//同时访问任务，最大限制64个
    private int maxRequestsPerHost = 5;     //同时访问同一个服务器域名，最大限制5个

    private Deque<RealCall2.AsyncCall2> runningAsyncCalls = new ArrayDeque<>();
    private Deque<RealCall2.AsyncCall2> readyAsyncCalls = new ArrayDeque<>();

    public void enqueue(RealCall2.AsyncCall2 call) {
        //同时运行的列队数必须小于配置的64 && 同时访问同一个服务器域名不能超过5个
        if(runningAsyncCalls.size() < maxRequests && runningCallsForHost(call)<maxRequestsPerHost){
            runningAsyncCalls.add(call);//先把任务加到队列中
            executorService().execute(call);
        }else{
            readyAsyncCalls.add(call);      //加入等待队列中
        }
    }


    private ExecutorService executorService() {
//        Executors.newSingleThreadExecutor()
        ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("自定义线程...");
                thread.setDaemon(false);//不是守护线程
                return thread;
            }
        });
        return executorService;
    }

    /**
     *  判断AsyncCall2中的Host，在运行的队列中，计数，然后放回
     * @param call
     * @return
     */
    private int runningCallsForHost(RealCall2.AsyncCall2 call) {
        int count = 0 ;
        if(runningAsyncCalls.isEmpty()){
            return 0;
        }
        SocketRequestServer srs = new SocketRequestServer();
        for (RealCall2.AsyncCall2 runningAsyncCall : runningAsyncCalls) {
            if(srs.getHost(runningAsyncCall.getRequest()).equals(call.getRequest())){//TODO String .equals(Request) ?
                count ++;
            }
        }
        return count;
    }

    /**
     * 课堂代码做了下方的：TODO 但是应该还得把运行时的任务先执行吧？
     * 1.移除运行完成的任务
     * 2.把等待队列里面所有的任务取出来【执行】  AsyncCall2.run finished
     * @param call
     */
    public void finished(RealCall2.AsyncCall2 call) {
        runningAsyncCalls.remove(call);
        //考略等待队列里面是否有任务，如果有任务是需要执行的
        if(readyAsyncCalls.isEmpty()){
            return;
        }
        // 把等待队列中的任务给移移动到运行队列 TODO 这里没做运行队列的先执行完毕！！！ 完善！？
        for (RealCall2.AsyncCall2 readyAsyncCall : readyAsyncCalls) {
            readyAsyncCalls.remove(readyAsyncCall);//删除等待队列的任务
            runningAsyncCalls.add(readyAsyncCall);// 把刚刚删除的等待队列任务 加入到 运行队列
            // 开始执行任务
            executorService().execute(readyAsyncCall);
        }
    }
}
