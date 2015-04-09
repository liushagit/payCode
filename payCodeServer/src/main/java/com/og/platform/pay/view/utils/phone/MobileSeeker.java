/**
 * OGPayServer
 */
package com.og.platform.pay.view.utils.phone;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.utils.CommonTools;
import com.og.platform.pay.common.utils.HttpTool;
import com.og.platform.pay.common.vo.AreaVO;
import com.og.platform.pay.view.utils.XMLTool;
import com.payinfo.net.log.LoggerFactory;

/**
 * @author ogplayer.com
 *
 * 2013年10月31日
 */
public class MobileSeeker {
	private static Logger logger = LoggerFactory.getLogger(MobileSeeker.class);
	public static final String MOBILE_TAOBAO = "http://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=";
	public static final String MOBILE_TENPAY = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=";
	
	public static AreaVO queryProvince(String mobile) {
		AreaVO area = null;
		// 第一步:调用淘宝接口
		String url = convertURL(MOBILE_TAOBAO, mobile);
		String result = HttpTool.sendHttp(url, "", "gbk");
		if (result != null) area = parseTaobao(result);

		if (area != null) return area;
		
		url = convertURL(MOBILE_TENPAY, mobile);
		result = HttpTool.simpleSendHttp(url, "gb2312");
		if (result != null) area = parseTenPay(result);
		return area;
	}
	
	private static AreaVO parseTaobao(String content) {
		AreaVO area = null;
		try {
			int start = content.indexOf('=');
			String jsonValue = content.substring(start+1);
			JSONObject jsonObj = JSONObject.parseObject(jsonValue);
			String province = jsonObj.getString("province");
			String operator = jsonObj.getString("catName");
			int codeType = CommonTools.locateOperatorCode(operator);
			
			if (province!=null) {
				area = new AreaVO();
				area.setProvince(province);
				area.setCodeType(codeType);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return area;
	}
	
	private static AreaVO parseTenPay(String content) {
		AreaVO area = null;
		try {
			InputStream in = new ByteArrayInputStream(content.getBytes("gb2312"));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(in);
			String province = XMLTool.parse(doc, "province");
			String operator = XMLTool.parse(doc, "supplier");
			int codeType = CommonTools.locateOperatorCode(operator);
			
			if (province!=null) {
				area = new AreaVO();
				area.setProvince(province);
				area.setCodeType(codeType);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return area;
	}
	
	private static String convertURL(String url, String mobile) {
		StringBuilder sb = new StringBuilder();
		return sb.append(url).append(mobile).toString();
	}
	
	public static void main(String[] args) {
		AreaVO area = queryProvince("13058032471");
		System.out.println(area.getProvince() + ":" + area.getCodeType());
	}
}
