/**
 * OGPayCommon
 */
package com.og.platform.pay.common.vo;

import java.io.Serializable;

/**
 * @author ogplayer.com
 *
 *         2013年10月31日
 */
public class AreaVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String province;
	private int codeType;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}
}
