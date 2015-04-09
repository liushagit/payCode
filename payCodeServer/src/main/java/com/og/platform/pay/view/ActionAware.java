/**
 * SuperStarServer
 * com.orange.superstar.server
 * ActionAware.java
 */
package com.og.platform.pay.view;

import com.og.platform.pay.rpc.action.CommonAction;
import com.payinfo.net.container.Container;

/**
 * @author author
 * 2013-5-6
 */
public class ActionAware extends Application {
	public static final String REMOTE_PREFIX_SYSTEM = "OGPaySystem";
	
	protected static CommonAction commonAction = (CommonAction) Container.
			createRemoteService(CommonAction.class, REMOTE_PREFIX_SYSTEM);
}
