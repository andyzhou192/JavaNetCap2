package com.protocol.http.bean;

import net.sf.json.JSONObject;

import com.common.util.JsonUtil;
import com.protocol.DataBean;
import com.protocol.ProtocolEnum;

public class HttpDataBean extends DataBean {
	
	public HttpDataBean() {
		super(ProtocolEnum.HTTP);
	}

	private String protocolVersion;
	
	private String method;
	private String url;
	private JSONObject reqHeader;
	private JSONObject reqParams;
	
	private int statusCode;
	private String reasonPhrase;
	private JSONObject rspHeader;
	private Object rspBody;

	

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

	public JSONObject getReqHeader() {
		return reqHeader;
	}

	public void setReqHeader(JSONObject reqHeader) {
		this.reqHeader = reqHeader;
	}

	public JSONObject getReqParams() {
		return reqParams;
	}

	public void setReqParams(JSONObject reqParams) {
		this.reqParams = reqParams;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReasonPhrase() {
		return (null == reasonPhrase) ? "" : reasonPhrase;
	}

	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

	public JSONObject getRspHeader() {
		return rspHeader;
	}

	public void setRspHeader(JSONObject rspHeader) {
		this.rspHeader = rspHeader;
	}

	public Object getRspBody() {
		return (null == rspBody) ? "" : rspBody;
	}

	public void setRspBody(Object rspBody) {
		this.rspBody = rspBody;
	}

	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url){
		this.url = url;
	}

	@Override
	public String toString() {
		return "protocol:" + super.getProtocol() + ", protocolVersion:" + this.protocolVersion + ", URL:" + this.getUrl() +
				", method:" + this.method + ", requestHeader:" + this.reqHeader.toString() +
				", requestParam:" + this.reqParams.toString() + ", statusCode:" + this.statusCode + ", reasonPhrase:" + this.reasonPhrase +
				", responseHeader:" + this.rspHeader + ", responseBody:" + this.rspBody;
	}

	@Override
	public JSONObject toJson() {
		return JSONObject.fromObject(JsonUtil.beanToJson(this));
	}
	
}
