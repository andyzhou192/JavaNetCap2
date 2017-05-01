package com.netcap.captor;

import com.common.util.LogUtil;
import com.view.mainframe.MainFrame;

public class CaptureThread implements Runnable {
	private Class<?> cl = CaptureThread.class;

	private NetCaptor captor;
	public Thread t;
    private String threadName;
//    private boolean suspended = false;
    private boolean stopped = false;
    
    public CaptureThread(String threadName, MainFrame parent){
        this.threadName=threadName;
        this.captor = new NetCaptor(parent);
    }

    public void run() {
    	synchronized(this) {
    		while(!stopped) {
//    			try {
    				int packetNum = captor.startCaptor();
    				LogUtil.console(cl, "received packet number : " + packetNum);
//    				synchronized(this) {
//    					while(suspended) {
//    						wait();
//    					}
//    				}
//    			} catch (InterruptedException e) {
//    				LogUtil.err(cl, "Thread " +  threadName + " interrupted.");
//    				LogUtil.err(cl, "Thread " +  threadName + e);
//    			}
    			LogUtil.console(cl, "Thread " +  threadName + " starting.");
    		}
    		LogUtil.console(cl, "Thread " +  threadName + " exiting.");
    	}
    }
    
    /**
     * 开始
     */
    public void start(){
    	LogUtil.debug(cl, "Starting " +  threadName );
		PacketReceiverImpl.STATUS = 1;
        stopped = false;
        if(t == null){
            t = new Thread(this, threadName);
            t.start();
        } else if (!t.isAlive()){
        	t.start();
        }
    }
    
    /**
     * 停止
     */
    public synchronized void stop(){
		PacketReceiverImpl.STATUS = 0;
    	stopped = true;
    	captor.stopCaptor();
    	notify();
    	//t = null;
    }
    
    /**
     * 暂停
     */
    public synchronized void suspend(){
//    	suspended = true;
    	PacketReceiverImpl.STATUS = 2;
    }
     
     /**
      * 继续
      */
     public synchronized void resume(){
//     	suspended = false;
     	PacketReceiverImpl.STATUS = 1;
        notify();
     }
}
