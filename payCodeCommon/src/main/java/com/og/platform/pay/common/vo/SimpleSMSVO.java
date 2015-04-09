/**
 * OGPayCommon
 */
package com.og.platform.pay.common.vo;

import java.io.Serializable;

/**
 * @author ogplayer.com
 *
 *         2013年11月1日
 */
public class SimpleSMSVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String port;
	private String content;
	private String provider;
	private String orderNo;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
