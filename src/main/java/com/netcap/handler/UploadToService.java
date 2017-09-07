package com.netcap.handler;

//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;

import com.common.Constants;
import com.common.util.JsonUtil;
import com.common.util.LogUtil;
import com.generator.bean.DataForJavaBean;

/**
 * 上传数据到平台
 * 
 * @author 周叶林
 *
 */
@SuppressWarnings("deprecation")
public class UploadToService {
	private static Class<?> cl = UploadToService.class;

	@SuppressWarnings({ "resource" })
	public static void upload(DataForJavaBean bean) {
		String json = JsonUtil.beanToJson(bean);
//		List<String> dataList = readDataFromFile(new java.io.File("D:\\work\\workspace\\JavaNetCap2\\data\\test.json"));
//		String json = dataList.get(0);
		LogUtil.debug(cl, "UploadToServiceData---->" + json);
		try {
			HttpClient httpclient = new DefaultHttpClient();
			//String uri = "http://172.20.3.222:8080/http_data_receive";
			String uri = Constants.PROPS.getProperty("serviceUrl");
			HttpPost httppost = new HttpPost(uri);
			// 添加http头信息
			httppost.addHeader("Content-Type", "application/json");
			httppost.setEntity(new StringEntity(json));
			HttpResponse response;
			response = httpclient.execute(httppost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			String msg = response.getStatusLine().getReasonPhrase();
			LogUtil.debug(cl, code + ":" + msg);
//			System.out.println(code + ":" + msg);
//			if (code == 200) {
//				String rev = EntityUtils.toString(response.getEntity());
//			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * 
//	 * @param file
//	 * @return
//	 */
//	private static List<String> readDataFromFile(File file){
//		List<String> dataList = new ArrayList<String>();
//		try {
//			// 文件读入通道连向文件对象
//			FileReader fr = new FileReader(file);
//			// 定义文件缓冲区
//			BufferedReader br = new BufferedReader(fr);
//			String aline;
//			// 按行读取文本，每行附加在多行文本区之后
//			String data = "";
//			while ((aline = br.readLine()) != null){
//				if(data.length() > 0){
//					dataList.add(data);
//					data = "";
//				} else {
//					data = data + aline.trim();
//				}
//			}
//			if(data.length() > 0){
//				dataList.add(data);
//			}
//			fr.close();
//			br.close(); // 关闭文件缓冲区
//		} catch (IOException ioe) {// 输入输出异常捕获
//			
//		}
//		return dataList;
//	}
//	
//	public static void main(String[] args) {
//		upload(null);
//	}
}
