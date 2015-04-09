package com.og.platform.pay.common.domain;

import java.io.Serializable;

public class MMUnpopularity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String imsi;
	private String imei;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}

}
