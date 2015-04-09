package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.List;

import com.og.platform.pay.common.vo.SMSInfoVO;

public class URLHelperXSZSDT {
	
	/** 新深圳斯达通url生成 */
	public static String pieceURL(String url, String payCode, String imei, String imsi,String extData){
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"?channel=0001")
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei)
			.append("&code=").append(payCode)
			.append("&extData=").append(extData);//测试
		
		return reqUrl.toString();
	}
	
	public static List<SMSInfoVO> parseXml(String value, String ogOrderNO, String key ) throws Exception{
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		
		String smsport = value.substring(value.indexOf("<smsport>"),value.indexOf("</smsport>")).replace("<smsport>", "");
		String content = value.substring(value.indexOf("<smscontent>"),value.indexOf("</smscontent>")).replace("<smscontent>", "");
		
        SMSInfoVO info = new SMSInfoVO();
        info.setPort(smsport);
        info.setContent(content);
        info.setProvider("移动支付");
        info.setCompany("新深圳斯达通");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        
        return infos;
	}
	
	
	

}
