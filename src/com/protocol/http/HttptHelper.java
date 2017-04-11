package com.protocol.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.common.asserts.Assert;
import com.common.util.StringUtil;
import com.protocol.http.bean.HttpDataBean;

import net.sf.json.JSONObject;

public class HttptHelper {

	public static String[] HttpMethods = {"POST", "GET", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT"};
	public final static String SP = " ";
	public final static String CRLF = "\r\n";
	public final static String COLON = ":";
	public final static String NVSeparator = COLON + SP; //头的name和value分隔符
	public final static String QUES = "\\?";
	public final static String EQUAL = "=";
	public final static String AND = "&";
	
	public final static String[] PARAM_NAMES = {"CaseID", "CaseDesc", "Method", "URL","ReqHeader","ReqParams","StatusCode","ReasonPhrase","RspHeader","RspBody"};
	
	@SuppressWarnings("deprecation")
	public static HttpDataBean getDataBean(String reqData, String rspData) {
		HttpDataBean bean = new HttpDataBean();
		try {
			reqData = URLDecoder.decode(reqData.replaceAll("%(?![0-9a-fA-F]{2})", "%25"), "utf-8");
			rspData = URLDecoder.decode(rspData.replaceAll("%(?![0-9a-fA-F]{2})", "%25"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			reqData = URLDecoder.decode(reqData.replaceAll("%(?![0-9a-fA-F]{2})", "%25"));
			rspData = URLDecoder.decode(rspData.replaceAll("%(?![0-9a-fA-F]{2})", "%25"));
		} finally {
			initRequestData(bean, reqData);
			initResponseData(bean, rspData);
		}
		return bean;
	}
	
	private static void initRequestData(HttpDataBean bean, String requestData){
		int index = requestData.indexOf(HttptHelper.CRLF);
		String requestLine = requestData.trim().substring(0, index);
		bean.setMethod(requestLine.trim().split(HttptHelper.SP)[0]);
		String[] uriStr = requestLine.trim().split(HttptHelper.SP)[1].split(HttptHelper.QUES);
		String uri = uriStr[0];
		bean.setProtocolVersion(requestLine.trim().split(HttptHelper.SP)[2]);
		String[] data = requestData.trim().substring(index).split(HttptHelper.CRLF + HttptHelper.CRLF);
		int startIndex = data[0].toUpperCase().indexOf("HOST" + HttptHelper.NVSeparator) + ("HOST" + HttptHelper.NVSeparator).length();
		String host = data[0].substring(startIndex).split(HttptHelper.CRLF)[0].trim();
		bean.setUrl("http://" + host + uri);
		if(StringUtil.validate(data[0])){
			JSONObject requestHeader = new JSONObject();
			for(String nv : data[0].split(HttptHelper.CRLF)){
				if(StringUtil.validate(nv)){
					String name = nv.split(HttptHelper.NVSeparator)[0].trim();
					String value = nv.split(HttptHelper.NVSeparator).length > 1 ? nv.split(HttptHelper.NVSeparator)[1].trim() : "";
					/**
					 * 解决以下异常
					 * Caused by: org.apache.http.ProtocolException: Content-Length header already present
					 * Caused by: org.apache.http.ProtocolException: Content-Length header already present
					 */
					if(!name.equals("Content-Length") && !name.equals("Transfer-Encoding"))
						requestHeader.put(name, value);
				}
			}
			bean.setReqHeader(requestHeader);
		}
		JSONObject requestParam = new JSONObject();
		switch(bean.getMethod()){
		case "POST":
			if(data.length > 1)
				for(String nv : data[1].split(HttptHelper.AND)){
					if(StringUtil.validate(nv)){
						String name = nv.split(HttptHelper.EQUAL)[0];
						String value = nv.split(HttptHelper.EQUAL).length > 1 ? nv.split(HttptHelper.EQUAL)[1].trim() : "";
						requestParam.put(name, value);
					}
				}
		case "GET":
			if(uriStr.length > 1){
				for(String nv : uriStr[1].split(HttptHelper.AND)){
					if(StringUtil.validate(nv)){
						String name = nv.split(HttptHelper.EQUAL)[0];
						String value = nv.split(HttptHelper.EQUAL).length > 1 ? nv.split(HttptHelper.EQUAL)[1].trim() : "";
						requestParam.put(name, value);
					}
				}
			}
			break;
		case "PUT":
		case "DELETE":
		case "OPTIONS":
		case "HEAD":
		case "TRACE":
		case "CONNECT":
		default:
			break;
		}
		bean.setReqParams(requestParam);
	}
	
	private static void initResponseData(HttpDataBean bean, String rspData){
		if(!rspData.startsWith("HTTP/1.1")) return;
		int index = rspData.indexOf(HttptHelper.CRLF);
		String[] rspStatusLine = rspData.trim().substring(0, index).trim().split(HttptHelper.SP);
		bean.setStatusCode(Integer.valueOf(rspStatusLine[1]));
		if(rspStatusLine.length > 2)
			bean.setReasonPhrase(rspStatusLine[2]);
		String[] data = rspData.trim().substring(index).split(HttptHelper.CRLF + HttptHelper.CRLF);
		if(StringUtil.validate(data[0])){
			JSONObject responseHeader = new JSONObject();
			for(String nv : data[0].split(HttptHelper.CRLF)){
				if(StringUtil.validate(nv)){
					String name = nv.split(HttptHelper.NVSeparator)[0].trim();
					String value = nv.split(HttptHelper.NVSeparator).length > 1 ? nv.split(HttptHelper.NVSeparator)[1].trim() : "";
					responseHeader.put(name, value);
				}
			}
			bean.setRspHeader(responseHeader);
		}
		bean.setRspBody(data.length > 1 ? data[1] : "");
	}
	
	public static String checkHttpRequest(String data){
		String method = null;
		if(data.length() > 1){
			for(String m : HttpMethods){
				if(data.split(HttptHelper.SP)[0].trim().equals(m)){
					method = m;
					break;
				}
			}
		}
		return method;
	}
	
	public static boolean isFirstResponse(String data){
		if(data.length() > 1){
			if(data.split(HttptHelper.SP)[0].trim().equals("HTTP/1.1")){
				return true;
			}
		}
		return false;
	}
	
	public static int getContentLenth(String data){
		int result = -1000;
		if(isIgnoreEntity(data)){
			result = -1;
		} else if(hasChunked(data)){
			result = -2;
		} else {
			int begin = data.indexOf("Content-Length" + HttptHelper.NVSeparator) + ("Content-Length" + HttptHelper.NVSeparator).length();
			int headerLen = data.split(HttptHelper.CRLF + HttptHelper.CRLF)[0].length();
			String lenValue = data.substring(begin).split(HttptHelper.CRLF)[0].trim();
			result = headerLen + Integer.valueOf(lenValue.length() > 0 ? lenValue : "0");
		}
		return result;
	}

	public static boolean hasChunked(String data){ //Transfer-Encoding: chunked
		return data.contains("Transfer-Encoding" + HttptHelper.NVSeparator + "chunked");
	}
	
	public static boolean isLastChunk(String data){
		return ("0".equals(data.split(HttptHelper.CRLF)[0].trim()));
	}
	
	public static boolean isIgnoreEntity(String data){
		int statusCode = Integer.valueOf(data.split(HttptHelper.SP)[1].trim());
		if(statusCode < 200 || statusCode == 204 || statusCode == 304)
			return true;
		return false;
	}
	
	public static String getInterfaceMethodName(String url){
		Assert.notEmpty(url, "url");
		String fileName = "";
		if(url.contains("?")){
			String temp = url.substring(0, url.lastIndexOf("?") - 1);
			fileName = getInterfaceMethodName(temp);
		} else if(url.contains("/")){
			if(url.trim().endsWith("/")){
				String temp = url.substring(0, url.lastIndexOf("/") - 1);
				fileName = getInterfaceMethodName(temp);
			} else {
				String temp = url.substring(url.lastIndexOf("/") + 1).trim();
				fileName = getInterfaceMethodName(temp);
			}
		} else {
			if(url.contains(":")){
				fileName = url.substring(0, url.lastIndexOf(":") - 1).trim();
			} else {
				fileName = url;
			}
			return fileName;
		}
		return fileName;
	}
	
//	public static void main(String[] args) {
//		HttpDataBean bean = HttptHelper.getDataBean("GET / HTTP/1.1\r\nHost: 172.23.29.173\r\nContent-Type: */*\r\n\r\na=b&c=d", "HTTP/1.1 200 OK\r\nContent-Length: 10\r\n\r\nabcdef");
//		String jsonString = com.common.util.JsonUtil.beanToJson(bean);
//		System.out.println(jsonString);
//		HttpDataBean data = com.common.util.JsonUtil.jsonToBean(jsonString, HttpDataBean.class);
//	}
}
