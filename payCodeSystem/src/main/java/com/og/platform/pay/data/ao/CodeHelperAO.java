/**
 * OGPaySystem
 */
package com.og.platform.pay.data.ao;

import java.util.ArrayList;
import java.util.List;

import com.og.platform.pay.common.domain.SMSCodeInfo;
import com.og.platform.pay.common.utils.ConstantDefine;
import com.payinfo.net.cached.MemcachedResource;

/**
 * @author ogplayer.com
 *
 * 2013年10月25日
 */
public class CodeHelperAO extends BaseAO {
	public static final String CODE_TYPE_PREFIX = "code_";
	public static final String CODE_TYPE_DIRPREFIX = "directcode_";
	
	public List<SMSCodeInfo> getCodes(int codeType, String province, String country) {
		String index = createIndexKey(codeType, province);
		List<SMSCodeInfo> codes = (List<SMSCodeInfo>) MemcachedResource.get(index);
		if (codes != null) return codes;
		
		codes = codeHeplerDAO.getAllSMSCodes(codeType);
		List<SMSCodeInfo> tempList = filterProvince(codes, province, country);//过滤屏蔽省份
		
		if (!tempList.isEmpty()) MemcachedResource.save(index, tempList);
		return tempList;
	}
	
	public List<SMSCodeInfo> getDirectCodes(int codeType, String province, String billId) {
		String index = createDirectIndexKey(codeType, province);
		List<SMSCodeInfo> codes = (List<SMSCodeInfo>) MemcachedResource.get(index);
		if (codes != null) return codes;
		
		codes = codeHeplerDAO.getDirectSMSCodes(codeType,billId);
		List<SMSCodeInfo> tempList = filterProvince(codes, province, null);//过滤屏蔽省份
		
		if (!tempList.isEmpty()) MemcachedResource.save(index, tempList);
		return tempList;
		
	}
	
	public SMSCodeInfo getCodeInfo(String codeNO) {
		return codeHeplerDAO.getCodeInfo(codeNO);
	}
	
	private String createIndexKey(int codeType, String province) {
		StringBuilder index = new StringBuilder();
		index.append(CODE_TYPE_PREFIX).append(codeType).append("_").append(province);
		return index.toString();
	}
	
	private String createDirectIndexKey(int codeType, String province) {
		StringBuilder index = new StringBuilder();
		index.append(CODE_TYPE_DIRPREFIX).append(codeType).append("_").append(province);
		return index.toString();
	}
	
	//过滤屏蔽省份
	private List<SMSCodeInfo> filterProvince(List<SMSCodeInfo> codes, String province, String country) {
		List<SMSCodeInfo> tempList = new ArrayList<SMSCodeInfo>();
		for (SMSCodeInfo info : codes) {
			boolean isOK = false;
			if ("灵光互动".contains(info.getCompany()) && country != null) {
				if (country.contains("石家庄") || country.contains("郑州") || 
						country.contains("太原") || country.contains("成都")) {
					province = country;
				}
			}
			
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
		
		return tempList;
	}
}
