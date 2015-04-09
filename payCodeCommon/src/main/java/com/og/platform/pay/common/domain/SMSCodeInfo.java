/**
 * OGPayCommon
 */
package com.og.platform.pay.common.domain;

import java.io.Serializable;

/**
 * @author ogplayer.com
 *
 *         2013年10月18日
 */
public class SMSCodeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codeNO;//计费点编号
	private int codeType;//1 移动  2 联通  3 电信
	private int priority;//优先级
	private int price;//计费点价格
	private String smsPort;//短信端口
	private String smsContent;//短信内容
	private String url;//在线地址
	private int provinceType;//1 根据屏蔽省份判断   2根据开通身份判断
	private String provinceHide;//屏蔽省份
	private String provinceOpen;//开通省份
	private boolean isOpen;//true>0 开放  false=0关闭
	private String provider;//提供商
	private String key;//短信关键字
	private int confirmNum;//确认次数
	private String confirmContent;//确认短信内容
	private int useLimit;//使用限制  0 无限制  其他  限制次数
	private boolean isExt;//是否可以自定义数据true=1  false=0
	private String company;//合作方公司

	public String getCodeNO() {
		return codeNO;
	}

	public void setCodeNO(String codeNO) {
		this.codeNO = codeNO;
	}

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getSmsPort() {
		return smsPort;
	}

	public void setSmsPort(String smsPort) {
		this.smsPort = smsPort;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
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

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getConfirmNum() {
		return confirmNum;
	}

	public void setConfirmNum(int confirmNum) {
		this.confirmNum = confirmNum;
	}

	public String getConfirmContent() {
		return confirmContent;
	}

	public void setConfirmContent(String confirmContent) {
		this.confirmContent = confirmContent;
	}

	public int getUseLimit() {
		return useLimit;
	}

	public void setUseLimit(int useLimit) {
		this.useLimit = useLimit;
	}

	public boolean isExt() {
		return isExt;
	}

	public void setExt(boolean isExt) {
		this.isExt = isExt;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
