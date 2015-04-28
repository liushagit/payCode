/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.vo.SMSInfoVO;
import com.payinfo.net.log.LoggerFactory;

import sun.misc.BASE64Decoder;

/**
 * 逐梦
 * 
 * @author ogplayer.com
 * 
 *         2013年11月27日
 */
public class URLHelperZM {

	private static Logger logger = LoggerFactory.getLogger(URLHelperZM.class);
	/** 拼接逐梦请求URL地址 */
	public static String pieceURL(String url ,String imsi,String imei,String fee,
			String ext) {
		
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url).append("?");
		reqUrl.append("cpid=").append("5dc01d10").append("&");
		reqUrl.append("imsi=").append(imsi).append("&");
		reqUrl.append("imei=").append(imei).append("&");
		reqUrl.append("fee=").append(fee).append("&");
		reqUrl.append("ext=").append(ext);
		return reqUrl.toString();
	}
	
	
	public static List<SMSInfoVO> parseZM(String content, String ogOrderNO,
			String key) throws Exception {
		
//		num	String	否	发送短信的号码
//		content	String	否	发送短信内容
//		error	String	否	成功获取短信为0，失败请见错误代码

		JSONObject json = JSONObject.parseObject(content);
		String res = json.getString("error");
		if(!"0".equals(res)){
			logger.error("parseZM error:" + res);
			return null;
		}
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		String con = json.getString("content");
		SMSInfoVO info = new SMSInfoVO();
		info.setPort(json.getString("num"));
		info.setContent(con);
		info.setProvider("运营商");
		info.setCompany("逐梦");
		info.setOrderNO(ogOrderNO);
		info.setKey(key);
		infos.add(info);
		return infos;
	}

}
