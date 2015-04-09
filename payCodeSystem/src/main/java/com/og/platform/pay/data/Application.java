package com.og.platform.pay.data;

import org.apache.log4j.Logger;

import com.og.platform.pay.data.ao.CodeHelperAO;
import com.og.platform.pay.data.ao.CommonAO;
import com.og.platform.pay.data.ao.OGPayHelperAO;
import com.payinfo.net.log.LoggerFactory;

/**
 * @author author 2013-5-3
 */
public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);
	//
	private static CodeHelperAO codeHelperAO;
	private static OGPayHelperAO ogpayHelperAO;
	private static CommonAO commonAO;
	//测试
	//
	public void init() {
		codeHelperAO = new CodeHelperAO();
		ogpayHelperAO = new OGPayHelperAO();
		commonAO = new CommonAO();
	}

	public CodeHelperAO codeHelperAO() {
		return codeHelperAO;
	}

	public OGPayHelperAO ogpayHelperAO() {
		return ogpayHelperAO;
	}
	
	public CommonAO commonAO() {
		return commonAO;
	}
}
