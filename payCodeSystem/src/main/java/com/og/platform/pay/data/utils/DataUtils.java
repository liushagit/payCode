/**
 * MMSDKServer
 */
package com.og.platform.pay.data.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.payinfo.net.log.LoggerFactory;

/**
 * @author ogplayer.com
 *
 * 2013年11月27日
 */
public class DataUtils {
	private static Logger logger = LoggerFactory.getLogger(DataUtils.class);
	
	public static final String user_dir = System.getProperty("user.dir");
	public static Random random = new Random();
	public static String fDataPath = user_dir + File.separatorChar + "data" + File.separatorChar + "imsi";
	public static List<IMSIInfo> imsiList;
	public static AtomicInteger aint;
	
	private static DataUtils data;
	
	private DataUtils() {
		imsiList = new ArrayList<IMSIInfo>();
		aint = new AtomicInteger(0);
	}
	
	public static DataUtils getInstance() {
		if (data == null) {
			data = new DataUtils();
			imsiList = data.readData();
		}
		return data;
	}
	
	public List<IMSIInfo> readData() {
		List<IMSIInfo> tempList = new ArrayList<IMSIInfo>();
		try {
			FileReader fReader = new FileReader(fDataPath);
			BufferedReader br = new BufferedReader(fReader);
			
			String temp = "";
			while((temp=br.readLine())!=null) {
				String[] params = temp.split("\\,");
				if (params.length == 2) {
					IMSIInfo info = new IMSIInfo();
					info.setImsi(params[0]);
					info.setImei(params[1]);
					tempList.add(info);
				}
			}
			
			br.close();
			fReader.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e.getCause());
		}
		
		return tempList;
	}
	
	public IMSIInfo nextIMSI() {
		if (aint.get() >= imsiList.size()) {
			aint.set(0);
		}
		IMSIInfo imsi = imsiList.get(aint.getAndIncrement());
		return imsi;
	}
	
	public static void main(String[] args) {
		for (int i=0; i<1000; i++) {
			IMSIInfo info = DataUtils.getInstance().nextIMSI();
			System.out.println(info.getImsi()+","+info.getImei());
		}
	}
}
