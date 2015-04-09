/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.utils.CommonTools;
import com.og.platform.pay.common.vo.SMSInfoVO;
import com.payinfo.net.log.LoggerFactory;


/**
 * @author ogplayer.com
 *
 * 2013年12月3日
 */
public class URLHelperTAX {
	private static Logger logger = LoggerFactory.getLogger(URLHelperTAX.class);
	
	/** 拼接特安讯请求URL地址*/
	public static String pieceURL(String url, String price, String imei, String imsi, String extData){
		int priceInt = CommonTools.convertPrice(price) * 100;
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"?price=").append(priceInt)
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei)
			.append("&cpparam=").append(extData)
			.append("&phone=13800138000");
		
		return reqUrl.toString();
	}
	
	
	/** 拼接特安讯请求URL地址*/
	public static String pieceURLUnicom(String url, String price, String imei, String imsi, String extData){
		StringBuilder reqUrl = new StringBuilder();
		try {
			reqUrl.append(url+"?TheSubject=").append(URLEncoder.encode("植物大战僵尸", "utf-8"))
				.append("&TheFee=").append(price)
				.append("&apiKey=").append("qd802")
				.append("&apipwd=").append("qd802")
				.append("&cpparam=").append(extData)
				.append("&appName=").append(URLEncoder.encode("植物大战僵尸", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		return reqUrl.toString();
	}
	
	/** 拼接特安讯请求URL地址*/
	public static String pieceURLTelecom(String url, String price, String imei, String imsi, String extData){
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url)
			.append("?price=").append(price).append("00")
			.append("&imei=").append(imei)
			.append("&imsi=").append(imsi)
			.append("&cpparam=").append(extData);
		
		return reqUrl.toString();
	}
	
	public static String getPriceId(String price) {
		String priceId = "YMQLS400";
		if (price.equals("4")) {
			priceId = "YMQLS400";
		} else if (price.equals("6")) {
			priceId = "YMQLS600";
		} else if (price.equals("8")) {
			priceId = "YMQLS800";
		} else if (price.equals("0.01")) {
			priceId = "YMQLS1";
		} else if (price.equals("10")) {
			priceId = "GGEL1000";
		} 
		return priceId;
		
		
		
//		String priceId = "EYA200";
//		if (price.equals("2")) {
//			priceId = "EYA200";
//		} else if (price.equals("4")) {
//			priceId = "EYA400";
//		} else if (price.equals("5")) {
//			priceId = "EYA500";
//		} else if (price.equals("6")) {
//			priceId = "EYA600";
//		} else if (price.equals("8")) {
//			priceId = "EYA800";
//		} else if (price.equals("10")) {
//			priceId = "1EYA1000";
//		} else if (price.equals("0.01")) {
//			priceId = "EYA1";
//		} 
//		return priceId;
	}
	
	public static SMSInfoVO parseMM(String content, String ogOrderNO, String key) throws Exception {
		SMSInfoVO vo = new SMSInfoVO();
		JSONObject myObj = JSONObject.parseObject(content);
		String status = myObj.getString("status");
		if (!status.equals("0")) return null;
		
		String sms = myObj.getString("content");
		String spnumber = myObj.getString("num");
		
		vo.setPort(spnumber);
		vo.setContent(sms);
		vo.setProvider("运营商");
		vo.setCompany("特安讯");
		vo.setOrderNO(ogOrderNO);
		vo.setKey(key);
		return vo;
	}
	
	
	public static SMSInfoVO parseJson(String content, String ogOrderNO, String key) {
		SMSInfoVO vo = new SMSInfoVO();
		JSONObject myObj = JSONObject.parseObject(content);
		// "status":1000
		String sms = myObj.getString("sms");
		String spnumber = myObj.getString("spnumber");
		
		vo.setPort(spnumber);
		vo.setContent(sms);
		vo.setProvider("运营商");
		vo.setCompany("特安讯");
		vo.setOrderNO(ogOrderNO);
		vo.setKey(key);
		return vo;
	}
	
	public static SMSInfoVO parseTAXTele(String content, String ogOrderNO, String key) {
		SMSInfoVO vo = new SMSInfoVO();
		String resultCode = null;
		String sms = null;
		String spnumber = null;
		
		String newContent = content.replace("\":\"", "").replace("{\"", "").replace("\"}", "");
		String[] myObj = newContent.split("\",\"");
		for (String s : myObj) {
			if (s.contains("status")) {
				resultCode = s.replace("status", "");
			} else if (s.contains("content")) {
				sms = s.replace("content", "");
			} else if (s.contains("num")) {
				spnumber = s.replace("num", "");
			}
		}
		if (resultCode == null || !resultCode.equals("0")) return null;
		
		
		vo.setPort(spnumber);
		vo.setContent(sms);
		vo.setProvider("运营商");
		vo.setCompany("特安讯");
		vo.setOrderNO(ogOrderNO);
		vo.setKey(key);
		return vo;
	}
}
