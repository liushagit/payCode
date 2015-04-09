/**
 * OGPayServer
 */
package com.og.platform.pay.view.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author ogplayer.com
 *
 * 2013年10月31日
 */
public class XMLTool {
	public static String parse(String content, String key) throws Exception {
		if (content == null) return null;
		
		InputStream in = new ByteArrayInputStream(content.getBytes());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(in);
		return parse(doc, key);
	}
	
	public static String parse(Document doc, String key) throws Exception {
		NodeList keyNodes = doc.getElementsByTagName(key);
		Node keyNode = keyNodes.item(0);
		String keyValue = keyNode.getTextContent();
		return keyValue;
	}
}
