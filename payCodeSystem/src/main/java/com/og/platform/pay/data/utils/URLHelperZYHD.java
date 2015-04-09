/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.vo.SMSInfoVO;

import sun.misc.BASE64Decoder;

/**
 * 北京盛峰
 * 
 * @author ogplayer.com
 * 
 *         2013年11月27日
 */
public class URLHelperZYHD {

	public static final String ACCOUNT = "dysz";

	/** 拼接掌游互动请求URL地址 */
	public static String pieceURL(String url, String account, String productid,
			String cpparam) {
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url).append("?");
		reqUrl.append("account=").append(account).append("&");
		reqUrl.append("productid=").append(productid).append("&");
		reqUrl.append("cpparam=").append(cpparam);
		return reqUrl.toString();
	}
	
	/** 拼接掌游互动请求URL地址 */
	public static String pieceURLMM(String url, String imsi,
			String cpparam, String paycode, String imei, String ip, String province,
			String city,String bsc_lac, String bsc_cid) {
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url).append("?");
		reqUrl.append("imsi=").append(imsi).append("&");
		reqUrl.append("cpparam=").append(cpparam).append("&");
		reqUrl.append("paycode=").append(paycode).append("&");
		reqUrl.append("imei=").append(imei).append("&");
		reqUrl.append("ip=").append(ip).append("&");
		reqUrl.append("province=").append(province).append("&");
		reqUrl.append("city=").append(city).append("&");
		reqUrl.append("bsc_lac=").append(bsc_lac).append("&");
		reqUrl.append("bsc_cid=").append(bsc_cid).append("&");
		return reqUrl.toString();
	}

	public static List<SMSInfoVO> parseZYHD(String content, String ogOrderNO,
			String key,String port) throws Exception {

		JSONObject json = JSONObject.parseObject(content);
		
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		SMSInfoVO info = new SMSInfoVO();
		info.setPort(port);
		info.setContent(json.getString("message"));
		info.setProvider("运营商");
		info.setCompany("北京盛峰");
		info.setOrderNO(ogOrderNO);
		info.setKey(key);
		infos.add(info);
		return infos;
	}
	
	public static List<SMSInfoVO> parseZYHDMM(String content, String ogOrderNO,
			String key) throws Exception {
		
		JSONObject json = JSONObject.parseObject(content);
		String res = json.getString("result");
		if(!"0".equals(res)){
			return null;
		}
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		String con = json.getString("smsmsg");
		byte b[] = new BASE64Decoder().decodeBuffer(con);
		con = new String(b);
		SMSInfoVO info = new SMSInfoVO();
		info.setPort(json.getString("smsport"));
		info.setContent(con);
		info.setProvider("运营商");
		info.setCompany("北京盛峰");
		info.setOrderNO(ogOrderNO);
		info.setKey(key);
		infos.add(info);
		return infos;
	}

}
