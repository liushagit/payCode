/**
 * OGPayServer
 */
package com.og.platform.pay.view.utils;

import java.util.concurrent.atomic.AtomicInteger;

import com.og.platform.pay.common.utils.CommonTools;

/**
 * @author ogplayer.com
 *
 * 2013年12月2日
 */
public class RuleHelperTool {
	public static AtomicInteger at = new AtomicInteger(0);
	  
	public static boolean isChannelCheck(String ext) {
		String[] params = ext.split("\\@");
		if (params.length == 3) {
			String channel = params[1];
			if (channel.startsWith("lwxx0008") || channel.startsWith("lwxx0009")) {
				return false;
			}
			String prefix = channel.substring(0, 4);
			String last = channel.substring(4);
			int channelNum = CommonTools.convertPrice(last);
			if (prefix.startsWith("lwxx") && channelNum>= 1001 && channelNum <=1040) {
				int value = getNextInteger();
				if (value ==8 || value==4 || value== 6) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	private static int getNextInteger() {
		if (at.get() >= 10) {
			at.set(0);
		}
		return at.getAndIncrement();
	}

	
	public static void main(String[] args) {
		int num = 0;
		for (int i=0; i<100; i++) {
			System.out.println(getNextInteger());
		}
		System.out.println(num);
	}
}
