package com.netcap.captor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.common.util.LogUtil;
import com.common.util.StringUtil;
import com.view.mainframe.MainFrame;
import com.view.preference.PropertyHelper;
import com.view.util.ViewModules;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;

@SuppressWarnings("restriction")
public class Netcaptor {

	private final static Class<?> cl = Netcaptor.class;
	
	public static Thread captureThread;
	public volatile static boolean exit = false;
	
	public static Map<String, NetworkInterface> devicesMap = Netcaptor.getNetDeviceMap();
	/**
	 * 输入配置信息
	 */
	public static JpcapCaptor getJpcapCaptor() {
		JpcapCaptor jpcap = null;
		String net_devices_name = PropertyHelper.getNetDevicesName();
		int caplen = PropertyHelper.getCaptureLength();
		boolean isPromisc = PropertyHelper.getPromisc();
		try {
			jpcap = JpcapCaptor.openDevice(Netcaptor.devicesMap.get(net_devices_name), caplen, isPromisc, 5000);
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
	
	/**
	 * 获取所有网卡的名称
	 * @return
	 */
	private static Map<String, NetworkInterface> getNetDeviceMap(){
		Map<String, NetworkInterface> devicesMap = new HashMap<String, NetworkInterface>();
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
		if (devices == null) {
			return null;
		} else {
			for (int i = 0; i < devices.length; i++) {
				for(jpcap.NetworkInterfaceAddress addr : devices[i].addresses){
					if(addr != null && addr.address != null && StringUtil.isIPV4(addr.address.getHostAddress())){
						String ipv4 = addr.address.getHostAddress();
						if(!"0.0.0.0".equals(ipv4.trim())){
							String desc = devices[i].description == null ? "" : devices[i].description;
							devicesMap.put(desc + "[" + ipv4 + "]", devices[i]);
						}
						break;
					}
					continue;
				}
			}
			return devicesMap;
		}
	}
	
//	public static void main(String[] args) throws java.net.SocketException {
//	javax.swing.JFrame frame = new javax.swing.JFrame();
//	JcaptureSettingView inst = new JcaptureSettingView(frame);
//	inst.setVisible(true);
//	java.util.Enumeration<java.net.NetworkInterface> e = java.net.NetworkInterface
//			.getNetworkInterfaces();
//	while (e.hasMoreElements()) {
//		java.net.NetworkInterface ni = e.nextElement();
//		System.out.println("displayname: " + ni.getDisplayName());
//		System.out.println("name: " + ni.getName());
//		System.out.println("MTU: " + ni.getMTU());
//		System.out.println("Loopback: " + ni.isLoopback());
//		System.out.println("Virtual: " + ni.isVirtual());
//		System.out.println("Up: " + ni.isUp());
//		System.out.println("PointToPoint: " + ni.isPointToPoint());
//		java.util.Enumeration<java.net.InetAddress> addresses = ni.getInetAddresses();
//		while (addresses.hasMoreElements()) {
//			String ip = addresses.nextElement().getHostAddress();
//			if(ip.contains("."))
//				System.out.println(ip);
//		}
//		byte[] mac = ni.getHardwareAddress();
//		if (mac != null)
//			System.out.println(displayMac(mac));
//		else
//			System.out.println("mac is null");
//		System.out.println("-----");
//	}
	
	
//	NetworkInterface[] devices = JpcapCaptor.getDeviceList();
//	if (devices == null) {
//		return;
//	} else {
//		String[] names = new String[devices.length];
//		for (int i = 0; i < names.length; i++) {
//			names[i] = (devices[i].description == null ? devices[i].name : devices[i].description);
//			System.out.println(" : " + devices[i].name);
//			System.out.println(" : " + devices[i].description);
//			System.out.println(" : " + JcaptureSettingView.displayMac(devices[i].mac_address));
//			System.out.println(" : " + devices[i].addresses[1].address.getHostAddress());
//			System.out.println();
//		}
//	}
//}

public static String displayMac(byte[] mac) {
	String macStr = "";
	for (int i = 0; i < mac.length; i++) {
		byte b = mac[i];
		int intValue = 0;
		if (b >= 0)
			intValue = b;
		else
			intValue = 256 + b;
		//System.out.print(Integer.toHexString(intValue));
		macStr = macStr + Integer.toHexString(intValue);
		if (i != mac.length - 1){
//			System.out.print("-");
			macStr = macStr + "-";
		}
	}
	return macStr;
}    
}