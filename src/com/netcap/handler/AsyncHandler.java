package com.netcap.handler;

import com.common.util.LogUtil;
import com.netcap.view.MainView;

/**
 * <pre> 简单异步处理示例. </pre>
 * 
 * @author zhouyelin
 * @version $Id: AsyncHandler.java, v 0.1 2017年1月17日 下午8:47:54 zhouyelin Exp $
 */
public class AsyncHandler implements Runnable {
	private static Class<?> cl = AsyncHandler.class;

    private MainView frame;

    public void setFrame(MainView frame){
    	this.frame = frame;
    }

	public void run() {
		while(true){
            synchronized (DataQueues.queue) {
                while(DataQueues.queue.isEmpty()){ //
                    try {
                    	DataQueues.queue.wait(); //队列为空时，使线程处于等待状态
                    } catch (InterruptedException e) {
                    	LogUtil.err(cl, e);
                    }
                    LogUtil.debug(cl, "wait...");
                }
                DataQueues.Task t= DataQueues.queue.remove(0); //得到第一个
                t.setFrame(this.frame);
                t.dataProcess(); //执行该任务
            }
        }
	}


}
