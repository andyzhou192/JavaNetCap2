package com.netcap.handler;

import java.util.LinkedList;
import java.util.List;

import com.common.util.LogUtil;
import com.protocol.http.HttptHelper;
import com.protocol.http.bean.HttpDataBean;
import com.view.mainframe.MainFrame;
import com.view.preference.PropertyHelper;

public class DataQueues {

	public static List<Task> queue = new LinkedList<Task>();
	
    /**
     * 假如 参数t 为任务
     * @param t
     */
    public static void add (Task t){
        synchronized (DataQueues.queue) {
        	DataQueues.queue.add(t); //添加任务
        	DataQueues.queue.notifyAll();//激活该队列对应的执行线程，全部Run起来
        }
    }
    
    public static class Task{
    	private static Class<?> cl = Task.class;
    	
    	private String reqData;
    	private String rspData;
    	private MainFrame frame;

        public void setFrame(MainFrame frame){
        	this.frame = frame;
        }
    	
    	public Task(String reqData, String rspData){
    		this.reqData = reqData;
    		this.rspData = rspData;
    	}
    	
        public void dataProcess(){
        	HttpDataBean bean = HttptHelper.getDataBean(reqData, rspData);
        	if (bean != null && isValidReq(bean.getUrl())) {
            	LogUtil.debug(cl, bean.toJson());
            	if(null != this.frame)
            		frame.getScrollPane().addRowToTable(bean);
            }
        }
    }
    
    public static boolean isValidReq(String url){
    	String captureUrl = PropertyHelper.getCaptureUrl();
    	if(null == url || url.endsWith(".js") || url.endsWith(".css") || url.endsWith(".png"))
    		return false;
    	else if(null == captureUrl || url.contains(captureUrl)){
    		return true;
    	}
		return false;
    }

}
