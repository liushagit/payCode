/**
 * OGPayCommon
 */
package com.og.platform.pay.common.vo;

import java.io.Serializable;

/**
 * @author ogplayer.com
 *
 *         2013年10月29日
 */
public class SMSInfoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String port;
	private String content;
	private String provider;
	private String confirm;
	private String company;
	private String orderNO;
	private String key;

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getOrderNO() {
		return orderNO;
	}

	public void setOrderNO(String orderNO) {
		this.orderNO = orderNO;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
