package com.protocol.http;

public class HttptHelper {

	public static String[] HttpMethods = {"POST", "GET", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT"};
	public final static String SP = " ";
	public final static String CRLF = "\r\n";
	public final static String COLON = ":";
	public final static String NVSeparator = COLON + SP; //头的name和value分隔符
	public final static String QUES = "\\?";
	public final static String EQUAL = "=";
	public final static String AND = "&";
	
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
	
//	public static void main(String[] args) {
//		System.out.println("abc".split("d")[0]);
//	}
}
