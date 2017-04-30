package com.netcap.captor;

import com.common.util.LogUtil;
import com.netcap.handler.PacketAsyncHandler;
import com.netcap.handler.PacketQueues;
import com.view.mainframe.MainFrame;

import jpcap.PacketReceiver;
import jpcap.packet.Packet;

@SuppressWarnings("restriction")
public class PacketReceiverImpl implements PacketReceiver {
	private MainFrame frame;
	
	private Thread packetHandlerThread = null;
	static int index = 1;
	
	public PacketReceiverImpl(MainFrame frame){
		this.frame = frame;
	}
	
	/**
	 * 
	 */
	@Override
	public void receivePacket(Packet packet) {
		LogUtil.console(PacketReceiverImpl.class, "---------------" + (index++));
		if(null != packet.data && packet.data.length > 0){
			startPacketThread();
			PacketQueues.Task task = new PacketQueues.Task(packet);
			PacketQueues.add(task);
		}
	}
	
	/**
	 * 
	 */
	private void startPacketThread(){
		if(null == packetHandlerThread){
			PacketAsyncHandler handler = new PacketAsyncHandler(this.frame);
			//开启线程执行队列中的任务，那就是先到先得了
			packetHandlerThread = new Thread(handler);
			packetHandlerThread.start();
		} else if(!packetHandlerThread.isAlive()){
			packetHandlerThread.start();
		} else {
			//packetHandlerThread.notifyAll();
		}
		
	}

}
