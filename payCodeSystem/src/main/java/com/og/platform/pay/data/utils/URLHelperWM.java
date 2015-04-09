/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.og.platform.pay.common.vo.SMSInfoVO;


/**
 * 微米
 * 
 * @author ogplayer.com
 *
 * 2013年11月27日
 */
public class URLHelperWM {
	
	/** 拼接微米请求URL地址*/
	public static String pieceURL(String url, String imsi, String imei, String wimicode, String extData){
		
		
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"?channel=0001")
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei)
			.append("&wimicode=").append(wimicode)
			.append("&extData=").append(filterExtData(extData));
		
		return reqUrl.toString();
	}
	
	public static String getWimiCode(String fee) {
		String wimicode = "20131120715201";
		if (fee.equals("2")) {
			wimicode = "20131120715202";
		} else if (fee.equals("4")) {
			wimicode = "20131120715203";
		} else if (fee.equals("5")) {
			wimicode = "20131120715204";
		} else if (fee.equals("6")) {
			wimicode = "20131120715205";
		} else if (fee.equals("8")) {
			wimicode = "20131120715206";
		} else if (fee.equals("10")) {
			wimicode = "20131120715207";
		} 

		return wimicode;
	}
	
	
	public static List<SMSInfoVO> parseMM(String content, String ogOrderNO, String key) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(content.getBytes()));
		
		NodeList smsNodes = doc.getElementsByTagName("smscontent");
		Node smsNode = smsNodes.item(0);
		String sms = smsNode.getTextContent();
		
		NodeList portNodes = doc.getElementsByTagName("smsport");
		Node portNode = portNodes.item(0);
		String port = portNode.getTextContent();
		
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		SMSInfoVO info = new SMSInfoVO();
        info.setPort(port);
        info.setContent(sms);
        info.setProvider("运营商");
        info.setCompany("微米");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        return infos;
	}
	
	private static String filterExtData(String ext) {
		String[] params = ext.split("\\@");
		StringBuilder temp = new StringBuilder();
		if (params.length == 3) {
			String appId = params[0];
			String channel = params[1];
			String propId = params[2];
			
			temp.append(appId.substring(0, 1)).append('@')
				.append(channel).append('@').append(propId);
		} else {
			return ext;
		}
		return temp.toString();
	}
}
