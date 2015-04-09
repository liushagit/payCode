/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;


/**
 * @author ogplayer.com
 *
 * 2013年12月2日
 */
public class URLHelperRZD {
	/** 拼接锐之道请求URL地址*/
	public static String pieceURL(String url, String price, String imei, String imsi, String extData){
		StringBuilder reqUrl = new StringBuilder();
		reqUrl.append(url+"?ptid=").append(getPID(price))
			.append("&imsi=").append(imsi)
			.append("&imei=").append(imei)
			.append("&cpparam=").append(extData)
			.append("&channelid=");
		
		return reqUrl.toString();
	}
	
	private static String getPID(String price) {
		String pid = "468";
		if (price.equals("2")) {
			pid = "468"; //468
		} else if (price.equals("4")) {
			pid = "469";
		} else if (price.equals("6")) {
			pid = "470";
		} else if (price.equals("8")) {
			pid = "471";
		} else if (price.equals("10")) {
			pid = "472";
		}
		return pid;
	}
	
}
