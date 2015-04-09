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
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.og.platform.pay.common.vo.SMSInfoVO;

/**
 * @author ogplayer.com
 *
 * 2013年11月1日
 */
public class URLHelperWQ {
	
	/** 拼接沃勤请求内容*/
	public static String pieceURL(int price, String extData, String imsi, String imei, String mobile){
		String payId = adapt0(price);
		String priceValue = adapt1(price);
		
		StringBuilder info = new StringBuilder();
		info.append("<request>").append("\n");
		info.append("<body>").append("\n");
		info.append("<Price>" + priceValue + "</Price>").append("\n");
		info.append("<Version>1.0.0</Version>").append("\n");
		info.append("<ExtData>" + extData + "</ExtData>").append("\n");
		info.append("<PutChannelID>2083</PutChannelID>").append("\n");
		info.append("<Appid>117951255</Appid>").append("\n");
		info.append("<Payid>" + payId + "</Payid>").append("\n");
		info.append("<Imsi>" + imsi + "</Imsi>").append("\n");
		info.append("<Imei>" + imei + "</Imei>").append("\n");
		info.append("<UA>" + mobile + "</UA>").append("\n");
		info.append("<iccid></iccid>").append("\n");
		info.append("</body>").append("\n");
		info.append("</request>").append("\n");
		
		return info.toString();
	}
	
	public static List<SMSInfoVO> parse(String value, String ogOrderNO, String key) throws Exception {
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder = factory.newDocumentBuilder();  
        Document doc = builder.parse(new ByteArrayInputStream(value.getBytes()));  
        NodeList nodeList = doc.getElementsByTagName("response");  
        for (int i = 0; i < nodeList.getLength(); i++) {  
            Element  ele = (Element)nodeList.item(i);  
            
            NodeList rets = ele.getElementsByTagName("nRet");
            Node  retNode = rets.item(0);
            String ret = retNode.getTextContent();
            if (ret.equals("1"))return null;
            
            NodeList n1 = ele.getElementsByTagName("SendNumber");
            Node n1Node = n1.item(0);
            String n1Num = n1Node.getTextContent();
            
            NodeList c1 = ele.getElementsByTagName("SendContent");
            Node c1Node = c1.item(0);
            String c1Content = c1Node.getTextContent();
            
            NodeList n2 = ele.getElementsByTagName("SendNumber2");
            Node n2Node = n2.item(0);
            String n2Num = n2Node.getTextContent();
            
            NodeList c2 = ele.getElementsByTagName("SendContent2");
            Node c2Node = c2.item(0);
            String c2Content = c2Node.getTextContent();
            
            SMSInfoVO info1 = new SMSInfoVO();
            info1.setPort(n1Num);
            info1.setContent(c1Content);
            info1.setProvider("移动支付");
            info1.setCompany("沃勤");
            info1.setKey(key);
            info1.setOrderNO(ogOrderNO);
            
            SMSInfoVO info2 = new SMSInfoVO();
            info2.setPort(n2Num);
            info2.setContent(c2Content);
            info2.setProvider("移动支付");
            info2.setCompany("沃勤");
            info2.setKey(key);
            info2.setOrderNO(ogOrderNO);
            
            infos.add(info1);
            infos.add(info2);
        }
        return infos;
	}
	
	
	private static String adapt0(int price) {
		String payId = "11795125501";
		switch (price) {
		case 4:
			payId = "11795125501";
			break;
		case 6:
			payId = "11795125502";
			break;
		case 10:
			payId = "11795125503";
			break;
		default:
			break;
		}
		return payId;
	}
	
	private static String adapt1(int price) {
		String priceStr = "400";
		switch (price) {
		case 4:
			priceStr = "400";
			break;
		case 6:
			priceStr = "600";
			break;
		case 10:
			priceStr = "1000";
			break;
		default:
			break;
		}
		return priceStr;
	}
}
