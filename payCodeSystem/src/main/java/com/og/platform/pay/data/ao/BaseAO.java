/**
 * OGPaySystem
 */
package com.og.platform.pay.data.ao;

import com.og.platform.pay.data.dao.CodeHelperDAO;
import com.og.platform.pay.data.dao.CommonDAO;
import com.og.platform.pay.data.dao.OGPayDAO;

/**
 * @author ogplayer.com
 *
 * 2013年10月25日
 */
public class BaseAO {
	protected static CodeHelperDAO codeHeplerDAO = new CodeHelperDAO();
	protected static OGPayDAO ogpayDAO = new OGPayDAO();
	protected static CommonDAO commonDAO = new CommonDAO();
}
