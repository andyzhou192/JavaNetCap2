package com.netcap.captor;

import java.io.IOException;

import com.common.util.ConstsUtil;
import com.common.util.LogUtil;
import com.netcap.view.MainView;
import com.netcap.view.util.ViewModules;

import jpcap.JpcapCaptor;

@SuppressWarnings("restriction")
public class Netcaptor {

	private final static Class<?> cl = Netcaptor.class;
	
	public static Thread captureThread;
	public volatile static boolean exit = false;
	
	/**
	 * 输入配置信息
	 */
	public static JpcapCaptor getJpcapCaptor() {
		JpcapCaptor jpcap = null;
		int net_devices_index = Integer.valueOf(ConstsUtil.PROPS.getProperty("net_devices_index"));
		int caplen = Integer.valueOf(ConstsUtil.PROPS.getProperty("capture_length"));
		boolean isPromisc = Boolean.valueOf(ConstsUtil.PROPS.getProperty("promisc"));
		try {
			jpcap = JpcapCaptor.openDevice(JpcapCaptor.getDeviceList()[net_devices_index], caplen, isPromisc, 5000);
			jpcap.setFilter(ConstsUtil.PROPS.getProperty("protocol_type").toLowerCase(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jpcap;
	}

	/**
	 * 开始抓包
	 */
	public static void startCapture(final MainView frame) {
		System.out.println("线程启动!");
		LogUtil.debug(cl, "Capture Thread is started...");
		String msgTitle = "提示信息";
		if (captureThread != null && captureThread.isAlive()){
			String message = "抓包已经处于启动状态！";
			ViewModules.showMessageDialog(frame.getContentPane(), message, msgTitle, 1);
			return;
		} else {
			captureThread = new Thread(new Runnable() {
				public void run() {
					while (!exit){
						int packetNum = Netcaptor.getJpcapCaptor().processPacket(-1, new PacketReceiverImpl(frame));
						LogUtil.debug(cl, "received packet number : " + packetNum);
					}
				}
			});
			captureThread.setPriority(Thread.MIN_PRIORITY);
			captureThread.start();
		}
	}

	/**
	 * 停止抓包
	 */
	public static void stopCapture() {
		Netcaptor.exit = true;
		Netcaptor.captureThread = null;
        System.out.println("线程退出!");
		LogUtil.debug(cl, "Capture Thread is stoped...");
	}
	
}