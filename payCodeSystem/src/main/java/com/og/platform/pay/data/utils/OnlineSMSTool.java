/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.List;

import com.og.platform.pay.common.utils.CommonTools;
import com.og.platform.pay.common.utils.ConstantDefine;
import com.og.platform.pay.common.utils.DateUtil;
import com.og.platform.pay.common.utils.HttpTool;
import com.og.platform.pay.common.vo.SMSInfoVO;
import com.payinfo.net.log.LoggerFactory;

/**
 * @author ogplayer.com
 *
 * 2013年11月1日
 */
public class OnlineSMSTool {
	private static org.apache.log4j.Logger logger = LoggerFactory.getLogger(OnlineSMSTool.class);
	
	/***
	 * URL_NO : 1
	 * 智玩
	 */
	public static List<SMSInfoVO> getOnlineSMS1(String url, String price, String extData, String ogOrderNO, String key, String imsi) {
		List<SMSInfoVO> sms = null;
		try {
			int priceInt = convertPrice(price);
			String reqUrl = URLHelperZW.pieceURL(url, priceInt, ogOrderNO, extData, imsi);
			String result = HttpTool.sendHttp(reqUrl, "");
			sms = URLHelperZW.parseJson(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	//======================================================================================
	/***
	 * URL_NO : 2
	 * 沃勤
	 */
	public static List<SMSInfoVO> getOnlineSMS2(String url, String price, String extData, String imsi, 
			String imei, String mobile, String ip, String ogOrderNO, String key) {
		List<SMSInfoVO> sms = null;
		try {
			int priceInt = convertPrice(price);
			String reqValue = URLHelperWQ.pieceURL(priceInt, extData, imsi, imei, mobile);
			String result = HttpTool.sendHttp1(url, reqValue, ip);
			sms = URLHelperWQ.parse(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	//======================================================================================
	/***
	 * URL_NO : 3
	 * 恒巨MM
	 */
	public static List<SMSInfoVO> getOnlineSMS3(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String mobile, String smscenter, String ip, String key) {
		List<SMSInfoVO> sms = null;
		try {
			int priceInt = convertPrice(price) * 100;
			String reqValue = URLHelperHJ.pieceURL(imsi, imei, smscenter, ip, ext, priceInt, mobile);
			String result = HttpTool.sendHttp(url, reqValue);
			sms = URLHelperHJ.parseMM(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	/***
	 * URL_NO : 4
	 * 恒巨天翼
	 */
	public static List<SMSInfoVO> getOnlineSMS4(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String mobile, String smscenter, String ip, String key) {
		List<SMSInfoVO> sms = null;
		try {
			int priceInt = convertPrice(price) * 100;
			String reqValue = URLHelperHJ.pieceURL(imsi, imei, smscenter, ip, ext, priceInt, mobile);
			String result = HttpTool.sendHttp(url, reqValue);
			sms = URLHelperHJ.parseDX(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 5
	 * 恒巨沃商城
	 */
	public static List<SMSInfoVO> getOnlineSMS5(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String mobile, String smscenter, String ip, String key) {
		List<SMSInfoVO> sms = null;
		try {
			int priceInt = convertPrice(price) * 100;
			String reqValue = URLHelperHJ.pieceURL(imsi, imei, smscenter, ip, ext, priceInt, mobile);
			String result = HttpTool.sendHttp(url, reqValue);
			sms = URLHelperHJ.parseMM(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 6
	 * 微米
	 */
	public static List<SMSInfoVO> getOnlineSMS6(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		try {
			String wimicode = URLHelperWM.getWimiCode(price);
			String reqUrl = URLHelperWM.pieceURL(url, imsi, imei, wimicode, ext);
			String result = HttpTool.sendHttp(reqUrl, "");
			sms = URLHelperWM.parseMM(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 7
	 * 锐之道
	 */
	public static List<SMSInfoVO> getOnlineSMS7(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		try {
			String reqUrl = URLHelperRZD.pieceURL(url, price, imei, imsi, ext);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			sms = new ArrayList<SMSInfoVO>();
			SMSInfoVO info = new SMSInfoVO();
	        info.setPort("1065842410");
	        info.setContent(result);
	        info.setProvider("运营商");
	        info.setCompany("锐之道");
	        info.setOrderNO(ogOrderNO);
	        info.setKey(key);
	        sms.add(info);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	/***
	 * URL_NO : 8
	 * 特安讯
	 */
	public static List<SMSInfoVO> getOnlineSMS8(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key, int codeType) {
		List<SMSInfoVO> sms = null;
		try {
			if (codeType == ConstantDefine.CODE_TYPE_MOBILE) {
				String reqUrl = URLHelperTAX.pieceURL(url, price, imei, imsi, ext);
				String result = HttpTool.sendHttp(reqUrl, "");
				if (result.equals("getsms_error")) return sms;
				SMSInfoVO vo = URLHelperTAX.parseMM(result, ogOrderNO, key);
				
				sms = new ArrayList<SMSInfoVO>();
		        sms.add(vo);
			} else if (codeType == ConstantDefine.CODE_TYPE_UNICOM) {
				String reqUrl = URLHelperTAX.pieceURLUnicom(url, price, imei, imsi, ext);
				String result = HttpTool.sendHttp(reqUrl, "");
				if (result.equals("getsms_error")) return sms;
				SMSInfoVO vo = URLHelperTAX.parseJson(result, ogOrderNO, key);
				
				sms = new ArrayList<SMSInfoVO>();
		        sms.add(vo);
			} else if (codeType == ConstantDefine.CODE_TYPE_TELNET) {
				String reqUrl = URLHelperTAX.pieceURLTelecom(url, price, imei, imsi, ext);
				String result = HttpTool.sendHttp(reqUrl, "");
				if (result.equals("getsms_error")) return sms;
				SMSInfoVO vo = URLHelperTAX.parseTAXTele(result, ogOrderNO, key);
				
				sms = new ArrayList<SMSInfoVO>();
		        sms.add(vo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 9
	 * 明日空间
	 */
	public static List<SMSInfoVO> getOnlineSMS9(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		try {
			int fee = CommonTools.convertPrice(price) * 100;
			String reqUrl = URLHelperMRKJ.pieceURL(url, fee+"", imei, imsi);
			String result = HttpTool.sendHttp(reqUrl, "");
			String content = new String(Base64.decode(result));
			sms = URLHelperMRKJ.parseJosn(content, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 10
	 * 电盈联通
	 */
	public static List<SMSInfoVO> getOnlineSMS10(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		try {
			String gameId = URLHelperDY.getGameId(price);
			String reqContent = URLHelperDY.pieceContent(imsi, imei, gameId, ext);
			String result = HttpTool.sendHttp(url, reqContent);
			
			sms = URLHelperDY.parse(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 11
	 * 智玩
	 */
	public static List<SMSInfoVO> getOnlineSMS11(String url, String price, String extData, String ogOrderNO, String key, String province) {
		List<SMSInfoVO> sms = new ArrayList<SMSInfoVO>();
		try {
			String provinceCode = URLHelperZW.getProvinceCode(province);
			String orderid = provinceCode + URLHelperZW.getZWOrderId();
			
			String reqUrl = URLHelperZW.pieceURL1(url, price, orderid);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			SMSInfoVO info = new SMSInfoVO();
	        info.setPort("10658421018");
	        info.setContent(result);
	        info.setProvider("联络阅读");
	        info.setCompany("智玩");
	        info.setOrderNO("ZW"+orderid);
	        info.setKey(key);
	        sms.add(info);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 12
	 * 电盈电信
	 */
	public static List<SMSInfoVO> getOnlineSMS12(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		try {
			String gameId = URLHelperDY.getTelnetGameId(price);
			String reqContent = URLHelperDY.pieceContent(imsi, imei, gameId, ext);
			String result = HttpTool.sendHttp(url, reqContent);
			
			sms = URLHelperDY.parse(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 13
	 *深圳斯达通
	 */
	public static List<SMSInfoVO> getOnlineSMS13(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		
		String paycode = "";
		if ("1".equals(price)) {
			paycode = "30000834957501";
		} else if ("2".equals(price)) {
			paycode = "30000834957502";
		} else if ("4".equals(price)) {
			paycode = "30000834957503";
		} else if ("5".equals(price)) {
			paycode = "30000834957504";
		} else if ("6".equals(price)) {
			paycode = "30000834957505";
		} else if ("8".equals(price)) {
			paycode = "30000834957506";
		} else {
			paycode = "30000834957507";
		}
		
		String newExt = ogOrderNO+"_"+ext;
		
		try {
			String reqUrl = URLHelperSZSDT.pieceURL(url, paycode, imei, imsi, newExt);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			sms = URLHelperSZSDT.parseJosn(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	/***
	 * URL_NO : 14
	 *深圳斯达通（新）
	 */
	public static List<SMSInfoVO> getOnlineSMS14(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		
		String paycode = "";
		if ("0.01".equals(price)) {
			paycode = "20131120755201";
		} else if ("2".equals(price)) {
			paycode = "20131120755202";
		} else if ("4".equals(price)) {
			paycode = "20131120755203";
		} else if ("5".equals(price)) {
			paycode = "20131120755204";
		} else if ("6".equals(price)) {
			paycode = "20131120755205";
		} else if ("8".equals(price)) {
			paycode = "20131120755206";
		} else {
			paycode = "20131120755207";
		}
		
		String newExt = ogOrderNO.substring(ogOrderNO.length()-9);
		
		try {
			String reqUrl = URLHelperXSZSDT.pieceURL(url, paycode, imei, imsi, newExt);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			sms = URLHelperXSZSDT.parseXml(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	/***
	 * URL_NO : 15
	 *广州游发
	 */
	public static List<SMSInfoVO> getOnlineSMS15(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		
		String paycode = "";
		if ("2".equals(price)) {
			paycode = "30000876013910";
		} else if ("4".equals(price)) {
			paycode = "30000876013905";
		} else if ("6".equals(price)) {
			paycode = "30000876013903";
		} else if ("10".equals(price)) {
			paycode = "30000886973802";
		}
		
		String newExt = ogOrderNO+"_"+ext;
		
		try {
			String reqUrl = URLHelperGZYF.pieceURL(url, paycode, imei, imsi, newExt);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			sms = URLHelperGZYF.parseJosn(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	/***
	 * URL_NO : 16
	 *YFMP
	 */
	public static List<SMSInfoVO> getOnlineSMS16(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		
		String paycode = "";
		if ("1".equals(price)) {
			paycode = "30000887212802";
		} else if ("2".equals(price)) {
			paycode = "30000887212806";
		} else if ("4".equals(price)) {
			paycode = "30000887212801";
		} else if ("6".equals(price)) {
			paycode = "30000887212803";
		} else if ("8".equals(price)) {
			paycode = "30000887212807";
		} else if ("10".equals(price)){
			paycode = "30000887212804";
		} else {
			paycode = "30000887212805";
		}
		
		String newExt = ogOrderNO+"_"+ext;
		
		try {
			String reqUrl = URLHelperYFMP.pieceURL(url, paycode, imei, imsi, newExt);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			sms = URLHelperYFMP.parseJosn(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	/***
	 * URL_NO : 17
	 *万贝移动
	 */
	public static List<SMSInfoVO> getOnlineSMS17(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		
		try {
			String reqUrl = URLHelperWBYD.pieceURL(url, price, imei, imsi, ext);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			sms = URLHelperWBYD.parseJosn(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	/***
	 * URL_NO : 18
	 *DTYD
	 */
	public static List<SMSInfoVO> getOnlineSMS18(String url, String price, String ogOrderNO, String ext, String imsi, 
			String imei, String key) {
		List<SMSInfoVO> sms = null;
		
		try {
			String reqUrl = URLHelperDTYD.pieceURL(url, price, imei, imsi, ext);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			sms = URLHelperDTYD.parseJosn(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return sms;
	}
	
	
	/***
	 * URL_NO : 19
	 *北京盛峰
	 */
	public static List<SMSInfoVO> getOnlineSMS19(String url, String price, String ogOrderNO, 
			String key, String keyWords,String MobileType,String mobile,String province,String imei) {
		List<SMSInfoVO> sms = null;
		try {
			String reqUrl = URLHelperBJSF.pieceURL(url, keyWords+ogOrderNO, keyWords, price, MobileType,mobile,province,imei);
			String result = HttpTool.sendHttp(reqUrl, "");
			
			sms = URLHelperBJSF.parseBJSF(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sms;
	}
	
	/***
	 * URL_NO : 20
	 *掌游互动
	 */
	public static List<SMSInfoVO> getOnlineSMS20(String url, String productid,String ogOrderNO,String key,String port) {
		List<SMSInfoVO> sms = null;
		try {
			String reqUrl = URLHelperZYHD.pieceURL(url,URLHelperZYHD.ACCOUNT,productid,ogOrderNO);
			String result = HttpTool.sendHttp(reqUrl, "");
			sms = URLHelperZYHD.parseZYHD(result, ogOrderNO, key,port);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sms;
	}
	
	
	private static final int openDayOfWeek[] = {0,6};
	/***
	 * URL_NO : 21
	 *掌游互动MM
	 */
	public static List<SMSInfoVO> getOnlineSMS21(String url, String imsi,
			String cpparam, String paycode, String imei, String ip, String province,
			String city,String bsc_lac, String bsc_cid,String ogOrderNO ,String key) {
		List<SMSInfoVO> sms = null;
		try {
//			放量时间：周六和周日全天放量。周一至周五晚上19点放量到次天早上7点。
//			日20元月20元/用户，同一用户的连续上行，控制在30s以上
			boolean open = DateUtil.isInDayOfWeak(openDayOfWeek);
			if(!open){
				open = DateUtil.isInHours("190000", "235959");
				if(!open){
					open = DateUtil.isInHours("000000", "070000");
				}
			}
//			if(!open){
//				return sms;
//			}
			String reqUrl = URLHelperZYHD.pieceURLMM(url, imsi, cpparam, paycode, imei, ip, province, city, bsc_lac, bsc_cid);
			String result = HttpTool.sendHttp(reqUrl, "");
			sms = URLHelperZYHD.parseZYHDMM(result, ogOrderNO, key);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return sms;
	}
	
	
	public static int convertPrice(String value) {
		int price = 0;
		try {
			price = Integer.parseInt(value);
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return price;
	}
}
