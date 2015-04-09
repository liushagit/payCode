package com.og.platform.pay.common.domain;

import java.io.Serializable;

/**
 * 
 * @author author
 * 2013-01-28
 *
 */
public class OGMMPayConf implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;//主键
	private String appName;//游戏编号
	private String memo;//游戏备注名
	private String codeNo;//计费点编号
	private int codeType;//1 移动  2 联通  3 电信
	private String url;//在线地址
	private int isOpen;//是否开放
	private int provinceType;//1 根据屏蔽省份判断   2根据开通身份判断
	private String provinceHide;//屏蔽省份
	private String provinceOpen;//开通省份
	private String extData;//扩展字段
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCodeNo() {
		return codeNo;
	}
	public void setCodeNo(String codeNo) {
		this.codeNo = codeNo;
	}
	public int getCodeType() {
		return codeType;
	}
	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getIsOpen() {
		return isOpen;
	}
	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}
	public int getProvinceType() {
		return provinceType;
	}
	public void setProvinceType(int provinceType) {
		this.provinceType = provinceType;
	}
	public String getProvinceHide() {
		return provinceHide;
	}
	public void setProvinceHide(String provinceHide) {
		this.provinceHide = provinceHide;
	}
	public String getProvinceOpen() {
		return provinceOpen;
	}
	public void setProvinceOpen(String provinceOpen) {
		this.provinceOpen = provinceOpen;
	}
	public String getExtData() {
		return extData;
	}
	public void setExtData(String extData) {
		this.extData = extData;
	}
	
}
