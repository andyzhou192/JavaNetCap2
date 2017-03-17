package com.protocol;

import net.sf.json.JSONObject;

public abstract class DataBean {
	private ProtocolEnum protocol;

	public DataBean(ProtocolEnum protocol){
		this.protocol = protocol;
	}
	
	public ProtocolEnum getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolEnum protocol) {
		this.protocol = protocol;
	}
	
	public abstract String toString();
	
	public abstract JSONObject toJson();
	
}
