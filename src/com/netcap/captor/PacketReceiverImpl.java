package com.netcap.captor;

import com.common.util.LogUtil;
import com.netcap.handler.AsyncHandler;
import com.netcap.handler.DataQueues;
import com.protocol.http.HttptHelper;
import com.view.mainframe.MainFrame;

import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;

@SuppressWarnings("restriction")
public class PacketReceiverImpl implements PacketReceiver {
	private static Class<?> cl = PacketReceiverImpl.class;

	private static String reqData = "";
	private static String rspData = "";
	private MainFrame frame;
	private static int contentLen = -1000;
	private long startTime;
	
	public PacketReceiverImpl(MainFrame frame){
		this.frame = frame;
	}
	
	public void receivePacket(Packet packet) {
		startTime = System.currentTimeMillis();
		if(packet instanceof TCPPacket){
			TCPPacket tcpPacket = (TCPPacket) packet;
			String data = new String(tcpPacket.data);
			assemblePacket(data);
		}
	}
	
	private void assemblePacket(String data) {
		if(null != data && data.length() > 0) {
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
	}

	private void assembleRspPacket(String data, String method) {
		if(HttptHelper.isFirstResponse(data)){
			if("head".equalsIgnoreCase(method)){
				rspData = rspData + data;
				dealData();
				return;
			} else {
				contentLen = HttptHelper.getContentLenth(data);
			}
		} else if(rspData.length() == 0){ // 如果非首个响应数据包，且此时响应数据为空，则该包属于请求包
			reqData = reqData + data;
			return;
		}
		System.out.println(" data length =====>" + data.length());
		System.out.println(" contentLen =====>" + contentLen);
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
			//contentLen = contentLen - data.length();
			rspData = rspData + data;
			System.out.println("rspData.length ---------> " + rspData.length());
			System.out.println("rspData : " + rspData);
			long endTime = System.currentTimeMillis();
			if(contentLen <= rspData.length()){
				dealData();
			} else if((endTime - startTime) > 30 * 1000){
				dealData();
			}
		}
	}

	private void dealData() {
		LogUtil.debug(cl, "begin to show and save data...");
		AsyncHandler handler = new AsyncHandler();
		handler.setFrame(this.frame);
		//开启线程执行队列中的任务，那就是先到先得了
		new Thread(handler).start();
        //添加一个任务
		DataQueues.Task t =new DataQueues.Task(reqData, rspData);
		DataQueues.add(t); //执行该方法，激活所有对应队列，那两个线程就会开始执行啦
	}

}
