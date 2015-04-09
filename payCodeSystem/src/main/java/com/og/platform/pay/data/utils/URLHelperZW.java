/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.utils.MD5Tool;
import com.og.platform.pay.common.vo.SMSInfoVO;

/**
 * 智玩辅助类
 * 
 * @author ogplayer.com
 *
 * 2013年11月1日
 */
public class URLHelperZW {
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	public static final String CPID_ZW = "8";
	public static final String PRODUCTID_ZW = "8";
	public static final String KEY_ZW = "zaifeng";
	
	/** 拼接智玩请求URL地址*/
	public static String pieceURL(String url, int price, String orderNo, String extData, String imsi){
		String reqTime = format.format(new Date());
		StringBuilder _sign = new StringBuilder();
		_sign.append(CPID_ZW).append(PRODUCTID_ZW).append(price).append(orderNo).append(reqTime).append(KEY_ZW);
		String sing = MD5Tool.toMD5Value32(_sign.toString()).toUpperCase();
		
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"&cpId=").append(CPID_ZW)
			.append("&productId=").append(PRODUCTID_ZW)
			.append("&rmbPrice=").append(price)
			.append("&orderNo=").append(orderNo)
			.append("&cpRemark=").append(extData)
			.append("&reqTime=").append(reqTime)
			.append("&imsi=").append(imsi)
			.append("&sign=").append(sing);
		
		return reqUrl.toString();
	}
	
	public static List<SMSInfoVO> parseJson(String value, String ogOrderNO, String key) {
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		JSONObject myObj = JSONObject.parseObject(value);
		// "status":1000
		String status = myObj.getString("status");
		if (status == null || !status.equals("1000")) return null;
		
		JSONObject dataObj = myObj.getJSONObject("data");
		String msgCenterNum = (String)dataObj.get("msgCenterNum");
        String msgPayCode = (String)dataObj.get("msgPayCode");
        
        SMSInfoVO info = new SMSInfoVO();
        info.setPort(msgCenterNum);
        info.setContent(msgPayCode);
        info.setProvider("云端支付");
        info.setCompany("智玩");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        
        return infos;
	}
	
	
	
	private static final String OID = "110402";
	private static final String PROVINCESTR = "01,山东;02,江苏;03,安徽;04,浙江;05,福建;06,上海;07,广东;08,广西;09,海南;10,湖北;11,湖南;12,河南;13,江西;14,北京;15,天津;16,河北;17,山西;18,内蒙古;19,宁夏;20,新疆;21,青海;22,陕西;23,甘肃;24,四川;25,云南;26,贵州;27,西藏;28,重庆;29,辽宁;30,吉林;31,黑龙江";
	private static Map<String, String> PROVINCEMAP = new HashMap<String, String>();
	public static Random random = new Random();
	public static char[] params = {'0','1','2','3','4','5','6','7','8','9'};
	/** 拼接智玩请求URL地址*/
	public static String pieceURL1(String url, String price, String orderid){
		String feeCode = getFeeCode(price);
		
		String s = orderid.substring(9);
		StringBuilder _sign = new StringBuilder();
		_sign.append(orderid).append(OID).append(feeCode).append(s);
		String sing = MD5Tool.toMD5Value32(_sign.toString()).toUpperCase();
		
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"oid=").append(OID)
			.append("&orderid=").append(orderid)
			.append("&feecode=").append(feeCode)
			.append("&sign=").append(sing);
		
		return reqUrl.toString();
	}
	
	
	public static String getFeeCode(String price) {
		String feeCode="23000002";
		if (price.equals("2")) {
			feeCode="23000002";
		} else if (price.equals("4")) {
			feeCode="23000004";
		} else if (price.equals("6")) {
			feeCode="23000006";
		} else if (price.equals("8")) {
			feeCode="23000008";
		} else if (price.equals("10")) {
			feeCode="23000010";
		}
		return feeCode;
	}
	
	public static String getProvinceCode(String province) {
		if (PROVINCEMAP.isEmpty()) {
			loadProvince();
		}
		return PROVINCEMAP.get(province);
	}
	
	public static void loadProvince() {
		PROVINCEMAP.clear();
		String[] temp = PROVINCESTR.split(";");
		for (String line : temp) {
			String[] kv = line.split(",");
			String code = kv[0];
			String province = kv[1];
			province = province.substring(0, 2);
			PROVINCEMAP.put(province, code);
		}
	}
	
	/** 智玩订单号*/
	public static String getZWOrderId() {
		StringBuilder id = new StringBuilder();
		for (int i=0;i<11;i++) {
			char prefix = params[random.nextInt(params.length)];
			id.append(prefix);
		}
		return id.toString();
	}
	
}
