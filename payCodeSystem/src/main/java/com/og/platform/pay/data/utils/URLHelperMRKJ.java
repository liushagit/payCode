/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.vo.SMSInfoVO;

/**
 * @author ogplayer.com
 *
 * 2013年12月5日
 */
public class URLHelperMRKJ {
	/** 拼接明日空间请求URL地址*/
	public static String pieceURL(String url, String price, String imei, String imsi){
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"&price=").append(price)
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei)
			.append("&serviceName=mc")
			.append("&phone=");
		
		return reqUrl.toString();
	}
	
	
	public static List<SMSInfoVO> parseJosn(String value, String ogOrderNO, String key){
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		JSONObject myObj = JSONObject.parseObject(value);
		String code = myObj.getString("code");
		if (code == null || !code.equals("success")) return null;
		
		String msgport = (String)myObj.get("port");
        String msgcontent = (String)myObj.get("sms");
        
        SMSInfoVO info = new SMSInfoVO();
        info.setPort(msgport);
        info.setContent(msgcontent);
        info.setProvider("动漫");
        info.setCompany("明日空间");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        
        return infos;
	}
}
