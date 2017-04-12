package com.netcap.handler;

import java.nio.charset.Charset;
import java.util.LinkedList;

import com.common.util.LogUtil;
import com.protocol.http.HttptHelper;
import com.view.mainframe.MainFrame;

import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

@SuppressWarnings("restriction")
public class PacketAsyncHandler implements Runnable {
	private static Class<?> cl = PacketAsyncHandler.class;

	public static LinkedList<Packet> packetQueue = new LinkedList<Packet>();

	/**
	 * 假如 参数t 为任务
	 * 
	 * @param t
	 */
	public static void add(Packet packet) {
		synchronized (PacketAsyncHandler.packetQueue) {
			PacketAsyncHandler.packetQueue.addFirst(packet); // 添加任务
			PacketAsyncHandler.packetQueue.notifyAll();// 激活该队列对应的执行线程，全部Run起来
		}
	}

	@Override
	public void run() {
		while(true){
            synchronized (PacketAsyncHandler.packetQueue) {
                while(PacketAsyncHandler.packetQueue.isEmpty()){ //
                    try {
                    	PacketAsyncHandler.packetQueue.wait(); //队列为空时，使线程处于等待状态
                    } catch (InterruptedException e) {
                    	LogUtil.err(cl, e);
                    }
                    LogUtil.debug(cl, "wait...");
                }
                Packet packet = PacketAsyncHandler.packetQueue.removeLast();
                if(packet instanceof TCPPacket){
        			TCPPacket tcpPacket = (TCPPacket) packet;
        			String data = new String(tcpPacket.data);
        			assemblePacket(data);
        		}
            }
        }
	}
	
	private static String reqData = "";
	private static String rspData = "";
	private static int contentLen = -1000;
	
	/**
	 * 
	 * @param data
	 */
	private void assemblePacket(String data) {
		System.out.println("---->" + data);
		String method = HttptHelper.checkHttpRequest(data);
		if(method != null){
			if(reqData.length() > 0){
				dealData();
			}
			reqData = data;
			rspData = "";
		} else {
			assembleRspPacket(data, method);
		}
	}

	/**
	 * 
	 * @param data
	 * @param method
	 */
	private void assembleRspPacket(String data, String method) {
		if(HttptHelper.isFirstResponse(data)){
			if("head".equalsIgnoreCase(method)){
				rspData = rspData + data;
				dealData();
				return;
			} else {
				contentLen = HttptHelper.getContentLenth(data);
			}
		} else if(rspData.length() < 1){ // 如果非首个响应数据包，且此时响应数据为空，则该包属于请求包
			reqData = reqData + data;
			return;
		} else {
			LogUtil.debug(cl, "response data is not first response packet, and not a request data...");
		}
		if(contentLen == -1){
			rspData = rspData + data;
			dealData();
		} else if(contentLen == -2){
			if(HttptHelper.isLastChunk(data)){
				dealData();
			} else{
				rspData = rspData + data;
			}
		} else {
			rspData = rspData + data;
			int rspDataLength = rspData.getBytes(Charset.forName("utf-8")).length;
			LogUtil.debug(cl, "reqData ---------> " + reqData);
			LogUtil.debug(cl, "rspData =========> " + rspData);
			LogUtil.debug(cl, "contentLen ---------> " + contentLen);
			LogUtil.debug(cl, "rspDataLength =========> " + rspDataLength);
			if(contentLen <= rspDataLength){
				dealData();
			} else{
				LogUtil.debug(cl, "response data has not complete...");
			}
		}
	}

	/**
	 * 
	 */
	private void dealData() {
		LogUtil.debug(cl, "begin to show and save data...");
		startDealDataThread();
        //添加一个任务
		DataQueues.Task t =new DataQueues.Task(reqData, rspData);
		DataQueues.add(t); //执行该方法，激活所有对应队列，那两个线程就会开始执行啦
	}

	private Thread dealDataThread = null;
	private MainFrame frame;
	
	public void setFrame(MainFrame frame) {
		this.frame = frame;
	}
	
	/**
	 * 
	 */
	private void startDealDataThread(){
		if(null == dealDataThread){
			AsyncHandler handler = new AsyncHandler();
			handler.setFrame(this.frame);
			//开启线程执行队列中的任务，那就是先到先得了
			dealDataThread = new Thread(handler);
			dealDataThread.start();
		} else if(!dealDataThread.isAlive()){
			dealDataThread.start();
		}
		
	}
}
