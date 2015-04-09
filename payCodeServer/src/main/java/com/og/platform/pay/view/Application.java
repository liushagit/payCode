package com.og.platform.pay.view;

import org.apache.log4j.Logger;

import com.og.platform.pay.view.server.OGAnalyServer;
import com.og.platform.pay.view.server.OGCodeServer;
import com.payinfo.net.container.Container;
import com.payinfo.net.log.LoggerFactory;

/**
 * @author author 2013-5-3
 */
public class Application {
	private static Logger logger = LoggerFactory.getLogger(Application.class);

	//
	public void init() {
		initServer();
		//
	}

	public void initServer() {
		logger.info("Init servers......");
		// 用户服务
		Container.registerServer("code", new OGCodeServer());
		Container.registerServer("analy", new OGAnalyServer());
	}
}
