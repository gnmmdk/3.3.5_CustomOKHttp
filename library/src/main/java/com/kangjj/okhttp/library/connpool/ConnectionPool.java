package com.kangjj.okhttp.library.connpool;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @Description: Socket连接池
 * @Author: jj.kang
 * @Email: jj.kang@zkteco.com
 * @ProjectName: 3.3.5_CustomOKHttp
 * @Package: com.kangjj.okhttp.library.connpool
 * @CreateDate: 2019/12/10 17:05
 */
public class ConnectionPool {
    //容器（双端队列）用于保存连接对象
    private Deque<HttpConnection> httpConnectionDeque = null;
    //清理标记
    private boolean cleanRunnableFlag;
    //清理监测机制 每隔一分钟就去检查 连接池里面是否有可用的连接对象，如果不可用，就移除
    private long keepAlive;//最大允许闲置时间（默认一分钟）

    public ConnectionPool(){
        this(1,TimeUnit.MINUTES);
    }

    public ConnectionPool(long keepAlive, TimeUnit timeUnit){
        this.keepAlive = timeUnit.toMillis(keepAlive);
        httpConnectionDeque = new ArrayDeque<>();
    }


//   相当于 Executors.newSingleThreadExecutor()
    private Executor threadPoolExecutor = new ThreadPoolExecutor(
            0,
            Integer.MAX_VALUE,
            60, TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>(),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setName("threadPoolExecutor");
                    thread.setDaemon(true);
                    return thread;
                }
            }
    );

    /**
     * 获取 连接对象
     * @param host
     * @param port
     * @return
     */
    public synchronized HttpConnection getConnection(String host,int port){
        Iterator<HttpConnection> iterator = httpConnectionDeque.iterator();
        while (iterator.hasNext()) {
            HttpConnection httpConnection = iterator.next();
            if(httpConnection.isConnectionAction(host,port))
            {
                //把容器里面的连接对象移除 ，用完再加进来
                iterator.remove();
                return httpConnection;
            }
        }
        return null;
    }

    /**
     * 添加 连接对象
     * @param httpConnection
     */
    public synchronized void putConnection(HttpConnection httpConnection){
        //一旦添加的时候，就要去检查，连接池里面是否要去清理
        if(!cleanRunnableFlag){
            cleanRunnableFlag = true;
            threadPoolExecutor.execute(cleanRunnable);
        }
        httpConnectionDeque.add(httpConnection);
    }

    /**
     * 清理监测机制 开启一个任务，专门去检查，连接池里面的（连接对象）清理连接池里面的对象
     */
    private Runnable cleanRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                // 获取下一次检测的时间
                long nextCheckCleanTime = clean(System.currentTimeMillis());
                if(-1 == nextCheckCleanTime){
                    cleanRunnableFlag = false;
                    Log.d("cleanRunnable", "-1 == nextCheckCleanTime ");
                    return;//TODO 直接return 后续的Socket一超时不用关闭了吗？
                }
                if(nextCheckCleanTime > 0){
                    // 等待一段时间后，再去检查，是否要去清理
                    synchronized(ConnectionPool.this){
                        try {
                            Log.d("cleanRunnable", "nextCheckCleanTime="+nextCheckCleanTime);
                            ConnectionPool.this.wait(nextCheckCleanTime);//必须加锁
                            Log.d("cleanRunnable", "nextCheckCleanTime2");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("cleanRunnable", e.toString());
                        }
                    }
                }
            }
        }
    };

    /**
     * 清理那些连接对象的函数
     * @param currentTimeMillis
     * @return
     */
    private long clean(long currentTimeMillis){
        long idleRecordSave = -1;       //下一次的检查时间
        synchronized (this){
            //遍历容器操作
            Iterator<HttpConnection> iterator = httpConnectionDeque.iterator();
            while (iterator.hasNext()) {
                HttpConnection httpConnection = iterator.next();
                // 闲置时间的计算：
                // 添加了一个连接对象，超过了（最大闲置时间）就移除这个（连接对象）

                // 开始计算 ： 当前时间 - （连接对象）最后使用的时间
                long  idleTime = currentTimeMillis-httpConnection.hasUseTime;
                if(idleTime>keepAlive){
                    iterator.remove();
                    httpConnection.closeSocket();//这里移除掉了 队列里面就没有该超时时间，所以continue 不用计算idleRecordSave
                    continue;
                }

                if(idleRecordSave < idleTime){
                    idleRecordSave = idleTime;
                }

            }//end while
            //循环出来后，idleRecordSave = 计算完毕（闲置之间）
            if(idleRecordSave >=0){
                return keepAlive-idleRecordSave;
            }
        }
        return idleRecordSave;
    }

}
