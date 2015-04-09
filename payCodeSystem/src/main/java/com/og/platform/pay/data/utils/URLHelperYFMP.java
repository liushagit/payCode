package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.vo.SMSInfoVO;

public class URLHelperYFMP {
	
	/** 广州游发url生成 */
	public static String pieceURL(String url, String payCode, String imei, String imsi,String sid){
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"?paycode=").append(payCode)
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei)
			.append("&sid=").append(sid)
//			.append("&app_id=300008349575&channel_id=522&operation=102");
			.append("&app_id=300008872128&channel_id=559&operation=102");//测试
		
		return reqUrl.toString();
	}
	
	public static List<SMSInfoVO> parseJosn(String value, String ogOrderNO, String key ) throws Exception{
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		JSONObject resultObj = JSONObject.parseObject(value);
		
		String resultCode = resultObj.getString("result");
		if (resultCode == null || !resultCode.equals("0")) return null;
		
		String content = new String(Base64.decode(resultObj.getString("smsmsg")));
//		String sid = resultObj.getString("sid");
//		String tradeid = new String(Base64.decode(resultObj.getString("tradeid")));
		String smsport = resultObj.getString("smsport");
		
        SMSInfoVO info = new SMSInfoVO();
        info.setPort(smsport);
        info.setContent(content);
        info.setProvider("泡泡龙");
        info.setCompany("YFMP");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        
        return infos;
	}

}
