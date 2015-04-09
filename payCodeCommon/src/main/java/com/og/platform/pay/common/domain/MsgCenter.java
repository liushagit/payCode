/**
 * OGPayCommon
 */
package com.og.platform.pay.common.domain;

import java.io.Serializable;

/**
 * @author ogplayer.com
 *
 *         2013年11月3日
 */
public class MsgCenter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String centerNO;
	private int city;
	private String country;

	public String getCenterNO() {
		return centerNO;
	}

	public void setCenterNO(String centerNO) {
		this.centerNO = centerNO;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
