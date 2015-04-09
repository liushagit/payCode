package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.vo.SMSInfoVO;

public class URLHelperDTYD {
	
	/** DTYDurl生成 */
	public static String pieceURL(String url, String price, String imei, String imsi,String extData){
		
		String paycode = "";
		if ("2".equals(price)) {
			paycode = "1001";
		} else if ("4".equals(price)) {
			paycode = "1002";
		} else if ("5".equals(price)) {
			paycode = "1003";
		} else if ("6".equals(price)) {
			paycode = "1004";
		} else if ("8".equals(price)){
			paycode = "1005";
		} else if ("10".equals(price)){
			paycode = "1006";
		}
		
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"?pkey=").append(paycode)
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei)
			.append("&callbackData=").append(extData);
		return reqUrl.toString();
	}
	
	public static List<SMSInfoVO> parseJosn(String value, String ogOrderNO, String key ) throws Exception{
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		JSONObject resultObj = JSONObject.parseObject(value);
		
		String resultCode = resultObj.getString("result");
		if (resultCode == null || !resultCode.equals("0")) return null;
		
		String content = new String(resultObj.getString("sms"));
//		String sid = resultObj.getString("sid");
//		String tradeid = new String(Base64.decode(resultObj.getString("tradeNo")));
		String smsport = resultObj.getString("accessNo");
		
        SMSInfoVO info = new SMSInfoVO();
        info.setPort(smsport);
        info.setContent(content);
        info.setProvider("运营商");
        info.setCompany("DTYD");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        
        return infos;
	}

}
