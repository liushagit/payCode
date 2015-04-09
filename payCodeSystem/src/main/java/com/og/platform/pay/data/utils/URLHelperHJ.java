/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.og.platform.pay.common.vo.SMSInfoVO;
import com.payinfo.net.log.LoggerFactory;


/**
 * @author ogplayer.com
 *
 * 2013年11月1日
 */
public class URLHelperHJ {
	private static Logger logger = LoggerFactory.getLogger(URLHelperHJ.class);
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static AtomicInteger atid = new AtomicInteger(1);
	
	/** 拼接恒巨请求内容*/
	public static String pieceURL(String imsi, String imei, String smscn, 
			String ip,String ext, int price, String mobile){
		mobile = mobile==null?"":mobile;
		smscn = smscn==null?"":smscn;
		
		StringBuilder msg = new StringBuilder();
		msg.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		msg.append("<ROOT>")
			.append("<IMSI>"+imsi+"</IMSI>")
			.append("<IMEI>"+imei+"</IMEI>")
			.append("<SMSCN>"+smscn+"</SMSCN>")
			.append("<MOBILE>"+mobile+"</MOBILE>")
			.append("<CID>71048</CID>")
			.append("<FEEVALUE>"+price+"</FEEVALUE>")
			.append("<VERIFYCODE>szog123</VERIFYCODE>")
			.append("<IP>"+ip+"</IP>")//58.60.168.203
			.append("<EXTPARAM>"+ext+"</EXTPARAM>")
			.append("<UID>"+getUID(imsi)+"</UID>")
			.append("</ROOT>");
		return msg.toString();
	}
	
	public static List<SMSInfoVO> parseMM(String value, String ogOrderNO, String key) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));
		
		NodeList resultNodes = doc.getElementsByTagName("RESULTCODE");
		Node resultNode = resultNodes.item(0);
		String result = resultNode.getTextContent();
		
		if (!result.equals("200")) return null;
		
		NodeList linkidNodes = doc.getElementsByTagName("LINKID");
		Node linkidNode = linkidNodes.item(0);
		String linkid = linkidNode.getTextContent();
		
		NodeList smsNodes = doc.getElementsByTagName("SPMSG");
		Node smsNode = smsNodes.item(0);
		String sms = smsNode.getTextContent();
		
		NodeList portNodes = doc.getElementsByTagName("SPCODE");
		Node portNode = portNodes.item(0);
		String port = portNode.getTextContent();
		
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		SMSInfoVO info = new SMSInfoVO();
        info.setPort(port);
        info.setContent(sms);
        info.setProvider("运营商");
        info.setCompany("恒巨");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        return infos;
	}
	
	public static List<SMSInfoVO> parseDX(String param, String ogOrderNO, String key) throws Exception {
		if (param == null) return null;
		int start = param.indexOf("SPMSG");
		int end = param.indexOf("/SPMSG");
		String sms = param.substring(start+6, end-1);
		System.out.println(sms);
		param = param.replace(sms, "0");
		System.out.println(param);
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(param.getBytes("utf-8")));
		
		NodeList resultNodes = doc.getElementsByTagName("RESULTCODE");
		Node resultNode = resultNodes.item(0);
		String result = resultNode.getTextContent();
		
		if (!result.equals("200")) return null;
		
		NodeList linkidNodes = doc.getElementsByTagName("LINKID");
		Node linkidNode = linkidNodes.item(0);
		String linkid = linkidNode.getTextContent();
		
		NodeList portNodes = doc.getElementsByTagName("SPCODE");
		Node portNode = portNodes.item(0);
		String port = portNode.getTextContent();
		
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		SMSInfoVO info = new SMSInfoVO();
        info.setPort(port);
        info.setContent(sms);
        info.setProvider("运营商");
        info.setCompany("恒巨");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        return infos;
	}
	
	private static String getUID(String imsi) {
		try {
			Date date = format.parse("1970-01-01 00:00:00");
			int timeSpace = (int)(Calendar.getInstance().getTimeInMillis() - date.getTime())/1000;
			
			StringBuilder uid = new StringBuilder();
			uid.append(imsi).append(Math.abs(timeSpace)).append(getStrValue());
			return uid.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return null;
	}

	private static String getStrValue() {
		if (atid.get() == 9999) {
			atid.set(1);
		}
		int param = atid.getAndIncrement();
		
		String value = String.valueOf(param);
		StringBuilder msg = new StringBuilder();
		for (int i=0; i<(4-value.length());i++){
			msg.append("0");
		}
		msg.append(param);
		return msg.toString();
	}
	
	public static String filterExtData(String ext) {
		String[] params = ext.split("\\@");
		StringBuilder temp = new StringBuilder();
		if (params.length == 3) {
			String appId = params[0];
			String channel = params[1];
			String propId = params[2];
			
			temp.append(appId.substring(0, 2)).append('@')
				.append(channel).append('@').append(propId);
		}
		return temp.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(filterExtData("xmxx20131017@ogxx0005@001"));
		System.out.println(filterExtData("xmxx20131017@ogxx0005@001").length());
	}
}
