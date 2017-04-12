package com.netcap.captor;

//import com.common.util.LogUtil;
//import com.netcap.handler.AsyncHandler;
//import com.netcap.handler.DataQueues;
import com.netcap.handler.PacketAsyncHandler;
//import com.protocol.http.HttptHelper;
import com.view.mainframe.MainFrame;

import jpcap.PacketReceiver;
import jpcap.packet.Packet;
//import jpcap.packet.TCPPacket;

@SuppressWarnings("restriction")
public class PacketReceiverImpl implements PacketReceiver {
//	private static Class<?> cl = PacketReceiverImpl.class;

//	private static String reqData = "";
//	private static String rspData = "";
	private MainFrame frame;
//	private static int contentLen = -1000;
//	
//	private Thread dealDataThread = null;
	
	private Thread packetHandlerThread = null;
	
	public PacketReceiverImpl(MainFrame frame){
		this.frame = frame;
	}
	
	/**
	 * 
	 */
	@Override
	public void receivePacket(Packet packet) {
		PacketAsyncHandler handler = new PacketAsyncHandler();
		handler.setFrame(frame);
		PacketAsyncHandler.add(packet);
		if(null == packetHandlerThread){
			packetHandlerThread = new Thread(handler);
			packetHandlerThread.start();
		} else if(!packetHandlerThread.isAlive()){
			packetHandlerThread.start();
		}
//		if(packet instanceof TCPPacket && null != packet.data && packet.data.length > 0){
//			TCPPacket tcpPacket = (TCPPacket) packet;
//			String data = new String(tcpPacket.data);
//			assemblePacket(data);
//		}
	}
	
//	/**
//	 * 
//	 * @param data
//	 */
//	private void assemblePacket(String data) {
//		String method = HttptHelper.checkHttpRequest(data);
//		if(method != null){
//			if(reqData.length() > 0){
//				dealData();
//			}
//			reqData = data;
//			rspData = "";
//		} else {
//			assembleRspPacket(data, method);
//		}
//	}
//
//	/**
//	 * 
//	 * @param data
//	 * @param method
//	 */
//	private void assembleRspPacket(String data, String method) {
//		if(HttptHelper.isFirstResponse(data)){
//			if("head".equalsIgnoreCase(method)){
//				rspData = rspData + data;
//				dealData();
//				return;
//			} else {
//				contentLen = HttptHelper.getContentLenth(data);
//			}
//		} else if(rspData.length() == 0){ // 如果非首个响应数据包，且此时响应数据为空，则该包属于请求包
//			reqData = reqData + data;
//			return;
//		} else {
//			LogUtil.debug(cl, "response data is not first response packet, and not a request data...");
//		}
//		if(contentLen == -1){
//			rspData = rspData + data;
//			dealData();
//		} else if(contentLen == -2){
//			if(HttptHelper.isLastChunk(data)){
//				dealData();
//			} else{
//				rspData = rspData + data;
//			}
//		} else {
//			rspData = rspData + data;
//			LogUtil.debug(cl, "reqData ---------> " + reqData);
//			LogUtil.debug(cl, "rspData =========> " + rspData);
//			if(contentLen <= rspData.getBytes().length){
//				dealData();
//			} else{
//				LogUtil.debug(cl, "response data has not complete...");
//			}
//		}
//	}
//
//	/**
//	 * 
//	 */
//	private void dealData() {
//		LogUtil.debug(cl, "begin to show and save data...");
//		startDealDataThread();
//        //添加一个任务
//		DataQueues.Task t =new DataQueues.Task(reqData, rspData);
//		DataQueues.add(t); //执行该方法，激活所有对应队列，那两个线程就会开始执行啦
//		reqData = ""; 
//		rspData = "";
//	}
//	
//	/**
//	 * 
//	 */
//	private void startDealDataThread(){
//		if(null == dealDataThread){
//			AsyncHandler handler = new AsyncHandler();
//			handler.setFrame(this.frame);
//			//开启线程执行队列中的任务，那就是先到先得了
//			dealDataThread = new Thread(handler);
//			dealDataThread.start();
//		} else if(!dealDataThread.isAlive()){
//			dealDataThread.start();
//		}
//		
//	}

}
