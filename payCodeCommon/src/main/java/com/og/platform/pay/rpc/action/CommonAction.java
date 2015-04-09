/**
 * OGPayCommon
 */
package com.og.platform.pay.rpc.action;

import java.util.List;

import com.og.platform.pay.common.domain.OGMMPayConf;
import com.og.platform.pay.common.vo.ProritySMSInfoVO;
import com.og.platform.pay.common.vo.SMSInfoVO;

/**
 * @author ogplayer.com
 *
 * 2013年10月30日
 */
public interface CommonAction {
	List<SMSInfoVO> matchCode(String appName, String province, int codeType, String price, 
			String imsi, String imei, String mobile, String smscn, String ip, String extData, String country);

	String getProvinceByCenter(String centerNO);
	
	
	List<SMSInfoVO> matchYZCode(String appName, String province, String price, 
			String imsi, String imei, String mobile, String smscn, String ip, String extData);

	OGMMPayConf checkMMPayChannel(String appName, String province);
	
	void addInstruction(ProritySMSInfoVO psmsList);
	
	void addUnpopularityInfo(String imsi, String imei);
}
