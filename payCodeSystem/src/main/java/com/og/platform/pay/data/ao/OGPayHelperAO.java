/**
 * OGPaySystem
 */
package com.og.platform.pay.data.ao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.og.platform.pay.common.domain.OGMMPayConf;
import com.og.platform.pay.common.domain.OGPayDirect;
import com.og.platform.pay.common.domain.OGPayInfo;
import com.og.platform.pay.common.utils.ConstantDefine;
import com.og.platform.pay.data.utils.PriorityComparator;
import com.payinfo.net.cached.MemcachedResource;
import com.payinfo.net.log.LoggerFactory;

/**
 * @author ogplayer.com
 *
 *         2013年10月31日
 */
public class OGPayHelperAO extends BaseAO {
	private static Logger logger = LoggerFactory.getLogger(OGPayHelperAO.class);
	public static final String OGPAY_KEY = "oginfo_";
	public static final String OGMMPAY_KEY = "ogmminfo_";
	public static final Random random = new Random();
	
	/** 从在线通道或者混合通道中筛选*/
	public List<OGPayInfo> pieceBill(int codeType, String province, String price) {
		String index = createIndexKey(codeType, province);
		List<OGPayInfo> infos = (List<OGPayInfo>) MemcachedResource.get(index);
		if (infos == null) {
			infos = matchBill2(codeType, province);
			if (infos.size() > 0) {
				// 优先级排序
				Collections.sort(infos, new PriorityComparator<OGPayInfo>());
				MemcachedResource.save(index, infos);
			}
		}

		List<OGPayInfo> temp = new ArrayList<OGPayInfo>();
		for (OGPayInfo info : infos) {
			if (Integer.valueOf(price) == Integer.valueOf(info.getPrice())) {
				temp.add(info);
			}
		}
		
		if (temp.isEmpty()) return null;
		return temp;
	}
	
	/** 根据运营商和省份匹配*/
	private List<OGPayInfo> matchBill2(int codeType, String province) {
		List<OGPayInfo> infos = getAllBill(codeType);
		logger.info(codeType+"|size:" + infos.size());
		List<OGPayInfo> tempList = new ArrayList<OGPayInfo>();
		for (OGPayInfo info : infos) {
			boolean isOK = false;
			if (info.getProvinceType() == ConstantDefine.USE_TYPE_HIDE) {
				isOK = info.getProvinceHide().contains(province)?false:true;
			} else if (info.getProvinceType() == ConstantDefine.USE_TYPE_OPEN) {
				isOK = info.getProvinceOpen().contains(province)?true:false;
			} else {
				isOK = true;
			}
			
			if (!isOK) continue;
			tempList.add(info);
		}
		logger.info(codeType+"|tempList:" + tempList.size());
		return tempList;
	}
	
	/** 根据运营商匹配*/
	public List<OGPayInfo> getAllBill(int codeType) {
		return ogpayDAO.getAllBillInfo(codeType);
	}
	
	/** 根据游戏APPNAME匹配通道*/
	public List<OGPayInfo> queryBill(OGPayDirect direct,int codeType, String province) {
		String bill = direct.getBillId();
		if (bill == null)
			return null;
		String[] bills = bill.split(",");

		List<OGPayInfo> billList = new ArrayList<OGPayInfo>();
		for (String billId : bills) {
			OGPayInfo info = getUrlPayInfo(billId,codeType);
			if (info == null)
				continue;
			
			boolean isOK = false;
			if (info.getProvinceType() == ConstantDefine.USE_TYPE_HIDE) {
				isOK = info.getProvinceHide().contains(province)?false:true;
			} else if (info.getProvinceType() == ConstantDefine.USE_TYPE_OPEN) {
				isOK = info.getProvinceOpen().contains(province)?true:false;
			} else {
				isOK = true;
			}
			
			if (isOK)
				billList.add(info);
		}
		return billList;
	}
	
	public OGPayInfo getUrlPayInfo(String billId, int codeType) {
		String key = createIndexKey(codeType,billId);
		OGPayInfo info = (OGPayInfo) MemcachedResource.get(key);
		if (info==null) info= ogpayDAO.getUrlPayInfo(billId,codeType);
		
		if (info!=null) MemcachedResource.save(key, info);
		return info;
	}

	public OGPayInfo getBillInfo(String billId) {
		String key = createIndexKey(billId);
		OGPayInfo info = (OGPayInfo) MemcachedResource.get(key);
		if (info==null) info= ogpayDAO.getPayInfo(billId);
		
		if (info!=null) MemcachedResource.save(key, info);
		return info;
	}
	
	public OGMMPayConf checkMMPayChannel(String appName) {
		String key = createIndexKey(OGMMPAY_KEY+appName);
		OGMMPayConf info = (OGMMPayConf) MemcachedResource.get(key);
		
		if (info==null) info= ogpayDAO.checkMMPayChannel(appName);
		
		if (info!=null) MemcachedResource.save(key, info);
		return info;
	}
	
	private String createIndexKey(int codeType, String province) {
		StringBuilder index = new StringBuilder();
		index.append(OGPAY_KEY).append(codeType).append("_").append(province);
		return index.toString();
	}
	
	private String createIndexKey(String billId) {
		StringBuilder index = new StringBuilder();
		index.append(OGPAY_KEY).append(billId);
		return index.toString();
	}
}
