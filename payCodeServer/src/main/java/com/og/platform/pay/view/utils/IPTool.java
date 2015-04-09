/**
 * OGPayServer
 */
package com.og.platform.pay.view.utils;

import com.og.platform.pay.view.utils.ip.IPtest;
import com.payinfo.net.handler.HttpRequest;

/**
 * @author ogplayer.com
 *
 * 2013年10月31日
 */
public class IPTool {
	/**
	 * 从HTTP请求中提取IP地址
	 */
	public static String getRealIP(HttpRequest request) {
		String ip = request.header("X-Real-IP");
		if (ip == null || "unknown".equalsIgnoreCase(ip)) {
			ip = request.header("X-Forwarded-For");
			if (ip != null && !"unknown".equalsIgnoreCase(ip)) {
				int index = ip.indexOf(',');
				if (index != -1) {
					ip = ip.substring(0, index);
				}
			}
		}
		if (ip == null) {
			ip = request.remoteAddress().toString();
		}

		int port = ip.indexOf(":");
		if (port > 1) {
			ip = ip.substring(1, port);
		}
		return ip;
	}
	
	public static String queryProvince(String ip) {
		return IPtest.getInstance().queryProvince(ip);
	}
}	
