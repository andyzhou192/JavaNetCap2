package com.protocol.http.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.util.TextUtils;
import org.json.JSONObject;

import com.protocol.http.HttptHelper;

public class HttpDataBean {
	
	private String protocolVersion;
	
	private String method;
	private String uri;
	private String host;
	private JSONObject requestHeader = new JSONObject();
	private JSONObject requestParam = new JSONObject();
	
	private int statusCode;
	private String reasonPhrase = "";
	private JSONObject responseHeader = new JSONObject();
	private String responseBody = "";

	public HttpDataBean(String reqData, String rspData) {
		try {
			initRequestData(URLDecoder.decode(reqData, "utf-8"));
			initResponseData(URLDecoder.decode(rspData, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			initRequestData(reqData);
			initResponseData(rspData);
		}
	}
	
	public JSONObject toJsonObject(){
		JSONObject requestData = new JSONObject();
		requestData.append("method", this.getMethod()).append("uri", this.getUri()).append("requestHeader", this.getRequestHeader()).append("requestParam", this.getRequestParam());
		JSONObject responseData = new JSONObject();
		responseData.append("statusCode", this.getStatusCode()).append("reasonPhrase", this.getReasonPhrase()).append("responseHeader", this.getResponseHeader()).append("responseBody", this.getResponseBody());
		JSONObject json = new JSONObject();
		json.append("protocolVersion", this.getProtocolVersion()).append("requestData", requestData).append("responseData", responseData);
		return json;
	}
	
	private void initRequestData(String requestData){
		int index = requestData.indexOf(HttptHelper.CRLF);
		String requestLine = requestData.trim().substring(0, index);
		this.method = requestLine.trim().split(HttptHelper.SP)[0];
		String[] uriStr = requestLine.trim().split(HttptHelper.SP)[1].split(HttptHelper.QUES);
		this.uri = uriStr[0];
		this.protocolVersion = requestLine.trim().split(HttptHelper.SP)[2];
		String[] data = requestData.trim().substring(index).split(HttptHelper.CRLF + HttptHelper.CRLF);
		int startIndex = data[0].toUpperCase().indexOf("HOST" + HttptHelper.NVSeparator) + ("HOST" + HttptHelper.NVSeparator).length();
		this.host = data[0].substring(startIndex).split(HttptHelper.CRLF)[0].trim();
		if(!TextUtils.isEmpty(data[0]) && !TextUtils.isBlank(data[0])){
			for(String nv : data[0].split(HttptHelper.CRLF)){
				if(!TextUtils.isEmpty(nv) && !TextUtils.isBlank(nv)){
					String name = nv.split(HttptHelper.NVSeparator)[0].trim();
					String value = nv.split(HttptHelper.NVSeparator).length > 1 ? nv.split(HttptHelper.NVSeparator)[1].trim() : "";
					/**
					 * 解决以下异常
					 * Caused by: org.apache.http.ProtocolException: Content-Length header already present
					 * Caused by: org.apache.http.ProtocolException: Content-Length header already present
					 */
					if(!name.equals("Content-Length") && !name.equals("Transfer-Encoding"))
						this.requestHeader.append(name, value);
				}
			}
		}
		switch(this.method){
		case "POST":
			if(data.length > 1)
				for(String nv : data[1].split(HttptHelper.AND)){
					if(!TextUtils.isEmpty(nv) && !TextUtils.isBlank(nv)){
						String name = nv.split(HttptHelper.EQUAL)[0];
						String value = nv.split(HttptHelper.EQUAL).length > 1 ? nv.split(HttptHelper.EQUAL)[1].trim() : "";
						this.requestParam.append(name, value);
					}
				}
		case "GET":
			if(uriStr.length > 1){
				for(String nv : uriStr[1].split(HttptHelper.AND)){
					if(!TextUtils.isEmpty(nv) && !TextUtils.isBlank(nv)){
						String name = nv.split(HttptHelper.EQUAL)[0];
						String value = nv.split(HttptHelper.EQUAL).length > 1 ? nv.split(HttptHelper.EQUAL)[1].trim() : "";
						this.requestParam.append(name, value);
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
	}
	
	private void initResponseData(String rspData){
		if(!rspData.startsWith("HTTP/1.1")) return;
		int index = rspData.indexOf(HttptHelper.CRLF);
		String[] rspStatusLine = rspData.trim().substring(0, index).trim().split(HttptHelper.SP);
		this.statusCode = Integer.valueOf(rspStatusLine[1]);
		if(rspStatusLine.length > 2)
			this.reasonPhrase = rspStatusLine[2];
		String[] data = rspData.trim().substring(index).split(HttptHelper.CRLF + HttptHelper.CRLF);
		if(!TextUtils.isEmpty(data[0]) && !TextUtils.isBlank(data[0])){
			for(String nv : data[0].split(HttptHelper.CRLF)){
				if(!TextUtils.isEmpty(nv) && !TextUtils.isBlank(nv)){
					String name = nv.split(HttptHelper.NVSeparator)[0].trim();
					String value = nv.split(HttptHelper.NVSeparator).length > 1 ? nv.split(HttptHelper.NVSeparator)[1].trim() : "";
					this.responseHeader.append(name, value);
				}
			}
		}
		this.responseBody = data.length > 2 ? data[1] : "";
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public JSONObject getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(JSONObject requestHeader) {
		this.requestHeader = requestHeader;
	}

	public JSONObject getRequestParam() {
		return requestParam;
	}

	public void setRequestParam(JSONObject requestParam) {
		this.requestParam = requestParam;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public JSONObject getResponseHeader() {
		return responseHeader;
	}

	public void setResponseHeader(JSONObject responseHeader) {
		this.responseHeader = responseHeader;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getUrl() {
		return this.getHost() + this.getUri();
	}

}
