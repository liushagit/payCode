/**
 * OGPayServer
 */
package com.og.platform.pay.view.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.og.platform.pay.common.domain.OGMMPayConf;
import com.og.platform.pay.common.utils.CommonTools;
import com.og.platform.pay.common.utils.ConstantDefine;
import com.og.platform.pay.common.utils.HttpTool;
import com.og.platform.pay.common.vo.AreaVO;
import com.og.platform.pay.common.vo.ProritySMSInfoVO;
import com.og.platform.pay.common.vo.SMSInfoVO;
import com.og.platform.pay.common.vo.SimpleSMSVO;
import com.og.platform.pay.view.ActionAware;
import com.og.platform.pay.view.utils.IPTool;
import com.og.platform.pay.view.utils.ip.IPtest;
import com.og.platform.pay.view.utils.phone.MobileSeeker;
import com.payinfo.net.handler.HttpRequest;
import com.payinfo.net.handler.HttpResponse;
import com.payinfo.net.log.LoggerFactory;

/**
 * @author ogplayer.com
 *
 * 2013年10月31日
 */
public class OGCodeServer extends ActionAware {
	private static Logger logger = LoggerFactory.getLogger(OGCodeServer.class);
	
	/** 
	 * 获取短代指令
	 * appname 		游戏名
	 * imsi
	 * imei
	 * fee
	 * ext			自定义
	 * mobile		手机号
	 * smscenter	短信中心
	 * */
	public void reqCode(HttpRequest request, HttpResponse response) throws Exception {
		String appName = request.getParam("appId");
		String imsi = request.getParam("imsi");
		String imei = request.getParam("imei");
		String fee = request.getParam("fee");
		String ext = request.getParam("ext");
		String mobile = request.getParam("mobile");
		String centerNO = request.getParam("smscenter");
		
		String ip = getRealIP(request);
//		String ip="192.168.1.172";
		if (ip.equals("192.168.1.1") || ip.equals("192.168.1.172")) {
			ip = request.getParam("ip");
		}
		logger.info("用户访问IP=" + ip);
		String tempProvince = IPTool.queryProvince(ip);
		String country = IPtest.getInstance().queryCountry(ip);
		if (tempProvince.equals("广东")) {
			logger.info(country);
			if (country.contains("广州")) {
				tempProvince="广州";
			}
		}
		logger.info("根据IP查询省份=" + tempProvince);
		int codeType = CommonTools.locateOperator(imsi);
		//MM计费通道
		boolean isMobile = (codeType == ConstantDefine.CODE_TYPE_MOBILE);//判断是否为移动
		
		if (isMobile) {
			try {
				OGMMPayConf mmpay = commonAction.checkMMPayChannel(appName,tempProvince);//检查游戏是否可用MM通道
				if (mmpay == null) mmpay = commonAction.checkMMPayChannel("comappid",tempProvince);//检查通用MM通道
				if (mmpay != null) {
					//查询是否被屏蔽
					StringBuilder param = new StringBuilder();
					String tempUrl = "http://192.168.1.50:8080/mobile/black.jsp";
					param.append("cpid=app001")
						.append("&imsi=").append(imsi)
						.append("&imei=").append(imei);
					String result = HttpTool.sendGet(tempUrl, param.toString());//0，非黑名单 -1，黑名单 -2，参数不全
					logger.info("移动MM屏蔽查询url:" + tempUrl);
					logger.info("移动MM屏蔽查询param:" + param.toString());
					logger.info("移动MM屏蔽查询result:" + result);
					if ("-1".equals(result)) {
						//添加屏蔽信息
						logger.error("移动MM屏蔽该imsi和imei:" + param.toString());
						commonAction.addUnpopularityInfo(imsi, imei);
					} else {
						StringBuilder url = new StringBuilder();
						url.append(mmpay.getUrl()).append("?appname=").append(appName)
						.append("&imsi=").append(imsi)
						.append("&imei=").append(imei)
						.append("&fee=").append(fee)
						.append("&ext=").append(ext)
						.append("&mobile=").append(mobile)
						.append("&smscenter=").append(centerNO);
						String sms = reqMMCode(url.toString());
						if (sms != null && sms.length() > 0) {
							logger.info("移动MM通道:" + sms);
							response.content(sms).end();
							
							//插入取指令记录
							addInstruction(sms, fee, mmpay.getCodeNo(),"移动MM");
							return;
						}
					}
					
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
			}
		}
		
		
		
		String sms = "error";
		
		String province = tempProvince;
		try {
			AreaVO area = null;
			codeType = CommonTools.locateOperator(imsi);
			// 第一步:根据手机号判断归属地
			if (mobile!=null) {
				area = MobileSeeker.queryProvince(mobile);
				if (area!=null)province = area.getProvince();
				logger.info("根据手机号查询省份=" + province);
			}
			// 第二步:根据短信中心判断归属
//			if (area == null && centerNO!=null) {
//				String smscn = "";
//				if (centerNO.length()>9)smscn = centerNO.substring(0, 9);
//				logger.info("短信中心=" + smscn);
//				province = commonAction.getProvinceByCenter(smscn);
//				if (province != null) {
//					area = new AreaVO();
//					area.setProvince(province);
//					area.setCodeType(codeType);
//					logger.info("根据短信中心查询省份=" + province);
//				}
//			}
			// 第三步:根据IP地址判断归属
//			if (area == null) {
//				province = IPTool.queryProvince(ip);
//				logger.info("根据IP查询省份=" + province);
//				
//				area = new AreaVO();
//				area.setProvince(province);
//				area.setCodeType(codeType);
//			}
			logger.info("appName"+appName+"|tempProvince"+tempProvince+"|codeType"+codeType+"|fee"+ 
					fee+"|imsi"+imsi+"|imei"+imei+"|mobile"+mobile+"|centerNO"+centerNO+"|ip"+ip+"|ext"+ext+"|country"+country);
			List<SMSInfoVO> vos = commonAction.matchCode(appName, tempProvince, codeType, 
					fee, imsi, imei, mobile, centerNO, ip, ext, country);
			if (!vos.isEmpty()) {
				List<SimpleSMSVO> simples = new ArrayList<SimpleSMSVO>();
				for (SMSInfoVO vo : vos) {
					SimpleSMSVO simple = new SimpleSMSVO();
					simple.setPort(vo.getPort());
					simple.setContent(vo.getContent());
					simple.setProvider(vo.getProvider());
					simple.setOrderNo(vo.getOrderNO());
					simple.setKey(vo.getKey());
					simples.add(simple);
					logger.info("短信提供商=" + vo.getCompany() + ";短信端口=" + vo.getPort() + 
							";短信内容=" + vo.getContent() + ";短信关键字=" + vo.getKey());
					
					if (isFilter1(vo.getCompany()) && isMobile) {
						String temp = vo.getContent();
						int length = temp.length();
						String orderId = temp.substring(length-7);
						send(orderId, ext, "DY", imsi, ip, fee);
					} else if (isFilter2(vo.getCompany()) && isMobile) {
						send(vo.getOrderNO(), ext, "ZW", imei, ip, fee);
					} else if (isFilter3(vo.getCompany()) && isMobile) {
						send(vo.getOrderNO(), ext, "HZZW", imei, ip, fee);
					} else if (isFilter4(vo.getCompany()) && isMobile) {
						send(vo.getOrderNO(), ext, "LGHD", imei, ip, fee);
					}
				}
				sms = JSON.toJSONString(simples);
			}
		}catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		if (sms.equals("error")) {
			logger.error("获取短信指令失败,请排查错误! mobile=" +mobile +";province="+ province+";codeType="+ codeType);
		} else {
			logger.info("短信指令:" + sms);
		}
		response.content(sms).end();
	}
	
	/** 特殊过滤规则*/
	private boolean isFilter1(String company) {
		if ("电盈".contains(company)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isFilter2(String company) {
		if ("智玩".contains(company)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isFilter3(String company) {
		if ("杭州掌维".contains(company)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean isFilter4(String company) {
		if ("灵光互动".contains(company)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	private void send(String linkId, String ext, String sp, String imsi, String ip, String fee) {
		StringBuilder url = new StringBuilder();
		url.append("http://127.0.0.1/common/bindOrder?linkId=").append(linkId)
			.append("&ext=").append(ext)
			.append("&sp=").append(sp)
			.append("&imsi=").append(imsi)
			.append("&ip=").append(ip)
			.append("&fee=").append(fee);
		HttpTool.sendHttp(url.toString(), "");
	}
	
	private String reqMMCode(String url) {
		try  {
			String sms = HttpTool.sendHttp(url.toString(), "");
			if (sms != null && sms.length() > 0 && !sms.equals("error")) {
				return sms;
			}
		} catch (Exception e) {
			logger.error("从MM获取指令失败，请仔细检查MM服务器!");
			logger.error(e.getMessage(), e.getCause());
		}
		return null;
	}
	
	
	//插入取指令记录
	private void addInstruction(String sms,String fee,String billId,String company) {
		ProritySMSInfoVO psmsList = new ProritySMSInfoVO();
		List<SMSInfoVO> smsList = new ArrayList<SMSInfoVO>();
		SMSInfoVO smsInfoVo = new SMSInfoVO();
		JSONObject myObj = JSONObject.parseObject(sms.replace("[", "").replace("]", ""));
		psmsList.setBillId(billId);
		smsInfoVo.setCompany(company);
		smsInfoVo.setOrderNO(myObj.getString("orderNo"));
		smsList.add(smsInfoVo);
		psmsList.setCodeType(1);
		psmsList.setPrice(Integer.parseInt(fee));
		psmsList.setSmsList(smsList);
		commonAction.addInstruction(psmsList);
	}
	
	private String getRealIP(HttpRequest request) {
		String ip = request.header("X-Real-IP");
		if (ip == null || "unknown".equalsIgnoreCase(ip)) {
			ip = request.header("X-Forwarded-For");
			//
			if (ip != null && !"unknown".equalsIgnoreCase(ip)) {
				int index = ip.indexOf(',');
				if (index != -1) {
					ip = ip.substring(0, index);
				}
			}
		}
		//
		if (ip == null) {
			ip = request.remoteAddress().toString();
		}

		int port = ip.indexOf(":");
		if (port > 1) {
			ip = ip.substring(1, port);
		}
		return ip;
	}
	
	private String adapterYZSMS(List<SMSInfoVO> yzSMSVO, String ext, String imsi, String ip, String fee) {
		List<SimpleSMSVO> simples = new ArrayList<SimpleSMSVO>();
		for (SMSInfoVO vo : yzSMSVO) {
			SimpleSMSVO simple = new SimpleSMSVO();
			simple.setPort(vo.getPort());
			simple.setContent(vo.getContent());
			simple.setProvider(vo.getProvider());
			simple.setOrderNo(vo.getOrderNO());
			simple.setKey(vo.getKey());
			simples.add(simple);
			logger.info("短信提供商=" + vo.getCompany() + ";短信端口=" + vo.getPort() + 
					";短信内容=" + vo.getContent() + ";短信关键字=" + vo.getKey());
			
			send(vo.getOrderNO(), ext, "YZ", imsi, ip, fee);
		}
		return JSON.toJSONString(simples);
	}
	
	private boolean filterYZ(String privonce) {
		return "湖北#河北#山东#黑龙江#吉林#辽宁#西藏#青海#内蒙古#重庆#上海#安徽#海南".contains(privonce);
	}
	
	//http://127.0.0.1:9991/code/test
//	public void test(HttpRequest request, HttpResponse response) {
//		String content = request.body();
//		System.out.println(content);
//		response.content(content).end();
//	}
}
