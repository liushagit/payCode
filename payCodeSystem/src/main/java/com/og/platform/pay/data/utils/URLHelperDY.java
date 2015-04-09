/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.io.ByteArrayInputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.og.platform.pay.common.vo.SMSInfoVO;

/**
 * @author ogplayer.com
 *
 * 2013年12月26日
 */
public class URLHelperDY {
	/** 拼接电盈请求URL地址*/
	public static String pieceContent(String imsi, String imei, String gameId, String extData){
		StringBuilder content = new StringBuilder();
		content.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><request>");
		content.append("<gameId>").append(gameId).append("</gameId>");
		content.append("<imei>").append(imei).append("</imei>");
		content.append("<imsi>").append(imsi).append("</imsi>");
		content.append("<extData>").append(extData).append("</extData>");
		content.append("</request>");
		
		return content.toString();
	}
	
	/** 联通*/
	public static String getGameId(String price) {
		String gameId = "11047";
		if (price.equals("2")) {
			gameId = "11047";
		} else if (price.equals("4")) {
			gameId = "11048";
		} else if (price.equals("6")) {
			gameId = "11049";
		} else if (price.equals("8")) {
			gameId = "11050";
		} else if (price.equals("10")) {
			gameId = "11051";
		} 

		return gameId;
	}
	
	/** 电信*/
	public static String getTelnetGameId(String price) {
		String gameId = "11158";
		if (price.equals("4")) {
			gameId = "11158";
		} else if (price.equals("6")) {
			gameId = "11159";
		} else if (price.equals("10")) {
			gameId = "11160";
		} 

		return gameId;
	}
	
	@SuppressWarnings("deprecation")
	public static List<SMSInfoVO> parse(String value, String ogOrderNO, String key) throws Exception {
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = factory.newDocumentBuilder();  
        Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));  
        NodeList nodeList = doc.getElementsByTagName("response");  
        for (int i = 0; i < nodeList.getLength(); i++) {  
            Element  ele = (Element)nodeList.item(i);  
            
            NodeList states = ele.getElementsByTagName("state");
            Node  stateNode = states.item(0);
            String state = stateNode.getTextContent();
            if (!state.equals("0"))return null;
            
            NodeList n1 = ele.getElementsByTagName("spNumber");
            Node n1Node = n1.item(0);
            String n1Num = n1Node.getTextContent();
            
            NodeList c1 = ele.getElementsByTagName("moContent");
            Node c1Node = c1.item(0);
            String c1Content = c1Node.getTextContent();
            c1Content = URLDecoder.decode(c1Content);
            
//            NodeList linkIds = ele.getElementsByTagName("linkId");
//            Node linkIdNode = linkIds.item(0);
//            String linkId = linkIdNode.getTextContent();
            
            SMSInfoVO info1 = new SMSInfoVO();
            info1.setPort(n1Num);
            info1.setContent(c1Content);
            info1.setProvider("运营商支付");
            info1.setCompany("电盈");
            info1.setKey(key);
            info1.setOrderNO(ogOrderNO);
            
            infos.add(info1);
        }
        return infos;
	}
}
