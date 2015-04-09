/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.og.platform.pay.common.domain.SMSCodeInfo;

/**
 * @author ogplayer.com
 *
 * 2013年10月31日
 */
public class PriceComparator implements Comparator<SMSCodeInfo> {
	@Override
	public int compare(SMSCodeInfo o1, SMSCodeInfo o2) {
		if (o1.getPrice() < o2.getPrice()) {
			return 1;
		} else if (o1.getPrice() > o2.getPrice()) {
			return -1;
		}
		return 0;
	}
	
	public static void main(String[] args) {
		SMSCodeInfo o1 = new SMSCodeInfo();
		o1.setCodeNO("1");
		o1.setPrice(1);
		
		SMSCodeInfo o2 = new SMSCodeInfo();
		o2.setCodeNO("2");
		o2.setPrice(2);
		
		SMSCodeInfo o3 = new SMSCodeInfo();
		o3.setCodeNO("8");
		o3.setPrice(8);
		
		List<SMSCodeInfo> tempList = new ArrayList<SMSCodeInfo>();
		tempList.add(o1);
		tempList.add(o3);
		tempList.add(o2);
		
		Collections.sort(tempList, new PriceComparator());
		System.out.println(tempList.get(0).getPrice());
	}
}
