package com.og.platform.pay.common.domain;

import java.io.Serializable;

public class ZYHDInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String extData;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getExtData() {
		return extData;
	}
	public void setExtData(String extData) {
		this.extData = extData;
	}
	
}
