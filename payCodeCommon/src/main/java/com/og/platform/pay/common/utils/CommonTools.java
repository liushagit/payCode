/**
 * OGPayCommon
 */
package com.og.platform.pay.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.payinfo.net.exception.JuiceException;
import com.payinfo.net.log.LoggerFactory;

/**
 * @author ogplayer.com
 *
 * 2013年10月29日
 */
public class CommonTools {
	private static org.apache.log4j.Logger logger = LoggerFactory.getLogger(CommonTools.class);
	
	public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static AtomicInteger atid = new AtomicInteger(1);
	private static AtomicInteger atidDY = new AtomicInteger(1);
	private static AtomicInteger atidYZ = new AtomicInteger(1);
	public static Random random = new Random();
	public static char[] params = {'0','1','2','3','4','5','6','7','8','9', 'q', 'w','e',
		'r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m'};
	
	/**
	 * 获取电话制式
	 * 定位运营商类型
	 * 
	 * 5,6两位就可分辨了
	 * 00,02为移动
	 * 01为联通
	 * 03为电信
	 */
	public static final int locateOperator(String imsi) {
		try {
			if (imsi == null || imsi.length()<15) {
				throw new JuiceException("IMSI:" + imsi + " 格式错误！");
			}
			
			String mcc = imsi.substring(0, 3);
			if (!mcc.equals("460")) {
				throw new JuiceException("IMSI mcc:" + mcc + " 不属于中国!");
			}
			
			// 划定区分网址：http://en.wikipedia.org/wiki/Mobile_country_code
			String mnc = imsi.substring(3, 5);
			if (mnc.equals("00") || mnc.equals("02") || mnc.equals("07")) {
				return ConstantDefine.CODE_TYPE_MOBILE;
			}
			
			if (mnc.equals("01") || mnc.equals("06") || mnc.equals("20")) {
				return ConstantDefine.CODE_TYPE_UNICOM;
			}
			
			if (mnc.equals("03") || mnc.equals("05")) {
				return ConstantDefine.CODE_TYPE_TELNET;
			}
			
			throw new JuiceException("IMSI mnc:" + mnc + " 运营商不能区分！"); 
		} catch (IndexOutOfBoundsException e) {
			logger.error("根据IMSI获取手机号码类型错误");
		} catch (Exception e) {
			logger.error("根据IMSI获取手机号码类型错误");
		}
		return ConstantDefine.CODE_TYPE_MOBILE;
	}

	/**
	 * 获取运营商名称
	 */
	public static final String locateOperatorName(String imsi) {
		switch (locateOperator(imsi)) {
		case ConstantDefine.CODE_TYPE_UNICOM:
			return "联通";
		case ConstantDefine.CODE_TYPE_TELNET:
			return "电信";
		default:
			return "移动";
		}
	}
	
	public static final int locateOperatorCode(String name) {
		if (name == null) return ConstantDefine.CODE_TYPE_MOBILE;
		if (name.contains("联通")) {
			return ConstantDefine.CODE_TYPE_UNICOM;
		} else if (name.contains("电信")) {
			return ConstantDefine.CODE_TYPE_TELNET;
		}
		return ConstantDefine.CODE_TYPE_MOBILE;
	}
	
	
	/**订单号生产规则*/
	public static final String createOrderNO(){
		try {
			Date date = format.parse("1970-01-01 00:00:00");
			int timeSpace = (int)(Calendar.getInstance().getTimeInMillis() - date.getTime())/1000;
			
			StringBuilder uid = new StringBuilder();
			int randomValue = (int)(random.nextFloat() * 100000);
			uid.append(Math.abs(timeSpace)).append(getStrValue(getSeqId(), 4)).append(getStrValue(randomValue, 5));
			return uid.toString();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return null;
	}
	
	/** 获取序列号*/
	private static int getSeqId() {
		if (atid.get()==9999) {
			atid.set(1);
		}
		return atid.getAndIncrement();
	}
	
	/** 数字填零格式化*/
	private static String getStrValue(int param, int length) {
		String value = String.valueOf(param);
		
		StringBuilder msg = new StringBuilder();
		for (int i=0; i<(length-value.length());i++){
			msg.append("0");
		}
		msg.append(param);
		return msg.toString();
	}
	
	/** 电盈订单号*/
	public static String getDYOrderId() {
		char prefix = params[random.nextInt(params.length)];
		StringBuilder id = new StringBuilder();
		id.append(prefix).append(getStrValue(getDYSeqId(), 6));
		return id.toString();
	}
	
	/** 灵光互动订单号 */
	public static String getLGHDOrderId() {
		try {
			/*SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			String orderId = format.format(new Date());*/
			String orderId = "";
			Random ran = new Random();
			for (int i=0; i<9; i++) {
				orderId += ran.nextInt(10);
			}
			return orderId;
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		return null;
	}
	
	/** 获取随机允许字母的订单号 */
	public static String getMixedOrderId(int len) {
		StringBuilder orderId = new StringBuilder();
		for (int i=0; i<len; i++) {
			orderId.append(params[random.nextInt(params.length)]);
		}
		return orderId.toString();
	}
	
	/** 获取电盈序列号*/
	private static int getDYSeqId() {
		if (atidDY.get()==999999) {
			atidDY.set(1);
		}
		return atidDY.getAndIncrement();
	}
	
	/** 盈正订单号*/
	public static String getYZOrderId() {
		StringBuilder id = new StringBuilder();
		for (int i=0;i<10;i++) {
			char prefix = params[random.nextInt(params.length)];
			id.append(prefix);
		}
		id.append(getStrValue(getYZSeqId(), 9));
		return id.toString();
	}
	
	/** 获取盈正序列号*/
	private static int getYZSeqId() {
		if (atidYZ.get()==999999999) {
			atidYZ.set(1);
		}
		return atidYZ.getAndIncrement();
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
	
	
	/**
	 * 校验IMSI
	 */
	public static boolean IMSICheck(String imsi) {
		boolean result = true;
		if (imsi == null || imsi.length()<15) {
			result = false;
		}
		return result;
	}
	
	/**
	 * 校验IMEI
	 */
	public static boolean IMEICheck(String imei) {
		boolean result = true;
		if (imei == null || imei.length()<12) {
			result = false;
		}
		return result;
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println(locateOperatorName("460006120448849"));
		/*Random ran = new Random();
		for(int i=0;i<100;i++) {
			SimpleDateFormat format = new SimpleDateFormat("HHmmss");
			String orderId = format.format(new Date());
			for (int j=0; j<3; j++) {
				orderId += ran.nextInt(10);
			}
			System.out.println(orderId);
		}*/
			
	}
}
