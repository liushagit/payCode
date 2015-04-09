/**
 * OGPaySystem
 */
package com.og.platform.pay.data.ao;

import com.og.platform.pay.common.domain.MMUnpopularity;
import com.og.platform.pay.common.domain.MsgCenter;
import com.og.platform.pay.common.domain.OGPayDirect;
import com.og.platform.pay.common.vo.ProritySMSInfoVO;
import com.payinfo.net.cached.MemcachedResource;

/**
 * @author ogplayer.com
 *
 * 2013年11月3日
 */
public class CommonAO extends BaseAO {
	public static final String CENTER_PREFIX = "center_";
	public static final String DIRECT_PREFIX = "direct_";
	public static final String MOBILE_UNPOPULARITY = "unpopularity_";
	
	/**
	 * 根据短信中心号获取省份信息
	 */
	public MsgCenter queryCenter(String centerNO) {
		String key = createKey(centerNO);
		MsgCenter info = (MsgCenter) MemcachedResource.get(key);
		if (info == null) info = commonDAO.queryCenter(centerNO);
		
		if (info != null) MemcachedResource.save(key, info);
		return info;
	}
	
	public OGPayDirect queryBill(String appName) {
		String key = createDirectKey(appName);
		OGPayDirect info = (OGPayDirect) MemcachedResource.get(key);
		if (info == null) info = commonDAO.queryBill(appName);
		
		if (info != null) MemcachedResource.save(key, info);
		return info;
	}
	
	//统计获取指令信息
	public void addInstruction(ProritySMSInfoVO psmsList) {
		commonDAO.addInstruction(psmsList);
	}
	
	//添加屏蔽信息
	public void addUnpopularityInfo(String imsi, String imei) {
		String key = createComKey(MOBILE_UNPOPULARITY,imsi+"_"+imei);
		MMUnpopularity info = (MMUnpopularity) MemcachedResource.get(key);
		if (info == null) info = commonDAO.queryUnpopularityInfo(imsi, imei);
		
		if (info != null) return;
		
		commonDAO.addUnpopularityInfo(imsi, imei);
		MMUnpopularity resultInfo = new MMUnpopularity();
		resultInfo.setImei(imei);
		resultInfo.setImsi(imsi);
		MemcachedResource.save(key, resultInfo);
	}
	
	public int addZYHDInfo(String extData){
		return commonDAO.addZYHDInfo(extData);
	}
	
	
	
	
	
	
	private String createKey(String centerNO) {
		StringBuilder key = new StringBuilder();
		key.append(CENTER_PREFIX).append(centerNO);
		return key.toString();
	}
	
	private String createDirectKey(String appName) {
		StringBuilder key = new StringBuilder();
		key.append(DIRECT_PREFIX).append(appName);
		return key.toString();
	}
	
	private String createComKey(String comkey, String temp) {
		StringBuilder key = new StringBuilder();
		key.append(comkey).append(temp);
		return key.toString();
	}
}
