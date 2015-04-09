/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.og.platform.pay.common.utils.MD5Tool;
import com.og.platform.pay.common.vo.SMSInfoVO;


/**
 * 北京盛峰
 * 
 * @author ogplayer.com
 *
 * 2013年11月27日
 */
public class URLHelperBJSF {
	
	/** 拼接北京盛峰请求URL地址*/
	public static String pieceURL(String url, String ogOrderNO,  String keyWords, String Fee,
			String MobileType,String mobile,String province,String imei){
		
//		Sign = md5(OrderId+KeyWords+Fee+MobileType+UserId+Key)
		StringBuffer sb = new StringBuffer();
		sb.append(ogOrderNO).append(keyWords).append(Fee).append(mobile).append(imei).append(MACKEY);
//		目前不需要签名验证，Sign可为任何值也可补0.
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url).append("?").append("OrderId=").append(ogOrderNO).append("&");
		reqUrl.append("KeyWords=").append(keyWords).append("&");
		reqUrl.append("Fee=").append(Fee).append("&");
		reqUrl.append("MobileType=").append(MobileType).append("&");
		reqUrl.append("UserId=").append(imei).append("&");
		reqUrl.append("Sign=").append(getSign(sb.toString())).append("&");
		reqUrl.append("Mobile=").append(mobile).append("&");
		reqUrl.append("ProvinceId=").append(getProvinceCodeBJSF(province));
		
		return reqUrl.toString();
	}
	
	public static String getWimiCode(String fee) {
		String wimicode = "20131120715201";

		return wimicode;
	}
	
	
	public static List<SMSInfoVO> parseBJSF(String content, String ogOrderNO, String key) throws Exception {
		String res[] = content.split("~");
		if(!"000".equals(res[0])){
			StringBuffer sb = new StringBuffer();
			sb.append(res[0]);
			if(res.length > 1){
				sb.append(":").append(res[1]);
			}
			throw new Exception(sb.toString());
		}
		
		List<SMSInfoVO> infos = new ArrayList<SMSInfoVO>();
		SMSInfoVO info = new SMSInfoVO();
		info.setPort(res[2]);
        info.setContent(res[3]);
        info.setProvider("运营商");
        info.setCompany("北京盛峰");
        info.setOrderNO(ogOrderNO);
        info.setKey(key);
        infos.add(info);
        return infos;
	}
	
/***************北京盛峰*******************/
	
	private static final String PROVINCESTR_BJSF = "00,未知;01,北京;02,上海;03,天津;04,重庆;05,河北;06,山西;07,内蒙古;08,辽宁;09,吉林;10,黑龙江;11,江苏;12,浙江;13,安徽;14,福建;15,江西;16,山东;17,河南;18,湖北;19,湖南;20,广东;21,广西;22,海南;23,四川;24,贵州;25,云南;26,西藏;27,陕西;28,甘肃;29,青海;30,宁夏;31,新疆";
	private static Map<String, String> PROVINCEMAP_BJSF = new HashMap<String, String>();
	private static void loadProvinceBJSF() {
		PROVINCEMAP_BJSF.clear();
		String[] temp = PROVINCESTR_BJSF.split(";");
		for (String line : temp) {
			String[] kv = line.split(",");
			String code = kv[0];
			String province = kv[1];
			province = province.substring(0, 2);
			PROVINCEMAP_BJSF.put(province, code);
		}
	}
	
	private static String getProvinceCodeBJSF(String province) {
		if (PROVINCEMAP_BJSF.isEmpty()) {
			loadProvinceBJSF();
		}
		String res = PROVINCEMAP_BJSF.get(province);
		return res == null ? "00" : res;
	}
	
	private static final String MACKEY = "1E10B0670331DE6F";
	private static String getSign(String msg){
//		Sign = md5(OrderId+KeyWords+Fee+MobileType+UserId+Key)
//		目前不需要签名验证，Sign可为任何值也可补0.
		return MD5Tool.toMD5Value(msg);
	}
	
}
