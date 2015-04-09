/**
 * OGPaySystem
 */
package com.og.platform.pay.data.utils;

import java.util.Comparator;

import com.og.platform.pay.common.domain.OGPayInfo;
import com.og.platform.pay.common.domain.SMSCodeInfo;
import com.og.platform.pay.common.vo.ProritySMSInfoVO;

/**
 * @author ogplayer.com
 *
 * 2013年10月31日
 */
public class PriorityComparator<T> implements Comparator<T> {
	@Override
	public int compare(T o1, T o2) {
		int first = 0;
		int second = 0;
		if (o1 instanceof OGPayInfo) {
			first = ((OGPayInfo)o1).getPriority();
			second = ((OGPayInfo)o2).getPriority();
		} else if (o1 instanceof SMSCodeInfo) {
			first = ((SMSCodeInfo)o1).getPriority();
			second = ((SMSCodeInfo)o2).getPriority();
		} else if (o1 instanceof ProritySMSInfoVO) {
			first = ((ProritySMSInfoVO)o1).getPriority();
			second = ((ProritySMSInfoVO)o2).getPriority();
		}
		
		if (first < second) {
			return 1;
		} else if (first > second) {
			return -1;
		}
		return 0;
	}
}
