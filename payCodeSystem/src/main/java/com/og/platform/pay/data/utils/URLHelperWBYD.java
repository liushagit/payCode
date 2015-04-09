package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.vo.SMSInfoVO;

public class URLHelperWBYD {
	
	/** 万贝移动url生成 */
	public static String pieceURL(String url, String price, String imei, String imsi,String extData){
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"?price=").append(price).append("00")
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei)
			.append("&cpparam=KUAIL").append(extData)
//			.append("&app_id=300008349575&channel_id=522&operation=102");
			.append("&channelid=3100002");//测试
		
		return reqUrl.toString();
	}
	
	public static List<SMSInfoVO> parseJosn(String value, String ogOrderNO, String key ) throws Exception{
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		JSONObject resultObj = JSONObject.parseObject(value);
		
		String resultCode = resultObj.getString("status");
		if (resultCode == null || !resultCode.equals("0")) return null;
		
		String content = new String(Base64.decode(resultObj.getString("smsmsg")));
//		String sid = resultObj.getString("sid");
//		String tradeid = new String(Base64.decode(resultObj.getString("tradeid")));
		String smsport = resultObj.getString("smsport");
		
        SMSInfoVO info = new SMSInfoVO();
        info.setPort(smsport);
        info.setContent(content);
        info.setProvider("运营商");
        info.setCompany("万贝移动");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        
        return infos;
	}

}
