/**
 * OGPaySystem
 */
package com.og.platform.pay.rpc.action.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.og.platform.pay.common.domain.MsgCenter;
import com.og.platform.pay.common.domain.OGMMPayConf;
import com.og.platform.pay.common.domain.OGPayDirect;
import com.og.platform.pay.common.domain.OGPayInfo;
import com.og.platform.pay.common.domain.SMSCodeInfo;
import com.og.platform.pay.common.utils.CommonTools;
import com.og.platform.pay.common.utils.ConstantDefine;
import com.og.platform.pay.common.utils.MD5Tool;
import com.og.platform.pay.common.vo.ProritySMSInfoVO;
import com.og.platform.pay.common.vo.SMSInfoVO;
import com.og.platform.pay.data.Application;
import com.og.platform.pay.data.utils.OnlineSMSTool;
import com.og.platform.pay.data.utils.PriceComparator;
import com.og.platform.pay.data.utils.PriorityComparator;
import com.og.platform.pay.data.utils.URLHelperHJ;
import com.og.platform.pay.rpc.action.CommonAction;
import com.payinfo.net.log.LoggerFactory;

/**
 * @author ogplayer.com
 *
 *         2013年10月30日
 */
public class CommonActionImpl extends Application implements CommonAction {
	private static Logger logger = LoggerFactory.getLogger(CommonActionImpl.class);
	
	@Override
	public List<SMSInfoVO> matchCode(String appName, String province,
			int codeType, String price, String imsi, String imei,
			String mobile, String smscn, String ip, String extData, String country) {
		logger.info("tttttttttt"+codeType);
		String ogOrderNO = CommonTools.createOrderNO();
		List<ProritySMSInfoVO> psmsList = new ArrayList<ProritySMSInfoVO>();
		int priceInt = OnlineSMSTool.convertPrice(price);
		// 第一步:先判断是否该APP绑定了指定计费点 
		ProritySMSInfoVO psms1 = matchCode1(province, codeType, priceInt, imsi,
				imei, mobile, smscn, ip, extData, ogOrderNO, appName);
		if(psms1 != null) {
			psmsList.add(psms1);
			Collections.sort(psmsList, new PriorityComparator<ProritySMSInfoVO>());
			List<SMSInfoVO> sms = psmsList.get(0).getSmsList();
			return sms;
		}
		
		// 第二步:从代码库中查找对应的计费点(主要是裸代方式)
		ProritySMSInfoVO psms2 = matchCode2(province, codeType, priceInt,
				extData, ogOrderNO, ip, country);
		if (psms2 != null)
			psmsList.add(psms2);
		// 第三步:从指定的策略池中查找对应的计费点(主要是在线获取方式)
		ProritySMSInfoVO psms3 = matchCode3(province, codeType, price, imsi,
				imei, mobile, smscn, ip, extData, ogOrderNO);
		if (psms3 != null)
			psmsList.add(psms3);
		
		if (psmsList.isEmpty()) {
			// 第四步:从代码库中查找对应的计费点(主要是价格最靠近)
			ProritySMSInfoVO psms4 = matchCode4(province, codeType, priceInt,
					extData, ogOrderNO);
			if (psms4 != null)
				psmsList.add(psms4);
		}

		if (psmsList.isEmpty())
			return null;
		Collections.sort(psmsList, new PriorityComparator<ProritySMSInfoVO>());
		List<SMSInfoVO> sms = psmsList.get(0).getSmsList();
		return sms;
	}

	/** 筛选第一步 */
	
	private ProritySMSInfoVO matchCode1(String province, int codeType,
			int price, String imsi, String imei, String mobile, String smscn, 
			String ip, String extData, String ogOrderNO, String appName) {
		
		//查询游戏指定通道的信息
		OGPayDirect payDir = commonAO().queryBill(appName);
		if (payDir == null) {
			return null;
		} else {
			ProritySMSInfoVO psmsList = new ProritySMSInfoVO();
			StringBuilder billId = new StringBuilder();
			billId.append("('").append(payDir.getBillId().replace(",", "','").trim()).append("')");
			
			//从裸代中查找
			List<SMSCodeInfo> codes = codeHelperAO().getDirectCodes(codeType, province, billId.toString());
			if (codes == null || codes.size() < 1) {
				//从在线获取方式中查找
				List<OGPayInfo> pays = ogpayHelperAO().queryBill(payDir,codeType,province);
				if (pays == null || pays.size() < 1) return null;
				//根据不同通道拼接url做特殊处理
				psmsList = getSMSInfo1(pays, province, codeType, price+"", imsi, imei, 
						mobile, smscn, ip, extData, ogOrderNO);
				return psmsList;
				
			}
			
			psmsList = getSMSInfo(codes, ogOrderNO, extData, price, ip);
			return psmsList;
		}
		
	}
	

	/** 筛选第二步 */
	private ProritySMSInfoVO matchCode2(String province, int codeType,
			int price, String extData, String ogOrderNO, String ip, String country) {
		return findCode0(province, codeType, price, extData, ogOrderNO, ip, country);
	}

	/** 筛选第三步 */
	private ProritySMSInfoVO matchCode3(String province, int codeType,
			String price, String imsi, String imei, String mobile,
			String smscn, String ip, String extData, String ogOrderNO) {
		
		List<OGPayInfo> infos = ogpayHelperAO().pieceBill(codeType, province, price);
		//根据不同通道拼接url做特殊处理
		ProritySMSInfoVO psmsList = getSMSInfo1(infos, province, codeType, price, imsi, 
				imei, mobile, smscn, ip, extData, ogOrderNO);
		
		return psmsList;
	}

	/** 筛选第二步 */
	private ProritySMSInfoVO matchCode4(String province, int codeType,
			int price, String extData, String ogOrderNO) {
		return findCode1(province, codeType, price, extData, ogOrderNO);
	}

	/** 匹配一次短信价格、省份合适的通道 */
	private ProritySMSInfoVO findCode0(String province, int codeType,
			int price, String extData, String ogOrderNO, String ip, String country) {
		List<SMSCodeInfo> codes = codeHelperAO().getCodes(codeType, province, country);
		
		//从裸代中匹配通道信息
		ProritySMSInfoVO psmsList = getSMSInfo(codes, ogOrderNO, extData, price, ip);
		return psmsList;
	}
	
	/** 匹配最接近短信价格、省份合适的通道 */
	private ProritySMSInfoVO findCode1(String province, int codeType,
			int price, String extData, String ogOrderNO) {
		List<SMSCodeInfo> codes = codeHelperAO().getCodes(codeType, province, null);
		List<SMSCodeInfo> tempList1 = new ArrayList<SMSCodeInfo>(); // 价格最接近
		for (SMSCodeInfo info : codes) {
			// 优先判断价格是否合适
			if (info.getPrice() > price) {
				continue;
			}

			tempList1.add(info);
		}
		// 从价格最匹配中筛选
		if (tempList1.isEmpty())
			return null;
		Collections.sort(tempList1, new PriceComparator());
		SMSCodeInfo info = tempList1.get(0);

		StringBuilder newOrderNO = new StringBuilder();
		if (isFilter(info.getCompany())) {
			newOrderNO.append("mr");
		}
		newOrderNO.append(ogOrderNO);

		List<SMSInfoVO> smsList = new ArrayList<SMSInfoVO>();
		SMSInfoVO sms = new SMSInfoVO();
		sms.setPort(info.getSmsPort());
		if (info.isExt()) {
			// 特殊过滤规则
			if (isFilter1(info.getCompany())) {
				String orderId = CommonTools.getDYOrderId();
				sms.setContent(concatSMS(info.getSmsContent(), orderId));
			} else {
				sms.setContent(concatSMS(info.getSmsContent(), extData));
			}
		} else {
			sms.setContent(info.getSmsContent());
		}
		sms.setProvider("运营商");
		sms.setConfirm(info.getConfirmContent());
		sms.setCompany(info.getCompany());
		sms.setOrderNO(newOrderNO.toString());
		sms.setKey(info.getKey());
		smsList.add(sms);
		if (isFilter1(info.getCompany())) {
			SMSInfoVO sms1 = new SMSInfoVO();
			sms1.setPort("1065889920001");
			sms1.setContent("Y");
			sms1.setProvider("电盈");
			sms1.setConfirm(info.getConfirmContent());
			sms1.setCompany(info.getCompany());
			sms1.setOrderNO(newOrderNO.toString());
			sms1.setKey(info.getKey());
			smsList.add(sms1);
		}

		ProritySMSInfoVO psmsList = new ProritySMSInfoVO();
		psmsList.setSmsList(smsList);
		psmsList.setPriority(info.getPriority());
		return psmsList;
	}

	/** 混合通道拼接 */
	private List<SMSInfoVO> findCode2(OGPayInfo info, String ogOrderNO, String extData) {
		if (info == null || info.getCodeNO() == null
				|| info.getCodeNO().length() == 0)
			return null;

		String[] codeNOs = info.getCodeNO().split("\\#");
		List<SMSInfoVO> tempList = new ArrayList<SMSInfoVO>();
		for (String codeNO : codeNOs) {
			SMSCodeInfo si = codeHelperAO().getCodeInfo(codeNO);
			if (si == null)
				continue;

			String sms = si.getSmsContent();
			if (si.isExt()) {
				// 特殊过滤规则
				if (isFilter1(info.getCompany())) {
					String orderId = CommonTools.getDYOrderId();
					sms = concatSMS(sms, orderId);
				} else {
					sms = concatSMS(sms, extData);
				}
			} 
			
			SMSInfoVO sv = new SMSInfoVO();
			sv.setPort(si.getSmsPort());
			sv.setContent(sms);
			sv.setProvider(si.getProvider());
			sv.setConfirm(si.getConfirmContent());
			sv.setCompany(si.getCompany());
			sv.setOrderNO(ogOrderNO);
			sv.setKey(si.getKey());
			tempList.add(sv);
			if (isFilter1(info.getCompany())) {
				SMSInfoVO sms1 = new SMSInfoVO();
				sms1.setPort("1065889920001");
				sms1.setContent("Y");
				sms1.setProvider("电盈");
				sms1.setConfirm(si.getConfirmContent());
				sms1.setCompany(si.getCompany());
				sms1.setOrderNO(ogOrderNO);
				sms1.setKey(si.getKey());
				tempList.add(sms1);
			}
		}
		return tempList;
	}
	
	private ProritySMSInfoVO getSMSInfo(List<SMSCodeInfo> codes, String ogOrderNO, 
			String extData, int price, String ip) {
		List<SMSCodeInfo> tempList0 = new ArrayList<SMSCodeInfo>(); // 价格最匹配
		int maxPriority = 0;
		for (SMSCodeInfo info : codes) {
			if (info.getPrice() == price && info.getPriority() >= maxPriority) {
				tempList0.add(info);
			}
		}
		
		if (tempList0.isEmpty())
			return null;
		Collections.sort(tempList0, new PriorityComparator<SMSCodeInfo>());
		SMSCodeInfo info = tempList0.get(0);
		
		StringBuilder newOrderNO = new StringBuilder();
		if (isFilter(info.getCompany())) {
			newOrderNO.append("mr");
		} else if (isFilter2(info.getCompany())) {
			ogOrderNO = CommonTools.getLGHDOrderId();
			newOrderNO.append("lghd");
		}
		newOrderNO.append(ogOrderNO);

		List<SMSInfoVO> smsList = new ArrayList<SMSInfoVO>();
		SMSInfoVO sms = new SMSInfoVO();
		sms.setPort(info.getSmsPort());
		if (info.isExt()) {
			// 特殊过滤规则
			if (isFilter1(info.getCompany())) {
				String orderId = CommonTools.getDYOrderId();
				sms.setContent(concatSMS(info.getSmsContent(), orderId));
			} else {
				sms.setContent(concatSMS(info.getSmsContent(), extData));
			}
		} else {
			if ("杭州掌维".equals(info.getCompany())) {
				String hzzwSmsContent = getHZZWSmsContent(info,newOrderNO.toString());
				sms.setContent(hzzwSmsContent);
			} else if (isFilter2(info.getCompany())) {
				sms.setContent(concatSMS(info.getSmsContent(), ogOrderNO));
			}  else {
				sms.setContent(info.getSmsContent());
			}
		}
		sms.setProvider("运营商");
		sms.setConfirm(info.getConfirmContent());
		sms.setCompany(info.getCompany());
		sms.setOrderNO(newOrderNO.toString());
		sms.setKey(info.getKey());
		smsList.add(sms);
		if (isFilter1(info.getCompany())) {
			SMSInfoVO sms1 = new SMSInfoVO();
			sms1.setPort("1065889920001");
			sms1.setContent("Y");
			sms1.setProvider("电盈");
			sms1.setConfirm(info.getConfirmContent());
			sms1.setCompany(info.getCompany());
			sms1.setOrderNO(newOrderNO.toString());
			sms1.setKey(info.getKey());
			smsList.add(sms1);
		} else if (isFilter2(info.getCompany())) {
			smsList.add(sms);
		}

		ProritySMSInfoVO psmsList = new ProritySMSInfoVO();
		psmsList.setSmsList(smsList);
		psmsList.setPriority(info.getPriority());
		psmsList.setBillId(info.getCodeNO());
		psmsList.setCodeType(info.getCodeType());
		psmsList.setPrice(info.getPrice());
		addInstruction(psmsList);
		return psmsList;
	}
	
	private ProritySMSInfoVO getSMSInfo1(List<OGPayInfo> infos ,String province, 
			int codeType, String price, String imsi, String imei, String mobile,
			String smscn, String ip, String extData, String ogOrderNO) {
		if (infos == null)
			return null;

		List<SMSInfoVO> sms = null;
		for (OGPayInfo info : infos) {
			logger.info("info==" + info.getUrlNO() +":" + info.getUrl());
			if (info.getBillType() == ConstantDefine.BILL_TYPE_CODE) {
				sms = findCode2(info, ogOrderNO, extData);
			} else if (info.getBillType() == ConstantDefine.BILL_TYPE_URL) {
				switch (info.getUrlNO()) {
				case ConstantDefine.URL_NO_ZHIWAN:
//					if (imsi.startsWith("46") && imsi.length()>=15) {
//						sms = OnlineSMSTool.getOnlineSMS1(info.getUrl(), price,
//							extData, ogOrderNO, info.getExtData(), imsi);
//					}
					sms = OnlineSMSTool.getOnlineSMS11(info.getUrl(), price,
							extData, ogOrderNO, info.getExtData(), province);
					break;
				case ConstantDefine.URL_NO_WOQING:
					sms = OnlineSMSTool.getOnlineSMS2(info.getUrl(), price,
							extData, imsi, imei, mobile, ip, ogOrderNO, info.getExtData());
					break;
				case ConstantDefine.URL_NO_HENGJU:
					String temp = URLHelperHJ.filterExtData(extData);
					if (codeType == ConstantDefine.CODE_TYPE_MOBILE) {
						sms = OnlineSMSTool
								.getOnlineSMS3(info.getUrl(), price,
										ogOrderNO, temp, imsi, imei, mobile,
										smscn, ip, info.getExtData());
					} else if (codeType == ConstantDefine.CODE_TYPE_TELNET) {
						sms = OnlineSMSTool
								.getOnlineSMS4(info.getUrl(), price,
										ogOrderNO, temp, imsi, imei, mobile,
										smscn, ip, info.getExtData());
					} else if (codeType == ConstantDefine.CODE_TYPE_UNICOM) {
						sms = OnlineSMSTool
								.getOnlineSMS4(info.getUrl(), price,
										ogOrderNO, temp, imsi, imei, mobile,
										smscn, ip, info.getExtData());
					}
					break;
				case ConstantDefine.URL_NO_WEIMI:
					sms = OnlineSMSTool.getOnlineSMS6(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_RZD:
					sms = OnlineSMSTool.getOnlineSMS7(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_TAX:
					if (codeType == ConstantDefine.CODE_TYPE_TELNET) {
						sms = OnlineSMSTool.getOnlineSMS8(info.getUrl(), price, 
								ogOrderNO, extData, imsi, imei, info.getExtData(), codeType);
					} else if (imsi.startsWith("46") && imsi.length()>=15) {
						sms = OnlineSMSTool.getOnlineSMS8(info.getUrl(), price, 
								ogOrderNO, extData, imsi, imei, info.getExtData(), codeType);
					}
					break;
				case ConstantDefine.URL_NO_MRKJ:
					ogOrderNO = "mr" + ogOrderNO;
					sms = OnlineSMSTool.getOnlineSMS9(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_DY:
					if (codeType == ConstantDefine.CODE_TYPE_UNICOM) {
						sms = OnlineSMSTool.getOnlineSMS10(info.getUrl(), price, 
								ogOrderNO, extData, imsi, imei, info.getExtData());
					} else if (codeType == ConstantDefine.CODE_TYPE_TELNET) {
						sms = OnlineSMSTool.getOnlineSMS12(info.getUrl(), price, 
								ogOrderNO, extData, imsi, imei, info.getExtData());
					}
					break;
				case ConstantDefine.URL_NO_SZSDT:
					sms = OnlineSMSTool.getOnlineSMS13(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_XSZSDT:
					ogOrderNO = "xszsdt" + CommonTools.getMixedOrderId(8);
					sms = OnlineSMSTool.getOnlineSMS14(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_GZYF:
					sms = OnlineSMSTool.getOnlineSMS15(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_YFMP:
					sms = OnlineSMSTool.getOnlineSMS16(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_WBYD:
					sms = OnlineSMSTool.getOnlineSMS17(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_DTYD:
					sms = OnlineSMSTool.getOnlineSMS18(info.getUrl(), price, 
							ogOrderNO, extData, imsi, imei, info.getExtData());
					break;
				case ConstantDefine.URL_NO_BJSF:
					String type = "LT";
					if (codeType == ConstantDefine.CODE_TYPE_TELNET) {
						type = "DX";
					}
					sms = OnlineSMSTool.getOnlineSMS19(info.getUrl(), price, 
							ogOrderNO + "@" + extData ,info.getExtData(),"MA", type, mobile, province,imei);
					break;
				case ConstantDefine.URL_NO_ZYHD:
					sms = OnlineSMSTool.getOnlineSMS20(info.getUrl(),info.getCodeNO(),ogOrderNO+ "@" + extData ,info.getExtData(),"10656666");
					break;
				case ConstantDefine.URL_NO_ZYHD_MM:
					int id = commonAO().addZYHDInfo(ogOrderNO + "@" + extData);
					if (id < 0) {
						break;
					}
					sms = OnlineSMSTool.getOnlineSMS21(info.getUrl(),imsi,"FKWN"+id,info.getCodeNO(),
							imei,ip,province,"","","",ogOrderNO,info.getExtData());
					break;
				case ConstantDefine.URL_NO_ZM:
					sms = OnlineSMSTool.getOnlineSMS22(info.getUrl(), imsi, imei, info.getPrice(), "data10J31"+"@"+extData, ogOrderNO, info.getExtData());
					break;
				default:
					break;
				}
			} else if (info.getBillType() == ConstantDefine.BILL_TYPE_ALL) {
				String[] codeNOs = info.getCodeNO().split("\\#");
				sms = new ArrayList<SMSInfoVO>();
				try {
					for (String codeNO : codeNOs) {
						String[] temps = codeNO.split("\\;");
						if (temps.length != 2) continue;
						String ogNO = temps[0];
						String ogPrice = temps[1];
						OGPayInfo ki = ogpayHelperAO().getBillInfo(ogNO);
						if (ki == null) continue;
						
						List<SMSInfoVO> tempList = getOGCode(ki, ogPrice, imsi, imei, mobile, 
								smscn, ip, extData, ogOrderNO);
						if (tempList != null && !tempList.isEmpty()) {
							sms.addAll(tempList);
						}
							Thread.sleep(10);
					}
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e.getCause());
				}
			}

			if (sms == null) continue;
			ProritySMSInfoVO psmsList = new ProritySMSInfoVO();
			psmsList.setSmsList(sms);
			psmsList.setPriority(info.getPriority());
			psmsList.setBillId(info.getBillId());
			psmsList.setCodeType(info.getCodeType());
			psmsList.setPrice(new Integer(price));
			addInstruction(psmsList);
			return psmsList;
		}

		return null;
	}

	private String concatSMS(String sc, String extData) {
		StringBuilder temp = new StringBuilder();
		temp.append(sc).append(extData);
		return temp.toString();
	}

	private boolean isFilter(String company) {
		if (company.contains("明日")) {
			return true;
		} else {
			return false;
		}
	}
	
	/** 特殊过滤规则*/
	private boolean isFilter1(String company) {
		if ("电盈".contains(company)) {
			return true;
		} else {
			return false;
		}
	}
	
	/** 特殊过滤规则*/
	private boolean isFilter2(String company) {
		if ("灵光互动".contains(company)) {
			return true;
		} else {
			return false;
		}
	}
	
	/** 从在线代码库中查找对应的代码 */
	private List<SMSInfoVO> getOGCode(OGPayInfo info, String price, String imsi,
			String imei, String mobile, String smscn, String ip,
			String extData, String ogOrderNO) {
		List<SMSInfoVO> sms = null;

		switch (info.getUrlNO()) {
		case ConstantDefine.URL_NO_ZHIWAN:
//			sms = OnlineSMSTool.getOnlineSMS1(info.getUrl(), price, extData,
//					ogOrderNO, info.getExtData(), imsi);
			sms = OnlineSMSTool.getOnlineSMS11(info.getUrl(), price, extData,
					ogOrderNO, info.getExtData(), imsi);
			break;
		case ConstantDefine.URL_NO_WOQING:
			sms = OnlineSMSTool.getOnlineSMS2(info.getUrl(), price, extData,
					imsi, imei, mobile, ip, ogOrderNO, info.getExtData());
			break;
		case ConstantDefine.URL_NO_HENGJU:
			String temp = URLHelperHJ.filterExtData(extData);
			if (info.getCodeType() == ConstantDefine.CODE_TYPE_MOBILE) {
				sms = OnlineSMSTool.getOnlineSMS3(info.getUrl(), price,
						ogOrderNO, temp, imsi, imei, mobile, smscn, ip, info.getExtData());
			} else if (info.getCodeType() == ConstantDefine.CODE_TYPE_TELNET) {
				sms = OnlineSMSTool.getOnlineSMS4(info.getUrl(), price,
						ogOrderNO, temp, imsi, imei, mobile, smscn, ip, info.getExtData());
			}
			break;
		default:
			break;
		}

		return sms;
	}

	// =====================================根据短信中心获取省份信息=========================================
	@Override
	public String getProvinceByCenter(String centerNO) {
		MsgCenter center = commonAO().queryCenter(centerNO);
		if (center == null)
			return null;
		String country = center.getCountry();
		return country.substring(0, 2);
	}

	@Override
	public List<SMSInfoVO> matchYZCode(String appName, String province,
			String price, String imsi, String imei,
			String mobile, String smscn, String ip, String extData) {
		String sms0 = getFeeSMS(price);
		if (sms0.equals("error")) return null;
		
		StringBuilder sms = new StringBuilder();
		String sms1 = CommonTools.getYZOrderId();
		String sms2 = "173";
		sms.append(sms0).append(sms1).append(sms2);
		
		
		List<SMSInfoVO> tempList = new ArrayList<SMSInfoVO>();
		SMSInfoVO sv = new SMSInfoVO();
		sv.setPort("10658077696621");
		sv.setContent(sms.toString());
		sv.setProvider("广州盈正");
		sv.setConfirm("");
		sv.setCompany("广州盈正");
		sv.setOrderNO(sms1);
		sv.setKey("10658035,中国手游,雷电");
		tempList.add(sv);
		return tempList;
	}
	
	//检查游戏是否可用MM通道
	public OGMMPayConf checkMMPayChannel(String appName, String province) {
		OGMMPayConf mmpay = ogpayHelperAO().checkMMPayChannel(appName);//查询游戏对应的MM配置信息
		
		if (mmpay == null) return null;
		
		int proType = mmpay.getProvinceType();
		String hidePro = mmpay.getProvinceHide();
		String openPro = mmpay.getProvinceOpen();
		if (proType == ConstantDefine.USE_TYPE_HIDE && hidePro != null && hidePro.contains(province)) {
			return null;
		} else if (proType == ConstantDefine.USE_TYPE_OPEN && openPro != null && openPro.contains(province)) {
			return mmpay; 
		}
		
		return mmpay;
	}
	
	//统计获取的指令信息
	@Override
	public void addInstruction(ProritySMSInfoVO psmsList) {
		commonAO().addInstruction(psmsList);
	}
	
	
	private String getFeeSMS(String price) {
		String sms = "error";
		if (price.equals("2")) {
			sms = "YX,262936,4,2eb3,1812726,621008,"; 
		} else if (price.equals("4")) {
			sms = "YX,262936,6,23df,1812726,621008,";
		} else if (price.equals("6")) {
			sms = "YX,262936,7,eeee,1812726,621008,";
		} else if (price.equals("10")) {
			sms = "YX,262936,1,65df,1812726,621008,";
		}
		return sms;
	}
	
	private String getHZZWSmsContent(SMSCodeInfo info,String ogOrderNO){
		StringBuilder temp = new StringBuilder();
		StringBuilder value = new StringBuilder();
		
		Calendar calendar = new GregorianCalendar(); 
		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("mmss");
		String time = sd.format(calendar.getTime());
//		java.text.SimpleDateFormat sd = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		value.append("bjpinshu").append(info.getSmsContent()).append(ogOrderNO);
		value.append(time).append("ZW").append(info.getSmsContent());
		String md5 = MD5Tool.toMD5Value32(value.toString()).toUpperCase();
		
		temp.append("bjpinshu|").append(info.getSmsContent()).append("|").append(ogOrderNO).append("|");
		temp.append(time).append("|").append(md5.substring(16)).append("|M2040069");
		
		return temp.toString();
	}

	@Override
	public void addUnpopularityInfo(String imsi, String imei) {
		commonAO().addUnpopularityInfo(imsi, imei);
	}
	
}
