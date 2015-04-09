/**
 * OGPayCommon
 */
package com.og.platform.pay.common.vo;

import java.io.Serializable;
import java.util.List;

import com.og.platform.pay.common.vo.SMSInfoVO;

/**
 * @author ogplayer.com
 *
 *         2013年11月6日
 */
public class ProritySMSInfoVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<SMSInfoVO> smsList;
	private int priority;
	private String billId;//指令计费id
	private int codeType;//1 移动  2 联通  3 电信
	private int price;//计费价格

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public List<SMSInfoVO> getSmsList() {
		return smsList;
	}

	public void setSmsList(List<SMSInfoVO> smsList) {
		this.smsList = smsList;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
