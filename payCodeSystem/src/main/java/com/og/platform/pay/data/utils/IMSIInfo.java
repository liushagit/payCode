/**
 * MMSDKServer
 */
package com.og.platform.pay.data.utils;

import java.io.Serializable;

/**
 * @author ogplayer.com
 *
 *         2013年11月27日
 */
public class IMSIInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String imsi;
	public String imei;

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
