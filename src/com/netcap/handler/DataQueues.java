package com.netcap.handler;

import java.util.LinkedList;
import java.util.List;

import com.common.util.LogUtil;
import com.dataprocess.DataSaveHandler;
import com.netcap.view.MainView;
import com.protocol.http.bean.HttpDataBean;

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
    	private MainView frame;

        public void setFrame(MainView frame){
        	this.frame = frame;
        }
    	
    	public Task(String reqData, String rspData){
    		this.reqData = reqData;
    		this.rspData = rspData;
    	}
    	
        public void dataProcess(){
        	HttpDataBean dataBean = new HttpDataBean(reqData, rspData);
        	if (dataBean != null && isValidReq(dataBean.getUri())) {
            	LogUtil.debug(cl, dataBean);
            	if(null != this.frame)
            		frame.updateView(dataBean);
            	//DataSaveHandler.saveToExcel(dataBean);
            }
        }
    }
    
    public static boolean isValidReq(String url){
    	if(null == url || url.endsWith(".js") || url.endsWith(".css") || url.endsWith(".png"))
    		return false;
    	return true;
    }

}
