/**
 * OGPayServer
 */
package com.og.platform.pay.view.server;

import com.payinfo.net.handler.HttpRequest;
import com.payinfo.net.handler.HttpResponse;

/**
 * @author ogplayer.com
 *
 * 2013年10月31日
 */
public class OGAnalyServer {
	/** 
	 * 付费
	 * appname 		游戏名
	 * pname		包名
	 * imsi
	 * imei
	 * fee
	 * ext			自定义
	 * mobile		手机号
	 * smscenter	短信中心
	 * appversion	app版本
	 * sdkversion   sdk版本
	 * 
	 * */
	public void pay(HttpRequest request, HttpResponse response) throws Exception {
		String appName = request.getParam("appname");
		String pName = request.getParam("pname");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String fee = request.getParam("fee");
		String ext = request.getParam("ext");
		String mobile = request.getParam("mobile");
		String smsCenter = request.getParam("smscenter");
		String appVersion = request.getParam("appversion");
		String sdkVersion = request.getParam("sdkversion");
	}
}
