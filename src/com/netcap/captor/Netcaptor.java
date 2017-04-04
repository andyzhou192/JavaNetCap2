package com.netcap.captor;

import java.io.IOException;

import com.common.util.LogUtil;
import com.view.mainframe.MainFrame;
import com.view.preference.PropertyHelper;
import com.view.util.ViewModules;

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
		int net_devices_index = PropertyHelper.getNetDevicesIndex();
		int caplen = PropertyHelper.getCaptureLength();
		boolean isPromisc = PropertyHelper.getPromisc();
		try {
			jpcap = JpcapCaptor.openDevice(JpcapCaptor.getDeviceList()[net_devices_index], caplen, isPromisc, 5000);
			jpcap.setFilter(PropertyHelper.getProtocolType().toLowerCase(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jpcap;
	}

	/**
	 * 开始抓包
	 */
	public static void startCapture(final MainFrame frame) {
		LogUtil.debug(cl, "Capture Thread is started...");
		String msgTitle = "提示信息";
		if (captureThread != null && captureThread.isAlive()){
			String message = "抓包已经处于启动状态！";
			ViewModules.showMessageDialog(null, message, msgTitle, 1);
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
		LogUtil.debug(cl, "Capture Thread is stoped...");
	}
	
}