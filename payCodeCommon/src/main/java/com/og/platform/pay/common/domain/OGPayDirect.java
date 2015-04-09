/**
 * OGPayCommon
 */
package com.og.platform.pay.common.domain;

import java.io.Serializable;

/**
 * @author ogplayer.com
 *
 *         2013年10月31日
 */
public class OGPayDirect implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private String appName;
	private String billId;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}
}
