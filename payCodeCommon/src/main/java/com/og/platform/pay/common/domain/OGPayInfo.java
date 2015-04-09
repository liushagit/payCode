/**
 * 
 */
package com.og.platform.pay.common.domain;

import java.io.Serializable;

/**
 * @author ogplayer.com
 * 
 *         2013.10.18
 */
public class OGPayInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String billId;
	private int billType;
	private String codeNO;
	private int codeType;
	private String price;
	private String url;
	private int urlNO;
	private int priority;
	private boolean isOpen;
	private int provinceType;
	private String provinceHide;
	private String provinceOpen;
	private String extData;
	private String company;

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getCodeNO() {
		return codeNO;
	}

	public void setCodeNO(String codeNO) {
		this.codeNO = codeNO;
	}

	public int getBillType() {
		return billType;
	}

	public void setBillType(int billType) {
		this.billType = billType;
	}

	public int getUrlNO() {
		return urlNO;
	}

	public void setUrlNO(int urlNO) {
		this.urlNO = urlNO;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getExtData() {
		return extData;
	}

	public void setExtData(String extData) {
		this.extData = extData;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
